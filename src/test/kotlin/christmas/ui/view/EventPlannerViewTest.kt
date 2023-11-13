package christmas.ui.view

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class EventPlannerViewTest {
    private lateinit var view: EventPlannerView
    private lateinit var inputView: TestInputView
    private lateinit var outputView: TestOutputView

    @BeforeEach
    fun setUp() {
        inputView = TestInputView()
        outputView = TestOutputView()

        view = EventPlannerView(inputView = inputView, outputView = outputView)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 25, 31])
    @DisplayName("방문 날짜 입력 시 올바르게 입력할 경우 유효하지 않은 날짜라고 출력하지 않음")
    fun start_validInput_notDisplayInvalidDateMessage(dayOfMonth: Int) {
        // given
        inputView.setInputs(dayOfMonth.toString())

        // when
        view.start()

        // then
        assertThat(outputView.capturedTexts).doesNotContain(TestInputView.INVALID_DATE_MESSAGE)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1, 32])
    @DisplayName("방문 날짜 입력 시 1 ~ 31사이의 값이 아닌 경우 유효하지 않은 날짜라고 출력")
    fun start_dayOfMonthIsOutOfValidRange_displayInvalidDateMessage(dayOfMonth: Int) {
        // given
        inputView.setInputs(dayOfMonth.toString(), "1") // 올바른 입력이 들어올때 까지 반복하므로 마지막에 올바른 입력을 추가

        // when
        view.start()

        // then
        assertThat(outputView.capturedTexts).contains(TestInputView.INVALID_DATE_MESSAGE)
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "", "one"])
    @DisplayName("방문 날짜 입력 시 숫자 외의 입력을 한 경우 유효하지 않은 날짜라고 출력")
    fun start_nonNumberInput_displayInvalidDateMessage(input: String) {
        // given
        inputView.setInputs(input, "1") // 올바른 입력이 들어올때 까지 반복하므로 마지막에 올바른 입력을 추가

        // when
        view.start()

        // then
        assertThat(outputView.capturedTexts).contains(TestInputView.INVALID_DATE_MESSAGE)
    }
}