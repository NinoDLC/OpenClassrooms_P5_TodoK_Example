package fr.delcey.todok.data

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.google.gson.Gson
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.domain.project.AddProjectUseCase
import fr.delcey.todok.getDefaultProjectEntitiesAsJson
import fr.delcey.todok.getDefaultProjectEntity
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InitializeDatabaseWorkerTest {

    companion object {
        private const val KEY_INPUT_DATA = "KEY_INPUT_DATA"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val context: Context = mockk()
    private val workerParams: WorkerParameters = mockk(relaxed = true)
    private val addProjectUseCase: AddProjectUseCase = mockk()
    private val gson: Gson = DataModule().provideGson()

    private val initializeDatabaseWorker = spyk(
        InitializeDatabaseWorker(
            context = context,
            workerParams = workerParams,
            addProjectUseCase = addProjectUseCase,
            gson = gson,
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider()
        )
    )

    @Before
    fun setUp() {
        every { initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA) } returns getDefaultProjectEntitiesAsJson()
        coJustRun { addProjectUseCase.invoke(any()) }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = initializeDatabaseWorker.doWork()

        // Then
        assertThat(result).isEqualTo(ListenableWorker.Result.success())
        coVerify(exactly = 1) {
            initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA)
            addProjectUseCase.invoke(getDefaultProjectEntity(0))
            addProjectUseCase.invoke(getDefaultProjectEntity(1))
            addProjectUseCase.invoke(getDefaultProjectEntity(2))
        }
        confirmVerified(addProjectUseCase)
    }

    @Test
    fun `error case - no input data`() = testCoroutineRule.runTest {
        // Given
        every { initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA) } returns null

        // When
        val result = initializeDatabaseWorker.doWork()

        // Then
        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
        confirmVerified(addProjectUseCase)
    }

    @Test
    fun `error case - can't parse empty input data`() = testCoroutineRule.runTest {
        // Given
        every { initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA) } returns ""

        // When
        val result = initializeDatabaseWorker.doWork()

        // Then
        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
        confirmVerified(addProjectUseCase)
    }

    @Test
    fun `error case - can't parse input data`() = testCoroutineRule.runTest {
        // Given
        every { initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA) } returns "some unparsable gibberish"

        // When
        val result = initializeDatabaseWorker.doWork()

        // Then
        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
        confirmVerified(addProjectUseCase)
    }
}