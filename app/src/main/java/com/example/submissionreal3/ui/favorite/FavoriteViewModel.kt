package com.example.submissionreal3.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submissionreal3.data.local.FavoriteUser
import com.example.submissionreal3.data.local.FavoriteUserDao
import com.example.submissionreal3.data.local.UserDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>?{
        return userDao?.getFavoriteUser()
    }

}