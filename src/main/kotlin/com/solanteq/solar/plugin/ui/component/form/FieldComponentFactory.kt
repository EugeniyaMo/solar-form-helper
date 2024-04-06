package com.solanteq.solar.plugin.ui.component.form

import com.solanteq.solar.plugin.element.FormField
import com.solanteq.solar.plugin.ui.component.form.fields.CheckboxFieldComponent
import com.solanteq.solar.plugin.ui.component.form.fields.DropdownFieldComponent
import com.solanteq.solar.plugin.ui.component.form.fields.TextFieldComponent
import com.solanteq.solar.plugin.ui.editor.FormEditor

class FieldComponentFactory {

    fun createFieldComponent(editor: FormEditor, field: FormField): FieldComponent? {
        return when (field.type) {
            "STRING" -> TextFieldComponent(editor, field)
            "CHECKBOX" -> CheckboxFieldComponent(editor, field)
            "DROPDOWN" -> DropdownFieldComponent(editor, field)
            else -> {
                TextFieldComponent(editor, field)
            }
        }
    }
}