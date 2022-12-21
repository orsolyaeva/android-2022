package com.nyorsi.p3track.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyorsi.p3track.R
import com.nyorsi.p3track.models.ActivityModel
import com.nyorsi.p3track.models.ActivityType

class DataAdapter (
    private var list: MutableList<ActivityModel>,
    private val clickListener: OnItemClickListener
): RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class DataViewHolder(activityView: View): RecyclerView.ViewHolder(activityView), View.OnClickListener {
        val activityType: TextView = activityView.findViewById(R.id.activityType)
        val activityTypeIcon: ImageView = activityView.findViewById(R.id.actvityTypeIcon)
        val userName: TextView = activityView.findViewById(R.id.userName)

        init {
            activityView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val currentPosition = this.adapterPosition
            clickListener.onItemClick(currentPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val activityView = LayoutInflater.from(parent.context).inflate(R.layout.activity_card, parent, false)
        return DataViewHolder(activityView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = list[position]
        holder.activityType.text = currentItem.activityType.toString()

        when (currentItem.activityType) {
            ActivityType.DEPARTMENT -> holder.activityTypeIcon.setImageResource(R.drawable.ic_department)
            ActivityType.TASK -> holder.activityTypeIcon.setImageResource(R.drawable.ic_task)
            ActivityType.ANNOUNCEMENT -> holder.activityTypeIcon.setImageResource(R.drawable.ic_announcement)
        }

        holder.userName.text = currentItem.createdByUser?.firstName + " " + currentItem.createdByUser?.lastName
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: MutableList<ActivityModel>) {
        list = newData
        notifyDataSetChanged()
    }
}
