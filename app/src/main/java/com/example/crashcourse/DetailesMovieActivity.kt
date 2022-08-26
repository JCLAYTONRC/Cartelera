package com.example.crashcourse

import android.icu.number.Scale
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.crashcourse.api.ApiClient
import com.example.crashcourse.api.ApiService
import com.example.crashcourse.databinding.ActivityDetailesMovieBinding
import com.example.crashcourse.response.DetailesMovieResponse
import com.example.crashcourse.utils.Constants.POSTER_BASEURL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailesMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailesMovieBinding
    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailesMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("id",1)

        binding.apply {
            val callDetailesMovieApi = api.getMovieDetailes(movieId)
            callDetailesMovieApi.enqueue(object : Callback<DetailesMovieResponse>{
                override fun onResponse(
                    call: Call<DetailesMovieResponse>,
                    response: Response<DetailesMovieResponse>
                ) {
                    when(response.code()){
                        in 200..299 ->{
                           response.body().let { itBody ->
                               val imagePoster = POSTER_BASEURL + itBody!!.poster_path
                               imgMovie.load(imagePoster){
                                   crossfade(true)
                                   placeholder(R.drawable.poster_placeholder)
                                   scale(coil.size.Scale.FILL)
                               }
                               imgBackground.load(imagePoster){
                                   crossfade(true)
                                   placeholder(R.drawable.poster_placeholder)
                                   scale(coil.size.Scale.FILL)
                               }

                               with(itBody){
                                   tvMovieName.text = title
                                   tvTagline.text = tagline
                                   tvMovieDateReleased.text = release_date
                                   tvRating.text = vote_average.toString()
                                   tvRuntime.text = runtime.toString()
                                   tvBudget.text = budget.toString()
                                   tvRevenue.text = revenue.toString()
                                   tvOverview.text = overview
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

                override fun onFailure(call: Call<DetailesMovieResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}