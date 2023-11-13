package christmas.ui.view

class TestInputView : InputView {
    private val inputs = mutableListOf<String>()

    fun setInputs(vararg inputs: String) {
        this.inputs.addAll(inputs)
    }

    private fun readLine(): String = inputs.removeFirst()

    private fun readInt(): Int = requireNotNull(readLine().toIntOrNull()) { INVALID_DATE_MESSAGE }

    override fun getDayOfMonth(): Int = readInt()

    override fun getMenusAndAmounts(): List<String> {
        // TODO: implement test view method
        return listOf("티본스테이크-1")
    }

    companion object {
        const val YEAR = 2023
        const val MONTH = 12

        const val INVALID_DATE_MESSAGE = InputView.INVALID_DATE_MESSAGE
    }
}