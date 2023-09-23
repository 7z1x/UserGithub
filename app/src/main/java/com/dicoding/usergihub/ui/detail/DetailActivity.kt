package com.dicoding.usergihub.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.usergihub.R
import com.dicoding.usergihub.databinding.ActivityDetailBinding
import com.dicoding.usergihub.ui.follow.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var username: String
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val avatarUrl = intent.getStringExtra("avatar_url")
        username = intent.getStringExtra("username") ?: "" // DISINI AKU LAKUKAN PERUBAHAN

        detailViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[DetailViewModel::class.java]

        setupUserProfileAndObserve(username, avatarUrl)

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setViewPager()

    }

    private fun setupUserProfileAndObserve(username: String, avatarUrl: String?) {
        Glide.with(this)
            .load(avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.imageView)

        binding.textViewNama.text = username

        detailViewModel.findDetailUser(username)

        detailViewModel.profileU.observe(this) { userDetail ->
            val followers = userDetail?.followers.toString()
            val following = userDetail?.following.toString()

            val followersText = getString(R.string.followers, followers)
            val followingText = getString(R.string.following, following)

            binding.textViewFollowers.text = followersText
            binding.textViewFollowing.text = followingText

            val realName = userDetail?.name
            if (realName != null) {
                binding.textViewRealName.text = realName
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setViewPager() {
        val adapter = SectionsPagerAdapter(this, username)
        binding.viewPager.adapter = adapter

        binding.viewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabs1, binding.viewPager) { tab, position ->
            val tabText = this.getString(TAB_TITLES[position])
            tab.text = tabText
        }.attach()
    }

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
