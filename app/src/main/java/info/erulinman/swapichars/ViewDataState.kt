package info.erulinman.swapichars

sealed class ViewDataState<out T> {
    object Empty : ViewDataState<Nothing>()
    object Loading : ViewDataState<Nothing>()
    data class Loaded<out T>(val data: T) : ViewDataState<T>()
    data class Error(val message: String) : ViewDataState<Nothing>()
}