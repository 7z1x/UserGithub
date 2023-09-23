package com.dicoding.usergihub.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.usergihub.data.response.ItemsItem
import com.dicoding.usergihub.databinding.ItemRowUserBinding

class UserAdapter(private val followList: List<ItemsItem>) : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class MyViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.tvItemName.text = user.login
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imgItemAvatar)

            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(user)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: ItemsItem)
    }
}
