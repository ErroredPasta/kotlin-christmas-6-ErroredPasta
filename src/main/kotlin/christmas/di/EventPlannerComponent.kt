package christmas.di

import christmas.ui.view.EventPlannerView

interface EventPlannerComponent {
    fun getEventPlannerView(): EventPlannerView
}