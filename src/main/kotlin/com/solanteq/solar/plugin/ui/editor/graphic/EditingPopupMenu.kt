package com.solanteq.solar.plugin.ui.editor.graphic

import com.solanteq.solar.plugin.element.base.FormLocalizableElement
import com.solanteq.solar.plugin.ui.editor.FormEditor
import com.solanteq.solar.plugin.ui.editor.FormUpdateDispatcher
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import javax.swing.*

class EditingPopupMenu(
    editor: FormEditor,
    private val formElement: FormLocalizableElement<*>
) : JPopupMenu() {

    private val editor = editor
    private val formUpdateDispatcher = FormUpdateDispatcher(editor)

    init {
        addRightElement()
        addLeftElement()
        editElement()
        removeElement()
    }

    private fun addRightElement() {
        val action = ActionType.ADD_RIGHT_ELEMENT
        createMenuItem(action)
    }

    private fun addLeftElement() {
        val action = ActionType.ADD_LEFT_ELEMENT
        createMenuItem(action)
    }

    private fun editElement() {
        val action = ActionType.EDIT_ELEMENT
        createMenuItem(action)
    }

    private fun removeElement() {
        val action = ActionType.REMOVE_ELEMENT
        val menuItem = JMenuItem(action.toString())
        val formRootFile = this.editor.getRootFile() ?: return
        menuItem.addActionListener {
            formUpdateDispatcher.removeField(formRootFile, formElement)
        }
        add(menuItem)
    }

    private fun createMenuItem(action: ActionType) {
        val menuItem = JMenuItem(action.toString())
        menuItem.addActionListener {
            showDialog(action)
        }
        add(menuItem)
    }

    private fun showDialog(action: ActionType) {
        val formRootFile = this.editor.getRootFile() ?: return
        val dialog = FieldDataDialog(editor, formRootFile, formElement, action)
        dialog.show()
    }
}
