package fr.delcey.todok.data

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import fr.delcey.todok.data.project.ProjectDao
import fr.delcey.todok.data.project.model.ProjectDto
import fr.delcey.todok.data.task.TaskDao
import fr.delcey.todok.data.task.model.TaskDto

@Database(
    entities = [
        TaskDto::class,
        ProjectDto::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao
    abstract fun getProjectDao(): ProjectDao

    companion object {
        private const val DATABASE_NAME = "TodoK_database"

        fun create(
            application: Application,
            workManager: WorkManager,
            gson: Gson,
        ): AppDatabase {
            val builder = Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DATABASE_NAME
            )

            builder.addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val dtosAsJson = gson.toJson(
                        listOf(
                            ProjectDto(
                                name = application.getString(R.string.tartampion_project),
                                colorInt = ResourcesCompat.getColor(application.resources, R.color.project_color_tartampion, null)
                            ),
                            ProjectDto(
                                name = application.getString(R.string.lucidia_project),
                                colorInt = ResourcesCompat.getColor(application.resources, R.color.project_color_lucidia, null)
                            ),
                            ProjectDto(
                                name = application.getString(R.string.circus_project),
                                colorInt = ResourcesCompat.getColor(application.resources, R.color.project_color_circus, null)
                            ),
                        )
                    )

                    workManager.enqueue(
                        OneTimeWorkRequestBuilder<InitializeDatabaseWorker>()
                            .setInputData(workDataOf(InitializeDatabaseWorker.KEY_INPUT_DATA to dtosAsJson))
                            .build()
                    )

                }
            })

            if (BuildConfig.DEBUG) {
                builder.fallbackToDestructiveMigration()
            }

            return builder.build()
        }
    }
}