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
import fr.delcey.todok.BuildConfig
import fr.delcey.todok.R
import fr.delcey.todok.data.project.ProjectDao
import fr.delcey.todok.data.task.TaskDao
import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.task.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        ProjectEntity::class,
    ],
    version = 1,
    exportSchema = false,
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
                    val entitiesAsJson = gson.toJson(
                        listOf(
                            ProjectEntity(
                                name = application.getString(R.string.tartampion_project),
                                colorInt = ResourcesCompat.getColor(application.resources, R.color.project_color_tartampion, null)
                            ),
                            ProjectEntity(
                                name = application.getString(R.string.lucidia_project),
                                colorInt = ResourcesCompat.getColor(application.resources, R.color.project_color_lucidia, null)
                            ),
                            ProjectEntity(
                                name = application.getString(R.string.circus_project),
                                colorInt = ResourcesCompat.getColor(application.resources, R.color.project_color_circus, null)
                            ),
                        )
                    )

                    workManager.enqueue(
                        OneTimeWorkRequestBuilder<InitializeDatabaseWorker>()
                            .setInputData(workDataOf(InitializeDatabaseWorker.KEY_INPUT_DATA to entitiesAsJson))
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