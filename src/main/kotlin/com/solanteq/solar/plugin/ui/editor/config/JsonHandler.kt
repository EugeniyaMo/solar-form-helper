package com.solanteq.solar.plugin.ui.editor.config

import com.google.gson.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import java.io.File

class JsonHandler {

    fun insertIntoFile(actionType: ActionType, newJsonObject: JsonObject, existingPsiObject: PsiElement, file: File) {
        val lines = file.readLines().toMutableList()

        val lineNumber = getPositionFile(actionType, existingPsiObject)

        lines.add(lineNumber, "$newJsonObject,")

        val jsonString = lines.joinToString("\n")


        val jsonElement = JsonParser.parseString(jsonString)
        val prettyJson = generateJson(jsonElement)

        file.writeText(prettyJson)
    }

    fun generateJson(formObject: Any): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(formObject)
    }

    private fun getPositionFile(actionType: ActionType, element: PsiElement): Int {
        return when (actionType) {
            ActionType.ADD_RIGHT_ELEMENT -> getStartLineNumber(element)
            ActionType.ADD_LEFT_ELEMENT -> getEndLineNumber(element)
            // TODO: do norm exceptions
            else -> throw Exception("error")
        }
    }

    private fun getStartLineNumber(element: PsiElement): Int {
        val containingFile: PsiFile? = element.containingFile
        return containingFile?.viewProvider?.document?.getLineNumber(element.textOffset) ?: -1
    }

    private fun getEndLineNumber(element: PsiElement): Int {
        val containingFile: PsiFile? = element.containingFile
        val textRange = element.textRange
        val endOffset = textRange.endOffset
        val lineNumber = containingFile?.viewProvider?.document?.getLineNumber(endOffset) ?: -1
        return lineNumber + 1
    }

}