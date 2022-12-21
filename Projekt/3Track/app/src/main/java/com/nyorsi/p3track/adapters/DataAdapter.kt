package com.nyorsi.p3track.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyorsi.p3track.R
import com.nyorsi.p3track.models.ActivityModel

class DataAdapter (
    private var list: MutableList<ActivityModel>,
    private val clickListener: OnItemClickListener
): RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class DataViewHolder(activityView: View): RecyclerView.ViewHolder(activityView), View.OnClickListener {
        val actionText = activityView.findViewById<TextView>(R.id.actionText)

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

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = list[position]
        holder.actionText.text = currentItem.description
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: MutableList<ActivityModel>) {
        list = newData
        notifyDataSetChanged()
    }
}
