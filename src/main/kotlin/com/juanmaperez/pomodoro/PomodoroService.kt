package com.juanmaperez.pomodoro

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.ui.EditorNotifications
import java.awt.Toolkit
import java.util.Timer
import java.util.TimerTask

class PomodoroService {

    private val listeners = mutableListOf<() -> Unit>()

    var pomodoroSeconds = PomodoroSettings.getInstance().state.pomodoroMinutes * 60
        private set
    var breakSeconds = PomodoroSettings.getInstance().state.breakMinutes * 60
        private set
    var timeLeft = pomodoroSeconds
        private set
    var running = false
        private set

    private var timer: Timer? = null
    private var isBreak = false

    fun start(project: Project) {
        if (running) return
        running = true
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (timeLeft > 0) {
                    timeLeft--
                    notifyListeners()
                } else {
                    Toolkit.getDefaultToolkit().beep()
                    PomodoroBannerProvider.showBanner = true
                    EditorNotifications.getInstance(project).updateAllNotifications()

                    val settings = PomodoroSettings.getInstance().state

                    if (!isBreak && settings.autoCycleEnabled) {
                        isBreak = true
                        timeLeft = breakSeconds
                        start(project)
                    } else if (!isBreak) {
                        timeLeft = pomodoroSeconds
                        start(project)
                    } else {
                        PomodoroBannerProvider.showBanner = false
                        EditorNotifications.getInstance(project).updateAllNotifications()
                        reset(settings.pomodoroMinutes, settings.breakMinutes)
                    }
                }
            }
        }, 0, 1000)

    }

    fun stop() {
        running = false
        timer?.cancel()
        notifyListeners()
    }

    fun reset(pomodoroMinutes: Int, breakMinutes: Int) {
        stop()
        pomodoroSeconds = pomodoroMinutes * 60
        breakSeconds = breakMinutes * 60
        timeLeft = pomodoroSeconds
        notifyListeners()
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }
    
    fun isBreak(): Boolean = isBreak

    private fun notifyListeners() {
        listeners.forEach { it.invoke() }
    }
    
    companion object {
        fun getInstance(): PomodoroService =
            ApplicationManager.getApplication().getService(PomodoroService::class.java)
    }
}
