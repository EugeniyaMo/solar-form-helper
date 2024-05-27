package com.solanteq.solar.plugin.ui.editor

import com.solanteq.solar.plugin.element.FormRootFile
import com.solanteq.solar.plugin.element.base.FormLocalizableElement

class FormUpdateDispatcher(
    editor: FormEditor
) {

    private val formUpdateService = FormUpdateService(editor)

    fun removeField(formRootFile: FormRootFile, formElement: FormLocalizableElement<*>) {
        val exitingPsiElement = formElement.sourceElement.originalElement
        val filePath = formRootFile.virtualFile.toString()
        formUpdateService.removeField(exitingPsiElement, filePath)
    }


}