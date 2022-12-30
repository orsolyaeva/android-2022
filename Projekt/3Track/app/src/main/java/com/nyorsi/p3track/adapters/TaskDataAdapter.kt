package com.nyorsi.p3track.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface.BOLD
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.nyorsi.p3track.R
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.models.TaskPriority
import com.nyorsi.p3track.models.TaskStatus

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
        val priorityCircle: TextView = taskView.findViewById(R.id.priorityCircle)

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

        holder.projectDepartment.text = currentItem.department?.name + " project"
        holder.taskTitle.text = currentItem.title
        holder.taskTitle.setTypeface(null, BOLD)

        @SuppressLint("SimpleDateFormat") val timeFormat = java.text.SimpleDateFormat("hh:mm a")
        val createdTime = timeFormat.format(currentItem.createdTime * 1000)

        if (currentItem.createdBy != null) {
            val text =
                currentItem.createdBy?.firstName + " " + currentItem.createdBy?.lastName + " " + createdTime
            holder.taskCreatedBy.text = text
        } else {
            holder.taskCreatedBy.text = "N/A $createdTime"
        }

        if (currentItem.assignedTo != null) {
            val text = "Assignee: " + "<b>" + currentItem.assignedTo?.firstName +
                    " " + currentItem.assignedTo?.lastName + "</b>"
            holder.taskAssignee.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            holder.taskAssignee.text = HtmlCompat.fromHtml("Assignee: <b>No user assigned</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        holder.taskTag.setPadding(20, 10, 20, 10)
        holder.taskTag.setTextColor("#FFFFFF".toColorInt())

        val taskCardLayout = holder.itemView.findViewById<View>(R.id.taskCardLayout)

        when(currentItem.status) {
            TaskStatus.NEW -> {
                holder.taskTag.text = "New"
                taskCardLayout.setBackgroundColor("#f5edf7".toColorInt())
                holder.progressBar.progressTintList = "#dca0f2".toColorInt().let { android.content.res.ColorStateList.valueOf(it) }
                holder.taskTag.background.setTint("#dca0f2".toColorInt())
            }
           TaskStatus.IN_PROGRESS -> {
                holder.taskTag.text = "In progress"
                taskCardLayout.setBackgroundColor("#ffffff".toColorInt())
               holder.progressBar.progressTintList = "#88b6f2".toColorInt().let { android.content.res.ColorStateList.valueOf(it) }
                holder.taskTag.background.setTint("#88b6f2".toColorInt())
            }
            TaskStatus.BLOCKED -> {
                holder.taskTag.text = "Blocked"
                taskCardLayout.setBackgroundColor("#ffffff".toColorInt())
                holder.progressBar.progressTintList = "#fc7d77".toColorInt().let { android.content.res.ColorStateList.valueOf(it) }
                holder.taskTag.background.setTint("#fc7d77".toColorInt())
            }
            TaskStatus.DONE -> {
                holder.taskTag.text = "Done"
                taskCardLayout.setBackgroundColor("#ffffff".toColorInt())
                holder.progressBar.progressTintList = "#85e075".toColorInt().let { android.content.res.ColorStateList.valueOf(it) }
                holder.taskTag.background.setTint("#85e075".toColorInt())
            }
        }

        val deadline = currentItem.deadline
        if (deadline != 0L) {
            // format deadline to human readable as dd/mm/yyyy
            @SuppressLint("SimpleDateFormat") val dateFormat =
                java.text.SimpleDateFormat("MMM dd yyyy")
            val deadlineDate = dateFormat.format(deadline * 1000)
            val text = "Deadline: <b>$deadlineDate</b>"
            holder.taskDeadline.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            holder.taskDeadline.text = "N/A"
        }

        holder.taskPriority.text = currentItem.priority.toString().lowercase() + " priority"
        holder.taskPriority.text = holder.taskPriority.text.toString().replaceFirstChar { it.uppercase() }
        when(currentItem.priority) {
            TaskPriority.HIGH -> {
                holder.priorityCircle.background.setTint("#fc7d77".toColorInt())
            }
            TaskPriority.MEDIUM -> {
                holder.priorityCircle.background.setTint("#f2d088".toColorInt())
            }
            TaskPriority.LOW -> {
                holder.priorityCircle.background.setTint("#85e075".toColorInt())
            }
        }

        if (holder.taskDescription.text.length > 200) {
            holder.taskDescription.text = holder.taskDescription.text.substring(0, 300) + "..."
        } else {
            holder.taskDescription.text = currentItem.description
        }

        if(currentItem.progress != null) {
            holder.taskProgress.text = currentItem.progress.toString() + "% Done"
            holder.progressBar.progress = currentItem.progress!!
        } else {
            holder.taskProgress.text = "0% Done"
            holder.progressBar.progress = 0
        }
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: MutableList<TaskModel>) {
        list = newData
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): TaskModel {
        return list[position]
    }
}