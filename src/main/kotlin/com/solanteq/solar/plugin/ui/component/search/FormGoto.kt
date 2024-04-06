package com.solanteq.solar.plugin.ui.component.search

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.solanteq.solar.plugin.element.base.FormLocalizableElement

class FormGoto(private val formElement: FormLocalizableElement<*>) {

    fun navigateToFormObject(project: Project) {
        if (DumbService.isDumb(project)) {
            DumbService.getInstance(project).showDumbModeNotification(
                CodeInsightBundle.message("message.navigation.is.not.available.here.during.index.update")
            )
        }
        val element = formElement.sourceElement
        element.navigate(true)
    }

}