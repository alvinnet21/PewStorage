package pepew.google.pewfilescourse.presentation

import pepew.google.pewfilescourse.domain.model.InternalStoragePhoto

data class InternalStorageListState(
    val isLoading: Boolean = false,
    val files: List<InternalStoragePhoto> = emptyList(),
    val error: String = ""
)
