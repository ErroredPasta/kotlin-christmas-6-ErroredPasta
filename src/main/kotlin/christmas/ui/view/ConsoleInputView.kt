package christmas.ui.view

import camp.nextstep.edu.missionutils.Console
import java.time.DateTimeException
import java.time.LocalDate

class ConsoleInputView : InputView {
    override fun getDate(): LocalDate {
        println(InputView.ASK_VISIT_DATE_MESSAGE)

        return runCatching {
            LocalDate.of(InputView.YEAR, InputView.MONTH, readInt())
        }.onFailure { error ->
            if (error is DateTimeException) throw IllegalArgumentException(InputView.INVALID_DATE_MESSAGE)
        }.getOrThrow()
    }

    private fun readInt(): Int = requireNotNull(Console.readLine().toIntOrNull()) { InputView.INVALID_DATE_MESSAGE }
}