package christmas.ui.view

import java.time.DateTimeException
import java.time.LocalDate

class TestInputView : InputView {
    private val inputs = mutableListOf<String>()

    fun setInputs(vararg inputs: String) {
        this.inputs.addAll(inputs)
    }

    private fun readLine(): String = inputs.removeFirst()

    private fun readInt(): Int = requireNotNull(readLine().toIntOrNull()) { INVALID_DATE_MESSAGE }

    override fun getDate(): LocalDate = runCatching {
        LocalDate.of(YEAR, MONTH, readInt())
    }.onFailure { error ->
        if (error is DateTimeException) throw IllegalArgumentException(INVALID_DATE_MESSAGE)
    }.getOrThrow()

    companion object {
        const val YEAR = 2023
        const val MONTH = 12

        const val INVALID_DATE_MESSAGE = InputView.INVALID_DATE_MESSAGE
    }
}