package com.solanteq.solar.plugin.ui.editor.data

data class FormFieldData(
    val visibleWhen: String? = null,
    val name: String,
    val fieldSize: Int,
    val labelSize: Int,
    val type: String,
    val form: String? = null
)