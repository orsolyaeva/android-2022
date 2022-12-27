package com.nyorsi.p3track.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface.BOLD
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.nyorsi.p3track.R
import com.nyorsi.p3track.models.TaskModel

class TaskDataAdapter(
    private var list: MutableList<TaskModel>,
    private val clickListener: OnItemClickListener
): RecyclerView.Adapter<TaskDataAdapter.DataViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class DataViewHolder(taskView: View): RecyclerView.ViewHolder(taskView), View.OnClickListener {
        val projectDepartment: TextView = taskView.findViewById(R.id.projectDepartment)
        val taskTitle: TextView = taskView.findViewById(R.id.taskTitle)
        val taskCreatedBy: TextView = taskView.findViewById(R.id.taskCreatedBy)
        val taskAssignee: TextView = taskView.findViewById(R.id.taskAssignee)
        val taskTag: TextView = taskView.findViewById(R.id.taskTag)
        val taskDeadline: TextView = taskView.findViewById(R.id.taskDeadline)
        val taskPriority: TextView = taskView.findViewById(R.id.taskPriority)
        val taskDescription: TextView = taskView.findViewById(R.id.taskDescription)
        val taskProgress: TextView = taskView.findViewById(R.id.taskProgress)
        val progressBar: ProgressBar = taskView.findViewById(R.id.progressBar)

        init {
            taskView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val currentPosition = this.adapterPosition
            clickListener.onItemClick(currentPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val taskView = LayoutInflater.from(parent.context).inflate(R.layout.task_card, parent, false)
        return DataViewHolder(taskView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        Log.d("TaskDataAdapter", "onBindViewHolder: $position")
        val currentItem = list[position]

        holder.projectDepartment.text = currentItem.department?.name
        holder.taskTitle.text = currentItem.title
        holder.taskTitle.setTypeface(null, BOLD)

        if (currentItem.createdBy != null) {
            // format created_time to human readable as hh:mm pm/am
            @SuppressLint("SimpleDateFormat") val timeFormat = java.text.SimpleDateFormat("hh:mm a")
            val createdTime = timeFormat.format(currentItem.createdTime)
            val text =
                currentItem.createdBy?.firstName + " " + currentItem.createdBy?.lastName + " " + createdTime
            holder.taskCreatedBy.text = text
        } else {
            holder.taskCreatedBy.text = "N/A"
        }

        if (currentItem.assignedTo != null) {
            val text = "Assignee: " + "<b>" + currentItem.assignedTo?.firstName +
                    " " + currentItem.assignedTo?.lastName + "</b>"
            holder.taskAssignee.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            holder.taskCreatedBy.text = "N/A"
        }

        holder.taskTag.text = currentItem.status.toString()

        val deadline = currentItem.deadline
        if (deadline != 0L) {
            // format deadline to human readable as dd/mm/yyyy
            @SuppressLint("SimpleDateFormat") val dateFormat =
                java.text.SimpleDateFormat("MMM dd yyyy")
            val deadlineDate = dateFormat.format(deadline)
            val text = "Deadline: <b>$deadlineDate</b>"
            holder.taskDeadline.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            holder.taskDeadline.text = "N/A"
        }

        holder.taskPriority.text = currentItem.priority.toString()
        holder.taskDescription.text = currentItem.description

        if(currentItem.progress != null) {
            holder.taskProgress.text = currentItem.progress.toString() + "%"
            holder.progressBar.progress = currentItem.progress!!
        } else {
            holder.taskProgress.text = "N/A"
            holder.progressBar.progress = 0
        }
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: MutableList<TaskModel>) {
        list = newData
        notifyDataSetChanged()
    }
}