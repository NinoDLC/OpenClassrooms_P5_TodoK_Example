package fr.delcey.todok.domain.navigate.model

sealed class DestinationEntity {

    sealed class Activity : DestinationEntity() {
        data class Detail(val taskId: Long) : Activity()
    }

    sealed class Fragment : DestinationEntity() {
        object Tasks : Fragment()
    }

    sealed class DialogFragment : DestinationEntity() {
        object AddTask : DialogFragment()
    }

    object Back : DestinationEntity()
    object Dismiss: DestinationEntity()
    object FinishActivity : DestinationEntity()
}