package com.solanteq.solar.plugin.ui.editor.graphic

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.solanteq.solar.plugin.element.FormField
import com.solanteq.solar.plugin.element.FormRootFile
import com.solanteq.solar.plugin.element.base.FormLocalizableElement
import com.solanteq.solar.plugin.ui.editor.data.FormFieldData
import com.solanteq.solar.plugin.ui.editor.FormEditor
import com.solanteq.solar.plugin.ui.editor.FormUpdateService
import com.solanteq.solar.plugin.ui.editor.ValidationService
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import com.solanteq.solar.plugin.ui.editor.enums.FieldType
import org.jetbrains.kotlin.utils.addToStdlib.UnsafeCastFunction
import org.jetbrains.kotlin.utils.addToStdlib.cast
import java.awt.BorderLayout
import javax.swing.*

@OptIn(UnsafeCastFunction::class)
class FieldDataDialog(
    editor: FormEditor,
    form: FormRootFile,
    private val formElement: FormLocalizableElement<*>,
    action: ActionType
) : DialogWrapper(true) {

    private var nameField = JBTextField()
    private var fieldSizeField = JBTextField()
    private var labelSizeField = JBTextField()
    private var typeField = JComboBox<String>(FieldType.getArrayValues())
    private var visibleWhen = JBTextField()

    private val formUpdateService = FormUpdateService(editor)
    private val validationService = ValidationService()
    private val formRootFile: FormRootFile
    private val actionType: ActionType

    init {
        title = "Add Field"
        formRootFile = form
        actionType = action

        if (actionType == ActionType.EDIT_ELEMENT) {
            title = "Edit Field"
            val formField = formElement.cast<FormField>()
            nameField = JBTextField(formField.name)
            if (formField.fieldSize != null) {
                fieldSizeField = JBTextField(formField.fieldSize!!.toString())
            }
            if (formField.labelSize != null) {
                labelSizeField = JBTextField(formField.labelSize!!.toString())
            }
        }


        init()
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.add(createFieldRow("Name:", nameField))
        panel.add(createFieldRow("Field Size:", fieldSizeField))
        panel.add(createFieldRow("Label Size:", labelSizeField))
        panel.add(createFieldRow("Type:", typeField))
        panel.add(createFieldRow("Visible when:", visibleWhen))

        return panel
    }

    private fun createFieldRow(labelText: String, component: JComponent): JPanel {
        val rowPanel = JPanel()
        rowPanel.layout = BorderLayout()

        val label = JLabel(labelText)
        val textFieldPanel = JPanel(BorderLayout())
        textFieldPanel.add(component)

        rowPanel.add(label, BorderLayout.WEST)
        rowPanel.add(textFieldPanel, BorderLayout.CENTER)

        return rowPanel
    }

    override fun doOKAction() {
        check()
        val fieldFormData = FormFieldData(
            visibleWhen.text,
            nameField.text,
            fieldSizeField.text.toInt(),
            labelSizeField.text.toInt(),
            typeField.selectedItem.toString()
        )

        val exitingPsiElement = formElement.sourceElement.originalElement
        val filePath = formRootFile.virtualFile.toString()
        if (actionType == ActionType.EDIT_ELEMENT) {
            formUpdateService.editField(fieldFormData, exitingPsiElement, filePath)
        }
        else {
            formUpdateService.addField(actionType, fieldFormData, exitingPsiElement, filePath)
        }
        super.doOKAction()
    }

    private fun check() {
        if (nameField.text == "") {
            val notification = Notification("Error Notification",
                "Ошибка валидации",
                "Некорректные параметры для формы: поле name не может быть пустым",
                NotificationType.ERROR)
            Notifications.Bus.notify(notification)
            return
        }
    }
}