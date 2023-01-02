package com.nyorsi.p3track.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nyorsi.p3track.R
import com.nyorsi.p3track.models.DepartmentModel
import com.nyorsi.p3track.models.UserModel

class UserDataAdapter(
    private var list: MutableList<UserModel>,
    private val clickListener: OnItemClickListener
): RecyclerView.Adapter<UserDataAdapter.DataViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class DataViewHolder(activityView: View): RecyclerView.ViewHolder(activityView), View.OnClickListener {
        val userProfile: ImageView = activityView.findViewById(R.id.profilePictureUser)
        val userName: TextView = activityView.findViewById(R.id.userNameRV)
        val userRole: TextView = activityView.findViewById(R.id.userRoleRV)

        init {
            activityView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val currentPosition = this.adapterPosition
            clickListener.onItemClick(currentPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val groupView = LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        return DataViewHolder(groupView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = list[position]
        holder.userName.text = currentItem.firstName + " " + currentItem.lastName

        when(currentItem.type.ordinal) {
            0 -> holder.userRole.text = "HR Manager"
            1 -> holder.userRole.text = "Department Lead"
            2 -> holder.userRole.text = "Simple Employee"
        }

        if(currentItem.image != null) {
            Glide.with(holder.userProfile.context)
                .load(currentItem.image)
                .into(holder.userProfile)
        } else {
            Glide.with(holder.userProfile.context)
                .load(R.drawable.userprofile_placeholder)
                .into(holder.userProfile)
        }
    }

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): UserModel {
        return list[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newDepartmentList: MutableList<UserModel>) {
        list = newDepartmentList
        notifyDataSetChanged()
    }
}