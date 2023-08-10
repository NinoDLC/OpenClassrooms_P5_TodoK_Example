package fr.delcey.todok.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.delcey.todok.data.project.RoomProjectRepository
import fr.delcey.todok.data.task.RoomTaskRepository
import fr.delcey.todok.domain.project.ProjectRepository
import fr.delcey.todok.domain.task.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingModule {

    @Singleton
    @Binds
    abstract fun bindProjectRepository(impl: RoomProjectRepository): ProjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(impl: RoomTaskRepository): TaskRepository
}