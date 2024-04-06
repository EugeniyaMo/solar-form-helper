package com.solanteq.solar.plugin.ui.component.form

import com.solanteq.solar.plugin.element.FormField
import com.solanteq.solar.plugin.ui.component.form.base.ExpressionAwareComponent
import com.solanteq.solar.plugin.ui.component.util.Refreshable
import com.solanteq.solar.plugin.ui.editor.FormEditor

abstract class FieldComponent(
    editor: FormEditor,
    val field: FormField
) : ExpressionAwareComponent<FormField>(editor, field), Refreshable {

    abstract fun getFormField(): FormField
}