package fr.delcey.todok.domain

import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Test

class CoroutineDispatcherProviderTest {
    @Test
    fun `verify coroutine dispatchers`() {
        // When
        val result = CoroutineDispatcherProvider()

        // Then
        assertEquals(Dispatchers.Main, result.main)
        assertEquals(Dispatchers.IO, result.io)
    }
}