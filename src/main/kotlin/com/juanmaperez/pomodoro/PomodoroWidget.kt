import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget.MultipleTextValuesPresentation
import com.intellij.util.Consumer
import com.juanmaperez.pomodoro.PomodoroService
import java.awt.event.MouseEvent

class PomodoroWidget(private val project: Project) : StatusBarWidget, MultipleTextValuesPresentation {

    private var statusBar: StatusBar? = null
    private val service = PomodoroService.getInstance()

    override fun ID() = "PomodoroWidget"

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
        service.addListener {
            ApplicationManager.getApplication().invokeLater {
                statusBar.updateWidget(ID())
            }
        }
    }

    override fun dispose() {}

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this
    override fun getTooltipText() = "🍅 Pomodoro Timer"
    override fun getSelectedValue(): String =
        "🍅 ${if (service.running) "Clic para detener -> " else "Clic para iniciar -> "}${formatTime(service.timeLeft)}"

    override fun getPopupStep(): ListPopup? {
        val actions = mutableListOf<String>()

        if (service.running) {
            actions.add("Detener")
            actions.add("Reiniciar")
        } else {
            actions.add("Iniciar")
        }

        val step = object : BaseListPopupStep<String>("Pomodoro", actions) {
            override fun onChosen(selectedValue: String?, finalChoice: Boolean): PopupStep<*>? {
                when (selectedValue) {
                    "Iniciar" -> service.start(project)
                    "Detener" -> service.stop()
                    "Reiniciar" -> service.reset(
                        service.pomodoroSeconds / 60,
                        service.breakSeconds / 60
                    )
                }
                return PopupStep.FINAL_CHOICE
            }

            override fun isSpeedSearchEnabled(): Boolean = false
        }

        return JBPopupFactory.getInstance().createListPopup(step)
    }


    override fun getClickConsumer(): Consumer<MouseEvent>? = null

    private fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }
}
