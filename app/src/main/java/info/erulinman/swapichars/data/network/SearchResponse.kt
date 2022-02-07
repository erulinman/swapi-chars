package info.erulinman.swapichars.data.network

data class SearchResponse(
    val count: Int,
    val next: String?,
    val previous: Any,
    val results: List<CharacterApiEntity>
)