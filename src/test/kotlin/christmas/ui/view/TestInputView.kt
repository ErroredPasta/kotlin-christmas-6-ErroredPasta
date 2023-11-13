package christmas.ui.view

class TestInputView : InputView {
    private val inputs = mutableListOf<String>()

    fun setInputs(vararg inputs: String) {
        this.inputs.addAll(inputs)
    }

    private fun readLine(): String = inputs.removeFirst()

    private fun readInt(): Int = requireNotNull(readLine().toIntOrNull()) { INVALID_DATE_MESSAGE }

    override fun getDayOfMonth(): Int = readInt()

    override fun getMenusAndAmounts(): List<String> = readLine().split(InputView.MENU_DELIMITER)

    companion object {
        const val INVALID_DATE_MESSAGE = InputView.INVALID_DATE_MESSAGE
    }
}