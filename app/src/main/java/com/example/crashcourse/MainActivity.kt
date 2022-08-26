package com.example.crashcourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crashcourse.adapter.MovieAdapter
import com.example.crashcourse.api.ApiClient
import com.example.crashcourse.api.ApiService
import com.example.crashcourse.databinding.ActivityMainBinding
import com.example.crashcourse.response.MovieListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieAdapter by lazy { MovieAdapter() }
    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            progressBarMovie.visibility = View.VISIBLE

            val callMovieApi = api.getPopularListMovie(1)
            callMovieApi.enqueue(object : Callback<MovieListResponse>{
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    progressBarMovie.visibility = View.GONE
                    when(response.code()){
                        //Successful responses
                        in 200..299 ->{
                            response.body().let { itBody ->
                                itBody?.results.let {  itData ->
                                    if(itData!!.isNotEmpty()){
                                        movieAdapter.differ.submitList(itData)
                                        rvMovie.apply {
                                            layoutManager = LinearLayoutManager(this@MainActivity)
                                            adapter = movieAdapter
                                        }
                                    }
                                }
                            }
                        }
                        //Redirection messages
                        in 300..399->{
                            Log.i("Redirection message", "Redirection message")
                        }
                        // Client error responses
                        in 400..499->{
                            Log.i("Client error responses", "Client error responses")

                        }
                        // Server error responses
                        in 500..599->{
                            Log.i("Server error responses", "Server error responses")

                        }
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    binding.progressBarMovie.visibility = View.GONE
                    Log.i("FAILED", "FAILED message")


                }

            })
        }

    }
}