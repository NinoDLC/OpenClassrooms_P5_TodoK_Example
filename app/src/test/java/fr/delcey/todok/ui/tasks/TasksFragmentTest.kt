package fr.delcey.todok.ui.tasks

import android.content.Context
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertThat
import assertk.assertions.contains
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.delcey.todok.R
import fr.delcey.todok.RecyclerViewItemAssertion
import fr.delcey.todok.domain.project.InsertProjectUseCase
import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.task.InsertTaskUseCase
import fr.delcey.todok.domain.task.TaskEntity
import fr.delcey.todok.launchFragmentInHiltContainer
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TasksFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var insertProjectUseCase: InsertProjectUseCase

    @Inject
    lateinit var insertTaskUseCase: InsertTaskUseCase

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()

        runBlocking { insertProjectUseCase.invoke(ProjectEntity(name = "Project 1", colorInt = Color.RED)) }
        runBlocking { insertProjectUseCase.invoke(ProjectEntity(name = "Project 2", colorInt = Color.GREEN)) }
        runBlocking { insertProjectUseCase.invoke(ProjectEntity(name = "Project 3", colorInt = Color.BLUE)) }

        runBlocking { insertTaskUseCase.invoke(TaskEntity(projectId = 1, description = "Task for project 1")) }
        runBlocking { insertTaskUseCase.invoke(TaskEntity(projectId = 2, description = "Task for project 2")) }
        runBlocking { insertTaskUseCase.invoke(TaskEntity(projectId = 3, description = "Task for project 3")) }
    }

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `verify context`() {
        assertThat(context.packageName).contains("fr.delcey.todok")
    }

    @Test
    fun `nominal case - 3 tasks`() {
        launchFragmentInHiltContainer<TasksFragment>()

        onView(withId(R.id.tasks_RecyclerView)).check(
            RecyclerViewItemAssertion(0, R.id.task_item_TextView_description, withText("Task for project 0"))
        )
        onView(withId(R.id.tasks_RecyclerView)).check(
            RecyclerViewItemAssertion(1, R.id.task_item_TextView_description, withText("Task for project 1"))
        )
        onView(withId(R.id.tasks_RecyclerView)).check(
            RecyclerViewItemAssertion(2, R.id.task_item_TextView_description, withText("Task for project 2"))
        )
    }
}