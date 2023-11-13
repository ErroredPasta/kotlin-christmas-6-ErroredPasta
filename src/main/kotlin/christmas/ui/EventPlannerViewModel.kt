package christmas.ui

import christmas.domain.logic.hasDuplicatedMenus
import christmas.domain.model.Menu
import christmas.domain.util.onFailureOtherThanNoSuchElementException
import java.time.LocalDate
import kotlin.properties.Delegates

typealias Callback = (UiState) -> Unit

class EventPlannerViewModel {
    private lateinit var date: LocalDate
    private lateinit var menusAndAmounts: List<Pair<Menu, Int>>

    private var callback: Callback? = null

    private var previousState: UiState = UiState.Initialized
        set(value) {
            if (value is UiState.Error) return // Error state로 되돌일 일은 없으므로 저장하지 않는다
            field = value
        }

    var uiState: UiState by Delegates.observable(UiState.Initialized) { _, oldValue, newValue ->
        previousState = oldValue
        callback?.invoke(newValue)
    }
        private set

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun rewindToPreviousState() {
        uiState = previousState
    }

    fun setDate(date: LocalDate) {
        this.date = date
        uiState = UiState.GetDateDone
    }

    fun setMenusAndAmounts(menusAndAmounts: List<Pair<Menu, Int>>) {
        runCatching {
            require(!menusAndAmounts.hasDuplicatedMenus()) { DUPLICATED_MENU_MESSAGE }
        }.onSuccess {
            this.menusAndAmounts = menusAndAmounts
            uiState = UiState.GetMenusAndAmountsDone
        }.onFailureOtherThanNoSuchElementException { error ->
            uiState = UiState.Error(error = error)
        }
    }

    companion object {
        const val DUPLICATED_MENU_MESSAGE = "유효하지 않은 주문입니다. 다시 입력해 주세요."
    }
}