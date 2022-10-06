package fr.delcey.todok.data.navigation

import fr.delcey.todok.domain.navigate.model.DestinationEntity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationRepository @Inject constructor() {

    private val destinationChannel = Channel<DestinationEntity>()
    val destinationFlow: Flow<DestinationEntity> = destinationChannel.receiveAsFlow()

    suspend fun navigate(destination: DestinationEntity) {
        destinationChannel.send(destination)
    }
}