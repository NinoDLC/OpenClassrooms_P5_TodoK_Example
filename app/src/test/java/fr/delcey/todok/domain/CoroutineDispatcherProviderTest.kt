package fr.delcey.todok.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.Dispatchers
import org.junit.Test

class CoroutineDispatcherProviderTest {
    @Test
    fun `verify coroutine dispatchers`() {
        // When
        val result = CoroutineDispatcherProvider()

        // Then
        assertThat(result.main).isEqualTo(Dispatchers.Main)
        assertThat(result.io).isEqualTo(Dispatchers.IO)
    }
}