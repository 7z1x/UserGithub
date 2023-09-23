package com.dicoding.usergihub.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.usergihub.data.response.ItemsItem
import com.dicoding.usergihub.databinding.ActivityMainBinding
import com.dicoding.usergihub.ui.UserAdapter
import com.dicoding.usergihub.ui.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mainViewModel.userI.observe(this) { items ->
            setUserData(items)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        performSearch(searchView.text.toString())
                        searchView.hide()
                        return@setOnEditorActionListener true
                    }
                    false
                }
        }
    }

    private fun setUserData(userData: List<ItemsItem>) {
        val adapter = UserAdapter(emptyList())
        adapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(user: ItemsItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("avatar_url", user.avatarUrl)
                intent.putExtra("username", user.login)
                startActivity(intent)
            }
        })
        binding.rvUser.adapter = adapter
        adapter.submitList(userData)
    }


    private fun showLoading(isLoading: Boolean) {
        binding.rvUser.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun performSearch(query: String) {
        mainViewModel.searchUser(query)
    }
}
