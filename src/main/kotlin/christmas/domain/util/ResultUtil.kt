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
        }.onFailure { error ->
            if (error is NoSuchElementException) throw error
            onFailure(error)
        }
    }
}