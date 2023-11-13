package fr.delcey.todok.data

import io.mockk.MockKMatcherScope

inline fun <reified T : Any> MockKMatcherScope.anyListOf(item: T): List<T> = match { param ->
    param.isNotEmpty() && param.all { it == item }
}