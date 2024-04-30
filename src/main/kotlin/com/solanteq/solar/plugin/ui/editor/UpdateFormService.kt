package com.solanteq.solar.plugin.ui.editor

import com.google.gson.Gson
import com.solanteq.solar.plugin.ui.component.form.data.FormFieldData
import com.solanteq.solar.plugin.ui.editor.config.JsonHandler
import com.google.gson.JsonObject
import com.intellij.psi.PsiElement
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import java.io.File

class UpdateFormService {

    private val jsonHandler = JsonHandler()

    fun addField(actionType: ActionType, formField: FormFieldData, exitingPsiElement: PsiElement, filePath: String) {
        val json = jsonHandler.generateJson(formField)

        val file = File(preparePath(filePath))
        val jsonObject = Gson().fromJson(json, JsonObject::class.java)

        jsonHandler.insertIntoFile(actionType, jsonObject, exitingPsiElement, file)
    }

    private fun preparePath(filePath: String): String {
        return filePath.substring(COUNT_OF_EXTRA_SYMBOLS)
    }

    companion object {
        private const val COUNT_OF_EXTRA_SYMBOLS = 5
    }
}