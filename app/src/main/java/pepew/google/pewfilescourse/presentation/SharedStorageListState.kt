package pepew.google.pewfilescourse.presentation

import pepew.google.pewfilescourse.domain.model.SharedStoragePhoto

data class SharedStorageListState(
    val isLoading: Boolean = false,
    val files: List<SharedStoragePhoto> = emptyList(),
    val error: String = ""
)
