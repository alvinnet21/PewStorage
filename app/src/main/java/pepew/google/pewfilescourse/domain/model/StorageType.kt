package pepew.google.pewfilescourse.domain.model

sealed class StorageType {

    data object Internal: StorageType()

    data object Shared: StorageType()

}
