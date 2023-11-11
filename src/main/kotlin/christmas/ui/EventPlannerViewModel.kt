package christmas.ui

import java.time.LocalDate
import kotlin.properties.Delegates

typealias Callback = (UiState) -> Unit

class EventPlannerViewModel {
    private lateinit var date: LocalDate

    private var callback: Callback? = null
    var uiState by Delegates.observable(UiState.Initialized) { _, _, newValue -> callback?.invoke(newValue) }
        private set

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setDate(date: LocalDate) {
        this.date = date
    }
}