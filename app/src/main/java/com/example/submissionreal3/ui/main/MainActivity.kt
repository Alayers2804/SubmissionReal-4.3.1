package com.example.submissionreal3.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionreal3.R
import com.example.submissionreal3.data.model.User
import com.example.submissionreal3.databinding.ActivityMainBinding
import com.example.submissionreal3.ui.detail.DetailActivity
import com.example.submissionreal3.ui.favorite.FavoriteActivity
import com.example.submissionreal3.ui.settings.DarkTheme
import com.example.submissionreal3.ui.settings.SettingPreferences
import com.example.submissionreal3.ui.settings.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)



        val pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory(pref)

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        adapter = UserAdapter()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailActivity::class.java).also{
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        binding.apply{
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

        }

        viewModel.getUsers().observe(this) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.setUsers("")

        showLoading(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setUsers(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also{
                    startActivity(it)
                }
            }
            R.id.toggletheme -> {
                Intent(this, DarkTheme::class.java).also{
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showLoading(state: Boolean)
    { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

}


