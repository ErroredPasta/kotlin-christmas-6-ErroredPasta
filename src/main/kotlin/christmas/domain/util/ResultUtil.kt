package christmas.domain.util

fun <T, R> T.runCatchingUntilSuccess(
    block: T.() -> R,
    onSuccess: (R) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    while (true) {
        runCatching(block = block).onSuccess {
            onSuccess(it)
            return
        }.onFailureOtherThanNoSuchElementException(action = onFailure)
    }
}

fun <T> Result<T>.onFailureOtherThanNoSuchElementException(action: (Throwable) -> Unit) = onFailure { error ->
    if (error is NoSuchElementException) throw error
    action(error)
}