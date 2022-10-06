package fr.delcey.todok.domain.navigate

import fr.delcey.todok.data.navigation.NavigationRepository
import fr.delcey.todok.domain.navigate.model.DestinationEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigateUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository,
) {
    suspend fun invoke(destination: DestinationEntity) {
        navigationRepository.navigate(destination)
    }
}