package com.solanteq.solar.plugin.index

import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.*
import com.intellij.util.io.EnumeratorStringDescriptor
import com.solanteq.solar.plugin.element.FormJsonInclude
import com.solanteq.solar.plugin.element.toFormElement
import com.solanteq.solar.plugin.util.isForm

class JsonIncludeFileIndex : ScalarIndexExtension<String>() {

    override fun getName() = NAME

    override fun getIndexer() = DataIndexer<String, Void, FileContent> { fileContent ->
        val file = fileContent.psiFile as? JsonFile ?: return@DataIndexer emptyMap()
        val jsonIncludesMap = mutableMapOf<String, Void?>()
        PsiTreeUtil.processElements(file, JsonStringLiteral::class.java) {
            val jsonInclude = it.toFormElement<FormJsonInclude>() ?: return@processElements true
            val formName = jsonInclude.formName ?: return@processElements true
            jsonIncludesMap[formName] = null
            true
        }
        jsonIncludesMap
    }

    override fun getKeyDescriptor(): EnumeratorStringDescriptor =
        EnumeratorStringDescriptor.INSTANCE

    override fun getVersion() = 1

    override fun getInputFilter() = FileBasedIndex.InputFilter { it.isForm() }

    override fun dependsOnFileContent() = true

    companion object {

        val NAME = ID.create<String, Void>("JsonIncludeFileIndex")

    }

}