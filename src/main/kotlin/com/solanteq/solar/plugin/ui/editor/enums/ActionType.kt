package com.solanteq.solar.plugin.ui.editor.enums

enum class ActionType(
    private val localization: String
) {
    ADD_RIGHT_ELEMENT("Добавить элемент справа"),
    ADD_LEFT_ELEMENT("Добавить элемент слева"),
    EDIT_ELEMENT("Редактировать элемент"),
    REMOVE_ELEMENT("Удалить элемент");

    override fun toString(): String {
        return localization
    }
}