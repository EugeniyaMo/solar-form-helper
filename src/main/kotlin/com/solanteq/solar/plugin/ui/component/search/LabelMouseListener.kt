package com.solanteq.solar.plugin.ui.component.search

import com.solanteq.solar.plugin.element.base.FormLocalizableElement
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class LabelMouseListener(private val formElement: FormLocalizableElement<*>) : MouseAdapter() {
    override fun mouseClicked(event: MouseEvent) {
        val formGotoHandler = FormGoto(formElement)
        formGotoHandler.navigateToFormObject(formElement.project)
    }
}