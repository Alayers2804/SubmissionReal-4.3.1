package com.example.submissionreal3.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionreal3.api.RetrofitClient
import com.example.submissionreal3.data.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setlistFollowing(username: String){
        RetrofitClient.apiInstance
            .getFollowing((username))
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful)
                        listFollowing.postValue(response.body())
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    t.message?.let { Log.d("failure", it) }
                }

            })
    }

    fun getlistFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }

}