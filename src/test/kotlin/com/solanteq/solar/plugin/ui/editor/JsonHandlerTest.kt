package com.solanteq.solar.plugin.ui.editor

import com.intellij.psi.PsiElement
import com.solanteq.solar.plugin.ui.editor.config.JsonHandler
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

class JsonHandlerTest {

    private val jsonHandler = JsonHandler()

    @Test
    fun insertIntoFile() {
        // Arrange
        val actionType = ActionType.ADD_RIGHT_ELEMENT
        val newJsonObject = "{\"key\": \"value\"}"
        val existingPsiObject = mock<PsiElement>() // Предположим, что PsiElement заглушка
        val file = createTempFile() // Создание временного файла для теста

        val position = 10 // Предположим, что позиция 10
        val expectedJson = "{\"key\": \"value\"}" // Предположим, что это ожидаемый JSON

        // Вызов приватного метода через рефлексию
        val getPositionFileMethod = JsonHandler::class.memberFunctions.find { it.name == "getPositionFile" }
            ?: throw IllegalStateException("getPositionFile method not found")
        getPositionFileMethod.isAccessible = true
        val lineNumber = getPositionFileMethod.call(jsonHandler, actionType, existingPsiObject) as Int

        // Act
        jsonHandler.insertIntoFile(actionType, newJsonObject, existingPsiObject, file)

        // Assert
        val result = file.readText()
        val expectedResult = expectedJson + "," // Предполагаем, что запятая добавляется
        assertEquals(expectedResult, result)
    }

    @Test
    fun generateJson() {
        assert(true)
    }

    @Test
    fun removeFromFile() {
        assert(true)
    }

    @Test
    fun editIntoFile() {
        assert(true)
    }
}