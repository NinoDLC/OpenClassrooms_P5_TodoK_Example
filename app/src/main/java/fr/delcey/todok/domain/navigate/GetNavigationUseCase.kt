package fr.delcey.todok.domain.navigate

import fr.delcey.todok.data.navigation.NavigationRepository
import fr.delcey.todok.domain.navigate.model.DestinationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNavigationUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository,
) {
    fun invoke(): Flow<DestinationEntity> = navigationRepository.destinationFlow
}