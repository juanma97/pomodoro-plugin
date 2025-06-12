package com.juanmaperez.pomodoro

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications
import javax.swing.JComponent

class PomodoroBannerProvider : EditorNotifications.Provider<JComponent>() {
    val settings = PomodoroSettings.getInstance().state
    
    companion object {
        private val KEY = Key.create<JComponent>("pomodoro.break.banner")
        var showBanner = false
    }

    override fun getKey(): Key<JComponent> = KEY

    override fun createNotificationPanel(
        file: VirtualFile,
        fileEditor: FileEditor,
        project: Project
    ): JComponent? {
        if (!showBanner) return null

        val panel = EditorNotificationPanel()
        if(settings.breakMinutes > 1){
            panel.text = "🍵 ¡Es hora del descanso! Tómate ${settings.breakMinutes} minutos para despejarte."
        }else{
            panel.text = "🍵 ¡Es hora del descanso! Tómate ${settings.breakMinutes} minuto para despejarte."
        }

        panel.createActionLabel("Aceptar") {
            showBanner = false
            EditorNotifications.getInstance(project).updateAllNotifications()
        }

        panel.createActionLabel("Ignorar") {
            showBanner = false
            EditorNotifications.getInstance(project).updateAllNotifications()
        }

        return panel
    }
}
