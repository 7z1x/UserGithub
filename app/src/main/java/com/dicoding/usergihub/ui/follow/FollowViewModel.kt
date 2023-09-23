package com.dicoding.usergihub.ui.follow

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.usergihub.data.response.ItemsItem
import com.dicoding.usergihub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _followList = MutableLiveData<List<ItemsItem>>()
    val followList: LiveData<List<ItemsItem>> = _followList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowList(username: String, tab: String) {
        _isLoading.value = true
            val client: Call<List<ItemsItem>> = if (tab == "follower") {
                ApiConfig.getApiService().getFollowerList(username)
            } else {
                ApiConfig.getApiService().getFollowingList(username)
            }
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _followList.value = response.body()
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

}