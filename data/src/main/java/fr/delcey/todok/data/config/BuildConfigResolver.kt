package fr.delcey.todok.data.config

import fr.delcey.todok.data.BuildConfig
import fr.delcey.todok.domain.config.ConfigResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildConfigResolver @Inject constructor() : ConfigResolver {
    override val isDebug: Boolean
        get() = BuildConfig.DEBUG
}