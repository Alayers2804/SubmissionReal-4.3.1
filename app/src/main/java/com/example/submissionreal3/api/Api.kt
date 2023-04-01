package com.example.submissionreal3.api

import com.example.submissionreal3.data.model.DetailUserResponse
import com.example.submissionreal3.data.model.User
import com.example.submissionreal3.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("search/users")
    @Headers("Authorization: put your here")
    fun getUsers(@Query("q") query: String): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: put your here")
    fun getDetail(@Path ("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: put your here")
    fun getFollowers(@Path ("username") username: String): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: put your here")
    fun getFollowing(@Path ("username") username: String): Call<ArrayList<User>>
}