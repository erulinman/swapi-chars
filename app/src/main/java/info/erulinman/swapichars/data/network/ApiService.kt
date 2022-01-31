package info.erulinman.swapichars.data.network

import info.erulinman.swapichars.data.entity.Character
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/people?format=json")
    suspend fun getCharacters(): SearchResponse

    @GET("/api/people?format=json")
    suspend fun getCharacters(@Query("search") search: String): SearchResponse

    @GET("/api/people?format=json")
    suspend fun getCharacters(@Query("page") page: Int): SearchResponse

    @GET("/api/people/{id}")
    suspend fun getCharacterDetails(@Path("id") id: Int): Character
}