//package com.solanteq.solar.plugin.ui.editor.graphic
//
//import org.junit.jupiter.api.Test
//
//class EditingPopupMenuTest {
//
//    @Test
//    fun editingPopupMenuInitTest() {
//        assert(true)
//    }
//
//    @Test
//    fun createMenuItemTest() {
//        assert(true)
//    }
//
//    @Test
//    fun showDialogTest() {
//        assert(true)
//    }
//}

import com.intellij.json.psi.JsonElement
import com.intellij.json.psi.JsonObject
import com.solanteq.solar.plugin.element.base.FormLocalizableElement
import com.solanteq.solar.plugin.ui.editor.FormEditor
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import com.solanteq.solar.plugin.ui.editor.graphic.EditingPopupMenu
import org.assertj.swing.core.GenericTypeMatcher
import org.assertj.swing.edt.GuiActionRunner
import org.assertj.swing.fixture.JPopupMenuFixture
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.swing.JMenuItem
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class EditingPopupMenuTest : AssertJSwingJUnitTestCase() {

    private lateinit var editor: FormEditor
    private lateinit var formElement: FormLocalizableElement<*>
    private lateinit var popupMenu: EditingPopupMenu

    override fun onSetUp() {
        GuiActionRunner.execute<Unit> {
            editor = mock<FormEditor>()
            formElement = mock<FormLocalizableElement<JsonElement>>()
            popupMenu = EditingPopupMenu(editor, formElement)
        }
    }

    @Test
    fun testMenuItemsCreation() {
        val popupMenuFixture = JPopupMenuFixture(robot(), popupMenu)

        GuiActionRunner.execute<Unit> {
            popupMenuFixture
        }

        popupMenuFixture.menuItem(object : GenericTypeMatcher<JMenuItem>(JMenuItem::class.java) {
            override fun isMatching(item: JMenuItem?): Boolean {
                return item != null && item.text == ActionType.ADD_RIGHT_ELEMENT.toString()
            }
        }).requireVisible()

        // Проверка наличия остальных пунктов меню аналогичным образом
    }

    // Тесты для проверки действий при выборе пунктов меню
    @Test
    fun testAddRightElementAction() {
        // Мокаем FormUpdateDispatcher и проверяем, что вызывается метод addField
    }

    @Test
    fun testAddLeftElementAction() {
        // Аналогично как для testAddRightElementAction
    }

    @Test
    fun testEditElementAction() {
        // Мокаем showDialog и проверяем, что вызывается нужный метод
    }

    @Test
    fun testRemoveElementAction() {
    }
}
// М
