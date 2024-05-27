//package com.solanteq.solar.plugin.ui.editor
//
//import com.intellij.openapi.vfs.VirtualFile
//import org.junit.jupiter.api.Test
//import com.intellij.psi.PsiElement
//import com.solanteq.solar.plugin.element.FormRootFile
//import com.solanteq.solar.plugin.element.base.FormLocalizableElement
//import com.solanteq.solar.plugin.ui.editor.data.FormFieldData
//import com.solanteq.solar.plugin.ui.editor.config.JsonHandler
//import com.solanteq.solar.plugin.ui.editor.enums.ActionType
//import org.junit.Before
//import org.junit.Ignore
//import org.junit.runner.RunWith
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.MockitoAnnotations
//import org.mockito.junit.MockitoJUnitRunner
//import org.mockito.kotlin.*
//import java.io.File
//
//class FormUpdateDispatcherTest {
//
//    @Mock
//    private lateinit var editor: FormEditor
//
//    @Mock
//    private lateinit var formRootFile: FormRootFile
//
//    @Mock
//    private lateinit var formElement: FormLocalizableElement<*>
//
//    @Mock
//    private lateinit var exitingPsiElement: PsiElement
//
//    @Mock
//    private lateinit var virtualFile: VirtualFile
//
//    @Mock
//    private lateinit var formUpdateService: FormUpdateService
//
//    private lateinit var formUpdateDispatcher: FormUpdateDispatcher
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//        formUpdateService = FormUpdateService(editor)
//        formUpdateDispatcher = FormUpdateDispatcher(editor, formUpdateService)
//        `when`(formRootFile.virtualFile).thenReturn(virtualFile)
//        `when`(formElement.sourceElement.originalElement).thenReturn(exitingPsiElement)
//    }
//
//    @Test
//    fun testRemoveField() {
//        val filePath = "test/path"
//        `when`(virtualFile.toString()).thenReturn(filePath)
//
//        formUpdateDispatcher.removeField(formRootFile, formElement)
//
//        // Проверяем, вызывался ли метод removeField с правильными аргументами
//        verify(formUpdateService).removeField(exitingPsiElement, filePath)
//    }
//
//}