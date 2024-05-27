package com.solanteq.solar.plugin.ui.editor

import com.solanteq.solar.plugin.ui.editor.data.FormFieldData
import com.solanteq.solar.plugin.ui.editor.config.JsonHandler
import com.intellij.psi.PsiElement
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import java.io.File

class FormUpdateService(
    editor: FormEditor
) {

    private var jsonHandler = JsonHandler()
    private val editor = editor

    fun addField(actionType: ActionType, formField: FormFieldData, exitingPsiElement: PsiElement, filePath: String) {
        val json = jsonHandler.generateJson(formField)
        jsonHandler.insertIntoFile(actionType, json, exitingPsiElement, getFile(filePath))

        editor.applyState()
    }

    fun removeField(exitingPsiElement: PsiElement, filePath: String) {
        jsonHandler.removeFromFile(exitingPsiElement, getFile(filePath))
        editor.applyState()
    }

    fun editField(formField: FormFieldData, exitingPsiElement: PsiElement, filePath: String) {
        val json = jsonHandler.generateJson(formField)
        jsonHandler.editIntoFile(ActionType.ADD_RIGHT_ELEMENT, json, exitingPsiElement, getFile(filePath))

        editor.applyState()
    }

    private fun preparePath(filePath: String): String {
        return filePath.substring(COUNT_OF_EXTRA_SYMBOLS)
    }

    private fun getFile(filePath: String): File {
        return File(preparePath(filePath))
    }

    companion object {
        private const val COUNT_OF_EXTRA_SYMBOLS = 5
    }

    fun setJsonHandler(jsonHandler: JsonHandler) {
        this.jsonHandler = jsonHandler
    }
}