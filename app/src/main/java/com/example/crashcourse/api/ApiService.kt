package com.example.crashcourse.api

import com.example.crashcourse.response.DetailesMovieResponse
import com.example.crashcourse.response.MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun getPopularListMovie(@Query("page") page: Int) : Call<MovieListResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetailes(@Path("movie_id") id: Int) : Call<DetailesMovieResponse>
}