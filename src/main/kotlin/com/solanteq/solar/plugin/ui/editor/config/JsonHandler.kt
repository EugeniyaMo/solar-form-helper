package com.solanteq.solar.plugin.ui.editor.config

import com.google.gson.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import java.io.File

class JsonHandler {

    fun insertIntoFile(actionType: ActionType, newJsonObject: String, existingPsiObject: PsiElement, file: File) {
        val lineNumber = getPositionFile(actionType, existingPsiObject)

        insertIntoPosition(newJsonObject, lineNumber, file)
    }

    fun generateJson(formObject: Any): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(formObject)
    }

    fun removeFromFile(existingPsiObject: PsiElement, file: File) {
        val text = file.readText()

        val lines = text.split("\n") // Разбиваем текст на строки
        val startLineNumber = getStartLineNumber(existingPsiObject)
        val endLineNumber = getEndLineNumber(existingPsiObject)

        val updatedLines = lines.filterIndexed { index, _ -> index < startLineNumber || index >= endLineNumber }

        file.writeText(updatedLines.joinToString("\n"))
    }

    fun editIntoFile(actionType: ActionType, newJsonObject: String, existingPsiObject: PsiElement, file: File) {
        val lineNumber = getPositionFile(actionType, existingPsiObject)
        removeFromFile(existingPsiObject, file)
        insertIntoPosition(newJsonObject, lineNumber, file)
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

    private fun insertIntoPosition(newJsonObject: String, position: Int, file: File) {
        val lines = file.readLines().toMutableList()
        lines.add(position, "$newJsonObject,")

        val jsonString = lines.joinToString("\n")


        val jsonElement = JsonParser.parseString(jsonString)
        val prettyJson = generateJson(jsonElement)

        file.writeText(prettyJson)
    }

}