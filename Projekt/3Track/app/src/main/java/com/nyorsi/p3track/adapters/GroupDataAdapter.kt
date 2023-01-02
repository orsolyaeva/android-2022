package com.nyorsi.p3track.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyorsi.p3track.R
import com.nyorsi.p3track.models.DepartmentModel
import com.nyorsi.p3track.ui.groups.MyGroupsFragment

class GroupDataAdapter(
    private var list: MutableList<DepartmentModel>,
    private val clickListener: OnItemClickListener
): RecyclerView.Adapter<GroupDataAdapter.DataViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class DataViewHolder(activityView: View): RecyclerView.ViewHolder(activityView), View.OnClickListener {
        val departmentName: TextView = activityView.findViewById(R.id.departmentName)
        val viewMembersButton: Button = activityView.findViewById(R.id.viewMembersButton)

        init {
            activityView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val currentPosition = this.adapterPosition
            clickListener.onItemClick(currentPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val groupView = LayoutInflater.from(parent.context).inflate(R.layout.group_card, parent, false)
        return DataViewHolder(groupView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = list[position]
        holder.departmentName.text = currentItem.name

        holder.viewMembersButton.setOnClickListener {
            clickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int): DepartmentModel {
        return list[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newDepartmentList: MutableList<DepartmentModel>) {
        list = newDepartmentList
        notifyDataSetChanged()
    }


}