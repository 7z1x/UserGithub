package com.dicoding.usergihub.data.retrofit

import com.dicoding.usergihub.data.response.DetailUserResponse
import com.dicoding.usergihub.data.response.ItemsItem
import com.dicoding.usergihub.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUser( @Query("q") username: String)
    : Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String)
    : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowerList(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingList(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}

