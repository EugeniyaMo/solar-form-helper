package com.solanteq.solar.plugin.ui.editor.enums

enum class FieldType {
    STRING,
    PASSWORD,
    INTEGER,
    LONG,
    DOUBLE,
    MONEY,
    TEXT,
    DATE_TIME,
    DATE,
    // todo: ???
    DATE_RANGE,
    CHECKBOX,
    LABEL,
    CURRENCY,
    // todo: ???
    DROPDOWN,
    // todo: ???
    MULTISELECT,
    CODE,
    MARKDOWN,
    // todo: ???
    LINK,
    // todo: ???
    ACTION;

    companion object {
        fun getArrayValues(): Array<String> {
            return FieldType.values().map { it.name }.toTypedArray()
        }
    }
}