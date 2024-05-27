package com.solanteq.solar.plugin.ui.component.form.fields

import com.intellij.ui.util.preferredHeight
import com.solanteq.solar.plugin.element.FormField
import com.solanteq.solar.plugin.ui.FormColorScheme
import com.solanteq.solar.plugin.ui.component.form.construction.RowComponent
import com.solanteq.solar.plugin.ui.component.util.UniversalBorder
import com.solanteq.solar.plugin.ui.editor.FormEditor
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JCheckBox

class CheckboxFieldComponent(
    editor: FormEditor,
    field: FormField
) : FieldComponent(editor, field) {

    private val checkboxField = JCheckBox()

    init {
        layout = GridBagLayout()
        preferredSize = Dimension(0, RowComponent.ROW_HEIGHT)
        val fieldConstraints = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            anchor = GridBagConstraints.CENTER
            weightx = 1.0
            weighty = 1.0
        }
        checkboxField.preferredHeight = FIELD_HEIGHT
        checkboxField.border = UniversalBorder.builder()
            .radius(4)
            .color(FormColorScheme.BORDER_COLOR)
            .build()
        add(checkboxField, fieldConstraints)
    }

    override fun getFormField(): FormField {
        return field
    }

    override fun refresh() {

    }

    companion object {
        const val FIELD_HEIGHT = 25
    }

}