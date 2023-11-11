package christmas.ui.view

class ConsoleOutputView : OutputView {
    override fun displayMessage(message: String) {
        println(message)
    }

    override fun displayErrorMessage(message: String?) {
        println("${OutputView.ERROR_MESSAGE_PREFIX} ${message ?: OutputView.DEFAULT_ERROR_MESSAGE}")
    }
}