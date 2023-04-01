package com.example.submissionreal3.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.submissionreal3.api.RetrofitClient
import com.example.submissionreal3.data.model.User
import com.example.submissionreal3.data.model.UserResponse
import com.example.submissionreal3.ui.settings.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(private val pref: SettingPreferences): ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setUsers(query: String = "q") {
        _isLoading.postValue(true)
        RetrofitClient.apiInstance
            .getUsers(query)
            .enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                    }
                    _isLoading.postValue(false)
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                    _isLoading.postValue(false)
                }
            })
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        setUsers()
        return listUsers
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

}
