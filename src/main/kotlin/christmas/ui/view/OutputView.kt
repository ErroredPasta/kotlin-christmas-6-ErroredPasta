package christmas.ui.view

interface OutputView {
    fun displayMessage(message: String)
    fun displayErrorMessage(message: String?)

    companion object {
        const val WELCOME_MESSAGE = "안녕하세요! 우테코 식당 12월 이벤트 플래너입니다."
        const val ERROR_MESSAGE_PREFIX = "[ERROR]"
        const val DEFAULT_ERROR_MESSAGE = "에러가 발생했습니다. 다시 시도해 주세요."
    }
}