package com.solanteq.solar.plugin.ui.editor

import com.intellij.psi.PsiElement
import com.solanteq.solar.plugin.ui.editor.data.FormFieldData
import com.solanteq.solar.plugin.ui.editor.config.JsonHandler
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import org.junit.Ignore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class FormUpdateServiceTest {

    @Mock
    private var jsonHandler: JsonHandler = mock<JsonHandler>()

//    @InjectMocks
//    private var formUpdateService: FormUpdateService = FormUpdateService(editor)

    val editor = mock<FormEditor>()
    val existingPsiElement = mock<PsiElement>()

    @Test
    fun removeField() {
        assert(true)
    }

    @Test
    fun editField() {
        assert(true)
    }

    @Test
    fun addField() {
        // Preparation
        val formUpdateService = FormUpdateService(editor)
        val spyJsonHandler = spy(JsonHandler())

        formUpdateService.setJsonHandler(spyJsonHandler)
        val jsonCaptor = argumentCaptor<String>()
        val fileCaptor = argumentCaptor<File>()

        // Parameters
        val formField = FormFieldData(
            visibleWhen = "isNew",
            name = "field",
            fieldSize = 4.toString(),
            labelSize = 3.toString(),
            type = "STRING"
        )
        val formFieldJson = "{\n" +
            "  \"visibleWhen\": \"isNew\",\n" +
            "  \"name\": \"field\",\n" +
            "  \"fieldSize\": \"4\",\n" +
            "  \"labelSize\": \"3\",\n" +
            "  \"type\": \"STRING\"\n" +
            "}"
        val filePath = "...../Users/emotorina/IdeaProjects/solar-form-helper/test.txt"

        // Mocking
        doNothing().`when`(spyJsonHandler).insertIntoFile(any(), any(), any(), any())

        // Execution
        formUpdateService.addField(ActionType.ADD_RIGHT_ELEMENT, formField, mock<PsiElement>(), filePath)

        // Verification
        verify(spyJsonHandler).insertIntoFile(any(), jsonCaptor.capture(), any(), fileCaptor.capture())

        val json = jsonCaptor.firstValue
        val file = fileCaptor.firstValue

        assertEquals(formFieldJson, json)
        assertEquals("/Users/emotorina/IdeaProjects/solar-form-helper/test.txt", file.absolutePath)
    }

}