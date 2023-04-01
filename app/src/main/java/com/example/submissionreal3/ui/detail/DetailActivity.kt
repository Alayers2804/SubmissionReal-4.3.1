package com.example.submissionreal3.ui.detail


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submissionreal3.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)


        if (username != null) {
            viewModel.setUserDetail(username)
        }



        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = "${it.followers} Followers"
                    tvFollowing.text = "${it.following} Following"
                    Glide.with(this@DetailActivity)
                        .load(it.avatar_url)
                        .into(detailImgview)
                }
                showLoading(false)
            }
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null) {
                    if (count > 0) {
                        binding.togglefavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.togglefavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.togglefavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                if (username != null) {
                    if (avatarUrl != null) {
                        viewModel.addToFavorite(username, id, avatarUrl)
                    }
                }
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.togglefavorite.isChecked = _isChecked
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            if (viewPager != null){
                TabLayoutMediator(tabs, viewPager) { tab, position ->
                    tab.text = resources.getString(sectionPagerAdapter.getTabTitle(position))
                }.attach()
            }
        }
    }
    private fun showLoading(state: Boolean)
    { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

    companion object {
        const val EXTRA_USERNAME ="extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }

}


