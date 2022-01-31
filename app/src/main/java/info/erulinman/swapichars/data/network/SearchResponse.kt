package info.erulinman.swapichars.data.network

import info.erulinman.swapichars.data.entity.Character

data class SearchResponse(
    val count: Int,
    val next: String?,
    val previous: Any,
    val results: List<Character>
)