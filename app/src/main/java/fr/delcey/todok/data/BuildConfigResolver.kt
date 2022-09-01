package fr.delcey.todok.data

import fr.delcey.todok.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildConfigResolver @Inject constructor() {
    val isDebug: Boolean
        get() = BuildConfig.DEBUG
}