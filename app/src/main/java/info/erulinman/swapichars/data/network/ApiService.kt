package info.erulinman.swapichars.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("/api/people?format=json")
    suspend fun getCharacters(): SearchResponse

    @GET("/api/people?format=json")
    suspend fun getCharacters(@Query("search") search: String): SearchResponse

    @GET("/api/people?format=json")
    suspend fun getCharacters(@Query("page") page: Int): SearchResponse

    @GET
    suspend fun getMovieByUrl(@Url url: String): SearchResponse
}