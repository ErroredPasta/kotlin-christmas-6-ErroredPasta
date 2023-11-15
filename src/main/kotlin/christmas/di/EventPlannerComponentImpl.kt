package christmas.di

import christmas.domain.logic.DiscountCalculator
import christmas.domain.logic.MenuValidator
import christmas.ui.EventPlannerViewModel
import christmas.ui.view.*

class EventPlannerComponentImpl : EventPlannerComponent {
    private fun provideInputView(): InputView = ConsoleInputView()
    private fun provideOutputView(): OutputView = ConsoleOutputView()

    private fun provideMenuValidator(): MenuValidator = MenuValidator
    private fun provideDiscountCalculator(): DiscountCalculator = DiscountCalculator

    private fun provideEventPlannerViewModel(): EventPlannerViewModel =
        EventPlannerViewModel(menuValidator = provideMenuValidator(), discountCalculator = provideDiscountCalculator())

    override fun getEventPlannerView(): EventPlannerView = EventPlannerView(
        inputView = provideInputView(),
        outputView = provideOutputView(),
        viewModel = provideEventPlannerViewModel()
    )
}