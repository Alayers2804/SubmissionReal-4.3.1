package com.example.submissionreal3.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionreal3.data.model.User
import com.example.submissionreal3.databinding.ItemUserBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val list = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<User>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){

            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClicked(user)
            }

            binding.apply {
            Glide.with(itemView)
                .load(user.avatar_url)
                .centerCrop()
                .into(binding.imgItemPhoto)
            tvItemName.text = user.login
        }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: User)
    }

}