package com.solanteq.solar.plugin.l10n

import com.solanteq.solar.plugin.base.*
import com.solanteq.solar.plugin.l10n.group.L10nGroupDeclarationProvider
import org.junit.jupiter.api.Test

class L10nGroupTest : LightPluginTestBase() {

    override fun getTestDataSuffix() = "l10n"

    @Test
    fun `test l10n reference to group`() {
        fixture.configureByForms("testForm1.json", module = "test")

        L10nTestUtils.createL10nFileAndConfigure(fixture, "l10n",
            "test.form.testForm1.<caret>group1.randomText" to "Group Name!"
        )

        assertReferencedSymbolNameEquals("group1")
    }

    @Test
    fun `test l10n group completion`() {
        fixture.configureByForms("testForm1.json", module = "test")

        L10nTestUtils.createL10nFileAndConfigure(fixture, "l10n",
            "test.form.testForm1.<caret>" to "Group Name!"
        )

        assertCompletionsContainsExact("group1", "group2")
    }

    @Test
    fun `test l10n group reference rename`() {
        val formFile = fixture.createForm("testForm", """
            {
              "groups": [
                {
                  "name": "group1"
                }
              ]
            }
        """.trimIndent(), "test")

        L10nTestUtils.createL10nFileAndConfigure(fixture, "l10n",
            "test.form.testForm.<caret>group1" to "Group Name!"
        )

        val expectedFormText = """
            {
              "groups": [
                {
                  "name": "renamed"
                }
              ]
            }
        """.trimIndent()

        renameFormSymbolReference("renamed")
        assertJsonStringLiteralValueEquals("test.form.testForm.renamed")
        fixture.openFileInEditor(formFile.virtualFile)
        fixture.checkResult(expectedFormText)
    }

    @Test
    fun `test l10n group declaration rename`() {
        fixture.createFormAndConfigure("testForm", """
            {
              "groups": [
                {
                  "name": "<caret>group1"
                }
              ]
            }
        """.trimIndent(), "test")

        val l10nFile = L10nTestUtils.createL10nFile(fixture, "l10n",
            "test.form.testForm.group1" to "Group Name!"
        )

        val expectedL10nText = L10nTestUtils.generateL10nFileText(
            "test.form.testForm.renamed" to "Group Name!"
        )

        renameFormSymbolDeclaration(L10nGroupDeclarationProvider(), "renamed")
        assertJsonStringLiteralValueEquals("renamed")

        fixture.openFileInEditor(l10nFile.virtualFile)
        fixture.checkResult(expectedL10nText)
    }

    @Test
    fun `test l10n reference to group in included form`() {
        fixture.createForm("rootForm", """
            {
              "groups": "json://includes/forms/test/includedForm.json"
            }
        """.trimIndent(), "test")

        fixture.createIncludedForm("includedForm", "test", """
            [
              {
                "name": "groupName"
              }
            ]
        """.trimIndent())

        L10nTestUtils.createL10nFileAndConfigure(fixture, "l10n",
            "test.form.rootForm.<caret>groupName" to "Group name"
        )

        assertReferencedSymbolNameEquals("groupName")
    }

}