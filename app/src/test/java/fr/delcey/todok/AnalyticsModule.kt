package fr.delcey.todok

import android.app.Application
import android.app.PendingIntent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkContinuation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import androidx.work.WorkRequest
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.delcey.todok.data.AppDatabase
import fr.delcey.todok.data.DataModule
import fr.delcey.todok.data.dao.ProjectDao
import fr.delcey.todok.data.dao.TaskDao
import io.mockk.mockk
import java.util.*
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
class FakeAnalyticsModule {

    @Singleton
    @Provides
    fun provideWorkManager(): WorkManager = mockk(relaxed = true)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideAppDatabase(
        application: Application,
        workManager: WorkManager,
        gson: Gson,
    ): AppDatabase = AppDatabase.create(application, workManager, gson)

    @Singleton
    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao = appDatabase.getTaskDao()

    @Singleton
    @Provides
    fun provideProjectDao(appDatabase: AppDatabase): ProjectDao = appDatabase.getProjectDao()
}