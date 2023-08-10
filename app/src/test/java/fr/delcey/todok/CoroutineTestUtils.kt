package fr.delcey.todok

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.Call
import io.mockk.MockKAdditionalAnswerScope
import io.mockk.MockKAnswerScope
import io.mockk.MockKStubScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.coroutineContext

fun <T> MockKStubScope<Unit, T>.ensuresDispatcher(
    coroutineDispatcher: CoroutineDispatcher
): MockKAdditionalAnswerScope<Unit, T> = ensuresDispatcher(coroutineDispatcher) {}

fun <T, B> MockKStubScope<T, B>.ensuresDispatcher(
    coroutineDispatcher: CoroutineDispatcher,
    answersBlock: suspend MockKAnswerScope<T, B>.(Call) -> T,
): MockKAdditionalAnswerScope<T, B> = coAnswers {
    assertThat(coroutineDispatcher).isEqualTo(coroutineContext[CoroutineDispatcher])
    answersBlock(call)
}

fun <T> emptyFlowOnDispatcher(coroutineDispatcher: CoroutineDispatcher): Flow<T> = emptyFlow<T>().onStart {
    assertThat(coroutineContext[CoroutineDispatcher]).isEqualTo(coroutineDispatcher)
}