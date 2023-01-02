package com.nyorsi.p3track.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nyorsi.p3track.R
import com.nyorsi.p3track.models.*
import java.util.*

class ActivityDataAdapter (
    private var list: MutableList<ActivityModel>,
    private val clickListener: OnItemClickListener
): RecyclerView.Adapter<ActivityDataAdapter.DataViewHolder>() {
    private val departmentList: MutableList<DepartmentModel> = mutableListOf()
    private val taskList: MutableList<TaskModel> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class DataViewHolder(activityView: View): RecyclerView.ViewHolder(activityView), View.OnClickListener {
        val activityType: TextView = activityView.findViewById(R.id.activityType)
        val activityTypeIcon: ImageView = activityView.findViewById(R.id.actvityTypeIcon)
        val createdAt: TextView = activityView.findViewById(R.id.createdAt)
        val userName: TextView = activityView.findViewById(R.id.userName)
        val userProfile: ImageView = activityView.findViewById(R.id.userProfile)
        val activityShortDescription: TextView = activityView.findViewById(R.id.activityShortDesciption)
        val activityContent: LinearLayout = activityView.findViewById(R.id.activityContent)

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

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceAsColor")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = list[position]
        holder.activityType.text = currentItem.activityType.toString().lowercase()
        holder.activityType.text = holder.activityType.text.toString().replaceFirstChar { it.uppercase() }

        when (currentItem.activityType) {
            ActivityType.DEPARTMENT -> holder.activityTypeIcon.setImageResource(R.drawable.ic_department)
            ActivityType.TASK -> holder.activityTypeIcon.setImageResource(R.drawable.ic_task)
            ActivityType.ANNOUNCEMENT -> holder.activityTypeIcon.setImageResource(R.drawable.ic_announcement)
        }

        holder.userName.text = currentItem.createdByUser?.firstName + " " + currentItem.createdByUser?.lastName
        if(currentItem.createdByUser?.image != null) {
            Glide.with(holder.userProfile.context)
                .load(currentItem.createdByUser?.image)
                .into(holder.userProfile)
        } else {
            holder.userProfile.setImageResource(R.drawable.userprofile_placeholder)
        }

        // convert current time to human readable format
        val time = currentItem.createdTime
        val date = java.util.Date(time)
        val format = java.text.SimpleDateFormat("MMM dd")
        val formatted = format.format(date)
        holder.createdAt.text = formatted

        if(currentItem.activitySubType == ActivitySubType.DEPARTMENT_USER_ADDED) {
            Log.d("currentItem", currentItem.toString())
            val department = departmentList.find { it.id == currentItem.activityTypeSubId }
            if (department != null) {
                val text = currentItem.createdByUser?.firstName + " " + currentItem.createdByUser?.lastName +
                        " added " +
                        if(currentItem.assignedToId != null) {
                            currentItem.assignedToId?.firstName + " " + currentItem.assignedToId?.lastName
                        } else {
                            "Unknown User"
                        } +
                        " to " + "<b>" + department.name + " group" + "</b>."
                holder.activityShortDescription.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY);
                // remove activity content
                holder.activityContent.visibility = View.GONE
            }
        }

        if(currentItem.activitySubType == ActivitySubType.TASK_CREATED ||
            currentItem.activitySubType == ActivitySubType.TASK_ASSIGNED) {
            currentItem.activityTypeSubId = 4
            val task = taskList.find { it.id == currentItem.activityTypeSubId }
            if (task != null) {

                if (currentItem.activitySubType == ActivitySubType.TASK_CREATED) {
                    val text = currentItem.createdByUser?.firstName + " " + currentItem.createdByUser?.lastName +
                            " created a new task:"
                    holder.activityShortDescription.text = text
                } else {
                    val text = currentItem.createdByUser?.firstName + " " + currentItem.createdByUser?.lastName +
                            " assigned " +
                            if(currentItem.assignedToId != null) {
                                currentItem.assignedToId?.firstName + " " + currentItem.assignedToId?.lastName
                            } else {
                                "Unknown User"
                            } +
                            " to the task:"
                    holder.activityShortDescription.text = text
                }

                if(holder.activityContent.childCount == 0) {
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(16, 16, 0, 0)
                    holder.activityContent.setPadding(16, 16, 16, 16)

                    // set layout background color
                    holder.activityContent.setBackgroundColor("#EBF0FB".toColorInt())
                    // add textview to activity content
                    val taskTitle = TextView(holder.activityContent.context)
                    taskTitle.text = task.title + " for " + if (task.assignedTo != null) {
                        task.assignedTo?.firstName + " " + task.assignedTo?.lastName
                    } else {
                        "Unknown User"
                    }
                    // convert to bold text style
                    taskTitle.setTypeface(null, android.graphics.Typeface.BOLD)
                    taskTitle.layoutParams = params
                    taskTitle.textSize = 13f
                    taskTitle.setTextColor("#062029".toColorInt())
                    holder.activityContent.addView(taskTitle)

//                    val taskDescription = TextView(holder.activityContent.context)
//                    taskDescription.text = task.description
//                    taskDescription.textSize = 13f
//                    taskDescription.layoutParams = params
//                    taskDescription.setPadding(0, 0, 0, 20)
//                    holder.activityContent.addView(taskDescription)

                    val taskDeadline = TextView(holder.activityContent.context)
                    val deadline = Date(task.deadline * 1000)
                    val deadlineFormat = java.text.SimpleDateFormat("yyyy-MM-dd EEE HH:mm")
                    val deadlineFormatted = deadlineFormat.format(deadline)
                    taskDeadline.text = HtmlCompat.fromHtml("Deadline: <b>$deadlineFormatted</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                    taskDeadline.textSize = 13f
                    taskDeadline.layoutParams = params
                    taskDeadline.setTextColor("#5a7078".toColorInt())
                    holder.activityContent.addView(taskDeadline)

                    val createdBy = TextView(holder.activityContent.context)
                    createdBy.text = HtmlCompat.fromHtml("Created by: <b>" + currentItem.createdByUser?.firstName + " " + currentItem.createdByUser?.lastName + "</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                    createdBy.textSize = 13f
                    createdBy.layoutParams = params
                    createdBy.setTextColor("#5a7078".toColorInt())
                    holder.activityContent.addView(createdBy)

                    val asssignee = TextView(holder.activityContent.context)
                    asssignee.text = "Assignee: <b>" + if (task.assignedTo != null) {
                        task.assignedTo?.firstName + " " + task.assignedTo?.lastName + "</b>"
                    } else {
                        "<b> Unknown User </b>"
                    }
                    asssignee.text = HtmlCompat.fromHtml(asssignee.text.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
                    asssignee.textSize = 13f
                    asssignee.layoutParams = params
                    asssignee.setTextColor("#5a7078".toColorInt())
                    holder.activityContent.addView(asssignee)
                }
            }
        }

        // @TODO: add the other activity types
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: MutableList<ActivityModel>, newDepartmentList: MutableList<DepartmentModel>,
                newTaskList: MutableList<TaskModel>) {
        list = newData
        departmentList.clear()
        departmentList.addAll(newDepartmentList)

        taskList.clear()
        taskList.addAll(newTaskList)

        notifyDataSetChanged()
    }

}
