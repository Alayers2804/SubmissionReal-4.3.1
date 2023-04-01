package com.example.submissionreal3.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submissionreal3.api.RetrofitClient
import com.example.submissionreal3.data.local.FavoriteUser
import com.example.submissionreal3.data.local.FavoriteUserDao
import com.example.submissionreal3.data.local.UserDatabase
import com.example.submissionreal3.data.model.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    val user = MutableLiveData<DetailUserResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }


    fun setUserDetail(username: String) {
        _isLoading.postValue(true)
        RetrofitClient.apiInstance
            .getDetail(username)
            .enqueue(object : retrofit2.Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                    _isLoading.postValue(false)
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    t.message?.let { Log.d("failure", it) }
                    _isLoading.postValue(false)
                }
            })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}