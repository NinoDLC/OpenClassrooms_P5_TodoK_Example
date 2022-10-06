package fr.delcey.todok.ui

import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

// TODO NINO "BaseActivities" aren't cool with hilt fragment testing... need to try that
open class BaseActivity : AppCompatActivity() {
    @Inject
    protected lateinit var navigator: Navigator
}