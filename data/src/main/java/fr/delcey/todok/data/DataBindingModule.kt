package fr.delcey.todok.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.delcey.todok.data.config.BuildConfigResolver
import fr.delcey.todok.data.project.ProjectRepositoryRoom
import fr.delcey.todok.data.task.TaskRepositoryRoom
import fr.delcey.todok.domain.config.ConfigResolver
import fr.delcey.todok.domain.project.ProjectRepository
import fr.delcey.todok.domain.task.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingModule {

    @Singleton
    @Binds
    abstract fun bindProjectRepository(impl: ProjectRepositoryRoom): ProjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryRoom): TaskRepository

    @Singleton
    @Binds
    abstract fun bindConfigResolver(impl: BuildConfigResolver): ConfigResolver
}