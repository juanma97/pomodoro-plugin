package com.juanmaperez.pomodoro

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "PomodoroSettings", storages = [Storage("pomodoro_settings.xml")])
class PomodoroSettings : PersistentStateComponent<PomodoroSettings.State> {

    data class State(
        var autoCycleEnabled: Boolean = false,
        var pomodoroMinutes: Int = 25,
        var breakMinutes: Int = 5
    )

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState = state
    }

    companion object {
        fun getInstance(): PomodoroSettings =
            com.intellij.openapi.application.ApplicationManager.getApplication()
                .getService(PomodoroSettings::class.java)
    }
}
