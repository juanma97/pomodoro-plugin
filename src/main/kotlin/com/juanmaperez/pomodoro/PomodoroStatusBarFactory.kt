import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class PomodoroStatusBarFactory : StatusBarWidgetFactory {
    override fun getId() = "PomodoroTimer"
    override fun getDisplayName() = "Pomodoro Timer"
    override fun isAvailable(project: Project) = true
    override fun createWidget(project: Project): StatusBarWidget = PomodoroWidget(project)
    override fun disposeWidget(widget: StatusBarWidget) {}
    override fun canBeEnabledOn(statusBar: StatusBar) = true
}