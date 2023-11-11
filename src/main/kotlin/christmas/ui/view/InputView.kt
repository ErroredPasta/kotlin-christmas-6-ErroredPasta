package christmas.ui.view

import java.time.LocalDate

interface InputView {
    fun getDate(): LocalDate

    companion object {
        const val YEAR = 2023
        const val MONTH = 12

        const val ASK_VISIT_DATE_MESSAGE = "12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)"
        const val INVALID_DATE_MESSAGE = "유효하지 않은 날짜입니다. 다시 입력해 주세요."
    }
}