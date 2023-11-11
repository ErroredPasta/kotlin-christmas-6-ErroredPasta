package christmas.di

import christmas.ui.view.*

class EventPlannerComponentImpl : EventPlannerComponent {
    private fun provideInputView(): InputView = ConsoleInputView()
    private fun provideOutputView(): OutputView = ConsoleOutputView()

    override fun getEventPlannerView(): EventPlannerView = EventPlannerView(
        inputView = provideInputView(),
        outputView = provideOutputView()
    )
}