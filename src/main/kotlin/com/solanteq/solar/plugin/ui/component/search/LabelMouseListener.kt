package com.solanteq.solar.plugin.ui.component.search

import com.solanteq.solar.plugin.element.base.FormLocalizableElement
import com.solanteq.solar.plugin.ui.editor.FormEditor
import com.solanteq.solar.plugin.ui.editor.graphic.EditingPopupMenu
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel

class LabelMouseListener(
    private val editor: FormEditor,
    private val formElement: FormLocalizableElement<*>,
    private val labelComponent: JLabel
) : MouseAdapter() {

    override fun mouseClicked(event: MouseEvent) {
        // Handling left mouse button click
        if (event.button == MouseEvent.BUTTON1) {
            navigateToFormObject()
        }
        // Handling right mouse button click
        else if (event.button == MouseEvent.BUTTON3) {
            openEditingMenu(event)
        }
    }

    private fun navigateToFormObject() {
        val formGotoHandler = FormGoto(formElement)
        formGotoHandler.navigateToFormObject(formElement.project)
    }

    private fun openEditingMenu(event: MouseEvent) {
        val popupMenu = EditingPopupMenu(editor, formElement)
        popupMenu.show(labelComponent, event.x, event.y)
    }
}