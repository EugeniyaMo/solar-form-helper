package com.solanteq.solar.plugin

import com.intellij.psi.PsiFile
import com.intellij.testFramework.RunsInEdt
import com.solanteq.solar.plugin.reference.form.FormReference
import com.solanteq.solar.plugin.reference.request.ServiceMethodReference
import com.solanteq.solar.plugin.reference.request.ServiceNameReference
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@RunsInEdt
class FormReferenceTest : FormTestBase() {

    override fun getTestDataSuffix() = "reference"

    @Test
    fun `test valid reference right after request literal with java service`() {
        doTestMethodReference("ServiceImpl.java",
            """
            {
              "request": "test.service.<caret>findData"
            }
        """.trimIndent())
    }

    @Test
    fun `test valid reference inside request object after name literal with kotlin service`() {
        doTestMethodReference("ServiceImpl.kt",
            """
            {
              "save": {
                "name": "test.service.<caret>findData"
              }
            }
        """.trimIndent())
    }

    @ParameterizedTest
    @ValueSource(strings = [
        "TestService",
        "NonConventionalServiceName"
    ])
    fun `test service name reference with different namings`(serviceName: String) {
        fixture.configureByText("TestService.kt", """
            import com.solanteq.solar.commons.annotations.CallableService

            @CallableService
            interface $serviceName {

                @Callable
                fun findData(viewParams: ViewParams): List<TestData>

            }
        """.trimIndent())

        fixture.configureByText("TestServiceImpl.kt", """
            import org.springframework.stereotype.Service

            @Service("test.testService")
            class ${serviceName}Impl : $serviceName {

                override fun findData(viewParams: ViewParams): List<TestData> {
                    return listOf()
                }

            }
        """.trimIndent())

        fixture.configureByFormText(
            """
            {
              "request": "<caret>test.testService.findData"
            }
        """.trimIndent())

        val serviceReference = fixture.file.findReferenceAt(fixture.caretOffset)

        assertNotNull(serviceReference)
        assertTrue(serviceReference is ServiceNameReference)

        val referencedService = serviceReference!!.resolve().toUElement() as? UClass

        assertNotNull(referencedService)
        assertTrue(referencedService is UClass)
        assertEquals("${serviceName}Impl", referencedService!!.javaPsi.name)
    }

    @Test
    fun `test form reference with group`() {
        fixture.createForm(
            "testFormNoGroup",
            "{}"
        )
        fixture.createForm(
            "testFormWithGroup",
            "{}",
            "test"
        )
        fixture.createForm(
            "testFormWithGroup",
            "{}",
            "test2"
        )

        fixture.configureByFormText("""
            {
              "form": "<caret>test.testFormWithGroup"
            }
        """.trimIndent()
        )

        val reference = fixture.file.findReferenceAt(fixture.caretOffset)
        assertNotNull(reference)
        assertTrue(reference is FormReference)

        val referencedForm = reference!!.resolve()

        assertNotNull(referencedForm)
        assertTrue(referencedForm is PsiFile)
        assertEquals("testFormWithGroup.json", (referencedForm as PsiFile).name)
    }

    @Test
    fun `test reference to super method`() {
        fixture.configureByFiles("ServiceImpl.kt", "SuperServiceImpl.kt")
        fixture.configureByFormText("""
            {
              "request": "test.service.<caret>superMethod"
            }
        """.trimIndent())

        assertMethodReference("superMethod")
    }

    private fun doTestMethodReference(serviceClassPath: String, formText: String) {
        fixture.configureByFile(serviceClassPath)
        fixture.configureByFormText(formText)

        assertMethodReference("findData")
    }

    private fun assertMethodReference(methodName: String) {
        val methodReference = fixture.file.findReferenceAt(fixture.caretOffset)

        assertNotNull(methodReference)
        assertTrue(methodReference is ServiceMethodReference)

        val referencedMethod = methodReference!!.resolve().toUElement() as? UMethod

        assertNotNull(referencedMethod)
        assertTrue(referencedMethod is UMethod)
        assertEquals(methodName, referencedMethod!!.name)
    }

}