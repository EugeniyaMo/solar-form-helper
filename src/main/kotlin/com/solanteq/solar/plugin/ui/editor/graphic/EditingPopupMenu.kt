package com.solanteq.solar.plugin.ui.editor.graphic

import com.solanteq.solar.plugin.element.base.FormLocalizableElement
import com.solanteq.solar.plugin.ui.editor.FormEditor
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import javax.swing.*

class EditingPopupMenu(
    editor: FormEditor,
    private val formElement: FormLocalizableElement<*>
) : JPopupMenu() {

    private val editor = editor

    init {
        addRightElement()
        addLeftElement()
        editElement()
    }

    private fun addRightElement() {
        val action = ActionType.ADD_RIGHT_ELEMENT
        val menuItem = JMenuItem(action.toString())
        menuItem.addActionListener {
            showDialog(action)
        }
        add(menuItem)
    }

    private fun addLeftElement() {
        val action = ActionType.ADD_LEFT_ELEMENT
        val menuItem = JMenuItem(action.toString())
        menuItem.addActionListener {
            showDialog(action)
        }
        add(menuItem)
    }

    private fun editElement() {
        val menuItem = JMenuItem(ActionType.EDIT_ELEMENT.toString())
        menuItem.addActionListener {
            println("Выполнено Действие 1")
        }
        add(menuItem)
    }

    private fun showDialog(action: ActionType) {
        val formRootFile = this.editor.getRootFile() ?: return
        val dialog = FieldDataDialog(formRootFile, formElement, action)
        dialog.show()
    }
}
