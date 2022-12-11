package com.solanteq.solar.plugin.element

import com.intellij.json.psi.JsonElement
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonProperty
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiSubstitutor
import com.intellij.psi.PsiType
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.TypeConversionUtil
import com.solanteq.solar.plugin.element.base.FormLocalizableElement
import com.solanteq.solar.plugin.reference.form.FormReference
import org.jetbrains.kotlin.idea.base.util.allScope
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UField
import org.jetbrains.uast.toUElementOfType

/**
 * A single object inside `fields` array in [FormRow] element.
 *
 * Each form field contains a reference to corresponding data class field (property).
 * For simplicity, we will call fields in data classes "properties".
 * Any data class may have another data classes as properties,
 * they can have their own data classes as properties, and so on.
 * This structure will form a chain of properties, and the final one must be a primitive type:
 * ```
 * "client.type.id"
 * ```
 * Example:
 * ```
 * "fields": [
 *   { //Field 1
 *     "name": "property",
 *     "fieldSize": 4,
 *     "type": "STRING"
 *   },
 *   { //Field 2
 *     "name": "property.nestedProperty",
 *     "fieldSize": 4,
 *     "type": "STRING"
 *   }
 * ]
 * ```
 */
class FormField(
    sourceElement: JsonObject
) : FormLocalizableElement<JsonObject>(sourceElement, sourceElement) {

    /**
     * A list of properties as a chain from main to nested ones represented as raw strings.
     *
     * Example:
     * ```
     * "fields": [
     *   {
     *     "name": "property.nestedProperty.nextNestedProperty.",
     *     "fieldSize": 4,
     *     "type": "STRING"
     *   }
     * ]
     * ```
     *
     * This will return:
     * ```
     * [
     *   "property",
     *   "nestedProperty",
     *   "nextNestedProperty",
     *   "" //We have an empty string here, because field name ends with a dot
     * ]
     * ```
     *
     */
    val stringPropertyChain by lazy { name?.split(".") }

    /**
     * A list of [FieldProperty] as a chain from main to nested ones represented as UAST fields.
     * Works similar to [stringPropertyChain].
     *
     * If any nested property is not resolved, every property to the right won't be resolved too
     * and the returned chain will only contain references to resolved properties.
     *
     * Example: consider we have a chain of five fields `field1`, `field2`...
     * If we make a typo at `field3`, then only `field1` and `field2` will be resolved.
     *
     * ```
     * "name": "field1.field2.fieldWithTypo.field4.field5"
     * -> [field1, field2, null, null, null]
     * ```
     */
    val propertyChain by lazy {
        val stringPropertyChain = stringPropertyChain ?: return@lazy emptyList()
        if(stringPropertyChain.isEmpty()) {
            return@lazy emptyList()
        }

        val propertyChain = mutableListOf<FieldProperty>()
        var dataClasses: List<UClass> = if(dataClass != null) {
            listOf(dataClass!!)
        } else {
            dataClassesFromInlineRequests
        }

        stringPropertyChain.forEach { fieldName ->
            if(dataClasses.isEmpty()) {
                propertyChain += FieldProperty(fieldName, emptyList(), null, null)
                return@forEach
            }

            val (containingClass, field) = findClassAndFieldByNameInClasses(dataClasses, fieldName)
            if(field == null) {
                propertyChain += FieldProperty(fieldName, dataClasses, containingClass, null)
                return@forEach
            }

            propertyChain += FieldProperty(fieldName, dataClasses, containingClass, field)

            val nextDataClass = psiTypeAsUClassOrNull(field.type)
            dataClasses = if(nextDataClass != null) listOf(nextDataClass) else emptyList()
        }

        return@lazy propertyChain.toList()
    }

    /**
     * Data class from source request that this field uses
     *
     * TODO move to FormTopLevelFile and cache
     */
    val dataClass by lazy {
        val sourceRequest = sourceRequest ?: return@lazy null
        val method = sourceRequest.methodFromRequest ?: return@lazy null
        val derivedClass = sourceRequest.serviceFromRequest?.javaPsi ?: return@lazy null
        val superClass = method.containingClass ?: return@lazy null
        val rawReturnType = method.returnType ?: return@lazy null
        return@lazy substitutePsiType(
            superClass,
            derivedClass,
            rawReturnType
        )
    }

    private val sourceRequest by lazy {
        val formTopLevelFile = containingFile.toFormElement<FormTopLevelFile>() ?: return@lazy null
        return@lazy formTopLevelFile.sourceRequest
    }

    //TODO move to FormTopLevelFile and cache
    private val inlineRequests: List<FormRequest> by lazy {
        val containingFile = containingFile ?: return@lazy emptyList()
        val references = ReferencesSearch.search(containingFile, project.allScope()).findAll()
        val formPropertyValueElements = references.filterIsInstance<FormReference>().map { it.element }
        val formInlineElements = formPropertyValueElements.mapNotNull {
            val formProperty = it.parent as? JsonProperty ?: return@mapNotNull null
            val inlineValueObject = formProperty.parent as? JsonObject ?: return@mapNotNull null
            val inlineProperty = inlineValueObject.parent as? JsonProperty ?: return@mapNotNull null
            return@mapNotNull inlineProperty.toFormElement<FormInline>()
        }
        return@lazy formInlineElements.mapNotNull { it.request }
    }

    private val dataClassesFromInlineRequests by lazy {
        inlineRequests.mapNotNull {
            val method = it.methodFromRequest ?: return@mapNotNull null
            val derivedClass = it.serviceFromRequest?.javaPsi ?: return@mapNotNull null
            val superClass = method.containingClass ?: return@mapNotNull null
            val rawReturnListType = method.returnType as? PsiClassType ?: return@mapNotNull null
            val rawReturnType = rawReturnListType.parameters.firstOrNull() ?: return@mapNotNull null
            return@mapNotNull substitutePsiType(
                superClass,
                derivedClass,
                rawReturnType
            )
        }
    }

    private fun findClassAndFieldByNameInClasses(uClasses: List<UClass>, fieldName: String): Pair<UClass?, UField?> {
        uClasses.forEach { uClass ->
            uClass.javaPsi.allFields
                .find { it.name == fieldName }
                ?.let { return uClass to it.toUElementOfType() }
        }
        return null to null
    }

    private fun substitutePsiType(superClass: PsiClass, derivedClass: PsiClass, psiType: PsiType): UClass? {
        val substitutedReturnType = TypeConversionUtil.getClassSubstitutor(
            superClass,
            derivedClass,
            PsiSubstitutor.EMPTY
        )?.substitute(psiType)
        val classReturnType = substitutedReturnType as? PsiClassType ?: return null
        return classReturnType.resolve().toUElementOfType()
    }

    private fun psiTypeAsUClassOrNull(psiType: PsiType): UClass? {
        val classReturnType = psiType as? PsiClassType ?: return null
        return classReturnType.resolve().toUElementOfType()
    }

    /**
     * @property name represents a name of this property.
     * Never null, but might be referencing to non-existing field.
     * @property applicableClasses List of classes from inline requests,
     * or a single class directly from source request
     * @property containingClass Data class containing this field
     * @property referencedField Real psi element resolved from [name] property
     */
    data class FieldProperty(
        val name: String,
        val applicableClasses: List<UClass>,
        val containingClass: UClass?,
        val referencedField: UField?
    )

    companion object : FormElementCreator<FormField> {

        const val ARRAY_NAME = "fields"

        override fun create(sourceElement: JsonElement): FormField? {
            if(canBeCreatedAsArrayElement(sourceElement, ARRAY_NAME)) {
                return FormField(sourceElement as JsonObject)
            }
            return null
        }

    }

}