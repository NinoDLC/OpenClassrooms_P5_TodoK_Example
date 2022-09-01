package fr.delcey.todok.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

class KotlinUtilsKtTest {
    @Test
    fun `exhaustive can be used on nullable type`() {
        // When
        val result = when ("a") {
            "a" -> null
            else -> 2
        }.exhaustive

        // Then
        assertThat(result).isEqualTo(Unit)
    }

    @Test
    fun `exhaustive returns unit as an expression`() {
        // When
        val result = when ("a") {
            "a" -> 1
            else -> 2
        }.exhaustive

        // Then
        assertThat(result).isEqualTo(Unit)
    }
}