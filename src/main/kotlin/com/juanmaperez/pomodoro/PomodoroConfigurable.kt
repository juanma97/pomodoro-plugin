package com.juanmaperez.pomodoro

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class PomodoroConfigurable : Configurable {

    private val pomodoroField = JBTextField(5)
    private val breakField = JBTextField(5)
    private val autoCycleCheckbox = JCheckBox("Activar modo auto-ciclo")


    override fun createComponent(): JComponent? {
        reset()

        val formPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Pomodoro Minutes:", pomodoroField)
            .addLabeledComponent("Break Minutes:", breakField)
            .addComponent(autoCycleCheckbox)
            .panel

        val wrapper = JPanel(BorderLayout())
        wrapper.add(formPanel, BorderLayout.NORTH)

        return wrapper
    }

    override fun isModified(): Boolean {
        val settings = PomodoroSettings.getInstance().state
        return pomodoroField.text != settings.pomodoroMinutes.toString() ||
                breakField.text != settings.breakMinutes.toString() || 
                autoCycleCheckbox.isSelected != settings.autoCycleEnabled
    }

    override fun apply() {
        val settings = PomodoroSettings.getInstance().state
        settings.pomodoroMinutes = pomodoroField.text.toIntOrNull() ?: 25
        settings.breakMinutes = breakField.text.toIntOrNull() ?: 5

        PomodoroService.getInstance().reset(settings.pomodoroMinutes, settings.breakMinutes)
        settings.autoCycleEnabled = autoCycleCheckbox.isSelected
    }

    override fun reset() {
        val settings = PomodoroSettings.getInstance().state
        pomodoroField.text = settings.pomodoroMinutes.toString()
        breakField.text = settings.breakMinutes.toString()
        autoCycleCheckbox.isSelected = settings.autoCycleEnabled
    }

    override fun getDisplayName(): String = "Pomodoro Settings"
}
