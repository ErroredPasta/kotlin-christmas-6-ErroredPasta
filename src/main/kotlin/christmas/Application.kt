package christmas

import christmas.di.EventPlannerComponent
import christmas.di.EventPlannerComponentImpl

fun main() {
    val component = createEventPlannerComponent()
    val view = component.getEventPlannerView()

    view.onStart()
}

fun createEventPlannerComponent(): EventPlannerComponent = EventPlannerComponentImpl()