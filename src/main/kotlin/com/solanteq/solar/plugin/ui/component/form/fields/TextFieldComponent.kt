package com.solanteq.solar.plugin.ui.component.form.fields

import com.intellij.ui.util.preferredHeight
import com.solanteq.solar.plugin.element.FormField
import com.solanteq.solar.plugin.ui.FormColorScheme
import com.solanteq.solar.plugin.ui.component.form.FieldComponent
import com.solanteq.solar.plugin.ui.component.form.RowComponent
import com.solanteq.solar.plugin.ui.component.util.UniversalBorder
import com.solanteq.solar.plugin.ui.editor.FormEditor
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JTextField

class TextFieldComponent(
    editor: FormEditor,
    field: FormField
) : FieldComponent(editor, field) {

    private val testField = JTextField()

    init {
        layout = GridBagLayout()
        preferredSize = Dimension(0, RowComponent.ROW_HEIGHT)
        val fieldConstraints = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            anchor = GridBagConstraints.CENTER
            weightx = 1.0
            weighty = 1.0
        }
        testField.preferredHeight = FIELD_HEIGHT
        testField.border = UniversalBorder.builder()
            .radius(4)
            .color(FormColorScheme.BORDER_COLOR)
            .build()
        add(testField, fieldConstraints)
    }

    override fun refresh() {
        //do nothing for now
    }

    override fun getFormField(): FormField {
        return field
    }

    companion object {

        const val DEFAULT_FIELD_SIZE = 4
        const val FIELD_HEIGHT = 25

    }

}