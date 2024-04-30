package com.solanteq.solar.plugin.ui.editor.graphic


import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.solanteq.solar.plugin.element.FormRootFile
import com.solanteq.solar.plugin.element.base.FormLocalizableElement
import com.solanteq.solar.plugin.ui.component.form.data.FormFieldData
import com.solanteq.solar.plugin.ui.editor.UpdateFormService
import com.solanteq.solar.plugin.ui.editor.enums.ActionType
import com.solanteq.solar.plugin.ui.editor.enums.FormType
import java.awt.BorderLayout
import javax.swing.*

class FieldDataDialog(
    form: FormRootFile,
    private val formElement: FormLocalizableElement<*>,
    action: ActionType
) : DialogWrapper(true) {

    private val nameField = JBTextField()
    private val fieldSizeField = JBTextField()
    private val labelSizeField = JBTextField()
    private val typeField = JComboBox<String>(FormType.getArrayValues())

    private val updateFormService = UpdateFormService()
    private val formRootFile: FormRootFile
    private val actionType: ActionType

    init {
        title = "Add Field"
        formRootFile = form
        actionType = action
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.add(createFieldRow("Name:", nameField))
        panel.add(createFieldRow("Field Size:", fieldSizeField))
        panel.add(createFieldRow("Label Size:", labelSizeField))
        panel.add(createFieldRow("Type:", typeField))

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
        val fieldFormData = FormFieldData(
            nameField.text,
            fieldSizeField.text,
            labelSizeField.text,
            typeField.selectedItem.toString()
        )

        val exitingPsiElement = formElement.sourceElement.originalElement
        val filePath = formRootFile.virtualFile.toString()
        updateFormService.addField(actionType, fieldFormData, exitingPsiElement, filePath)

        super.doOKAction()
    }
}