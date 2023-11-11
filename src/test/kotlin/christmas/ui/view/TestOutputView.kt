package christmas.ui.view

class TestOutputView : OutputView {
    val capturedTexts = mutableListOf<String>()

    override fun displayMessage(message: String) {
        capturedTexts.add(message)
    }

    override fun displayErrorMessage(message: String?) {
        if (message == null) return
        capturedTexts.add(message)
    }
}