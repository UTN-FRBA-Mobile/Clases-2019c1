package ar.edu.utn.frba.mobile.a2019c1

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class TweetsResponse(
    val tweets: List<Tweet>
)

data class Tweet(
    val profilePic: String,
    val name: String,
    val certified: Boolean,
    val username: String,
    val content: String,
    val image: String?,
    val commentCount: Int,
    val retweetCount: Int,
    val likeCount: Int
)

interface Api {

    @GET("list")
    fun getTweets(): Call<TweetsResponse>

    companion object {
        fun create(): Api =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()) // Para parsear automáticamente el json
                .baseUrl("https://demo0682762.mockable.io/")
                .build()
                .create(Api::class.java)
    }

}