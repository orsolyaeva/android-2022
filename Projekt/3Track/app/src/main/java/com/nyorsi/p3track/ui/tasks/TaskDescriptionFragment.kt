package com.nyorsi.p3track.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.core.text.HtmlCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nyorsi.p3track.R
import com.nyorsi.p3track.databinding.FragmentTaskDescriptionBinding
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.models.TaskPriority
import com.nyorsi.p3track.models.TaskStatus
import com.nyorsi.p3track.viewModels.GlobalViewModel


class TaskDescriptionFragment : Fragment() {
    private var currentItem: TaskModel? = null
    private var _binding: FragmentTaskDescriptionBinding? = null
    private val binding get() = _binding!!
    private val globalViewModel: GlobalViewModel by activityViewModels()

    private lateinit var taskTitle: TextView
    private lateinit var taskDepartment: TextView
    private lateinit var taskAssignedBy: TextView
    private lateinit var taskAssignedDate: TextView
    private lateinit var taskAssignee: TextView
    private lateinit var taskProgress: TextView
    private lateinit var taskProgressBar: ProgressBar
    private lateinit var taskTagSpinner: Spinner
    private lateinit var taskDeadline: TextView
    private lateinit var taskPriorityCircle: TextView
    private lateinit var taskPriority: TextView
    private lateinit var taskDescription: TextView

    companion object {
        const val TAG = "TaskDescriptionFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.findItem(R.id.add_new_task).isVisible = false
                menu.findItem(R.id.edit_task).isVisible = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.edit_task -> {
                        arguments?.clear()
                        if(currentItem != null) {
                            val parsedValue = Gson().toJson(currentItem!!, object: TypeToken<TaskModel>() {}.type)
                            findNavController().navigate(R.id.editTaskFragment, Bundle().apply {
                                putString("taskToUpdate", parsedValue)
                            })
                        } else {
                            val parsedValueTemp = arguments?.getString("currentTask")
                            currentItem = Gson().fromJson(parsedValueTemp, object : TypeToken<TaskModel>() {}.type)

                            Log.d(TAG, "onMenuItemSelected: $currentItem")
                            val parsedValue = Gson().toJson(currentItem!!, object: TypeToken<TaskModel>() {}.type)
                            findNavController().navigate(R.id.editTaskFragment, Bundle().apply {
                                putString("taskToUpdate", parsedValue)
                            })
                        }
                        return true
                    }
                }
                return false
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDescriptionBinding.inflate(inflater, container, false)
        initViews()

        val parsedValue = arguments?.getString("currentTask")
        currentItem = Gson().fromJson(parsedValue, object : TypeToken<TaskModel>() {}.type)

        taskTitle.text = currentItem?.title
        taskTitle.setTypeface(null, Typeface.BOLD)
        taskDepartment.text = currentItem?.department?.name + " project"

        @SuppressLint("SimpleDateFormat") val timeFormat = java.text.SimpleDateFormat("hh:mm a")
        val createdTime = timeFormat.format((currentItem?.createdTime ?: 0) * 1000)
        if(currentItem?.createdBy != null) {
            val text =
                "Assigned by: <b>" + currentItem?.createdBy?.firstName + " " + currentItem?.createdBy?.lastName + " " + createdTime + "</b>"
            taskAssignedBy.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            taskAssignedBy.text = "Not assigned yet $createdTime"
        }

        @SuppressLint("SimpleDateFormat") val dateFormat = java.text.SimpleDateFormat("hh:mm a MMM dd yyyy")
        val createdDate = dateFormat.format((currentItem?.createdTime ?: 0))
        val text = "Assigned on: <b>$createdDate</b>"
        taskAssignedDate.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)

        if (currentItem?.assignedTo != null) {
            val text = "Assignee: " + "<b>" + currentItem?.assignedTo?.firstName +
                    " " + currentItem?.assignedTo?.lastName + "</b>"
            taskAssignee.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            taskAssignee.text = "Assignee: N/A"
        }

        // ad task status names as spinner options
        val statusNames = TaskStatus.values().map { it.name.lowercase() }
        val adapter = context?.let { ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            statusNames
        ) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskTagSpinner.adapter = adapter

        // get current task status and set spinner to that
        val currentStatus = currentItem?.status?.name?.lowercase()
        val spinnerPosition = adapter?.getPosition(currentStatus)
        taskTagSpinner.setSelection(spinnerPosition ?: 0)

        when(currentItem?.status) {
            TaskStatus.NEW -> {
                taskProgressBar.progressTintList = "#dca0f2".toColorInt().let { android.content.res.ColorStateList.valueOf(it) }
            }
            TaskStatus.IN_PROGRESS -> {
                taskProgressBar.progressTintList = "#88b6f2".toColorInt().let { android.content.res.ColorStateList.valueOf(it) }
            }
            TaskStatus.BLOCKED -> {
                taskProgressBar.progressTintList = "#fc7d77".toColorInt().let { android.content.res.ColorStateList.valueOf(it) }
            }
            TaskStatus.DONE -> {
                taskProgressBar.progressTintList = "#85e075".toColorInt().let { android.content.res.ColorStateList.valueOf(it) }
            }
            else -> {}
        }

        val deadline = currentItem?.deadline
        if (deadline != 0L) {
            // format deadline to human readable as dd/mm/yyyy
            @SuppressLint("SimpleDateFormat") val dateFormat =
                java.text.SimpleDateFormat("MMM dd yyyy")
            val deadlineDate = dateFormat.format((deadline ?: 0) * 1000)
            val text = "Deadline: <b>$deadlineDate</b>"
            taskDeadline.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            taskDeadline.text = "N/A"
        }

        taskPriority.text = currentItem?.priority.toString().lowercase() + " priority"
        taskPriority.text = taskPriority.text.toString().replaceFirstChar { it.uppercase() }
        when(currentItem?.priority) {
            TaskPriority.HIGH -> {
                taskPriorityCircle.background.setTint("#fc7d77".toColorInt())
            }
            TaskPriority.MEDIUM -> {
                taskPriorityCircle.background.setTint("#f2d088".toColorInt())
            }
            TaskPriority.LOW -> {
                taskPriorityCircle.background.setTint("#85e075".toColorInt())
            }
            else -> {}
        }

        taskDescription.text = currentItem?.description

        if(currentItem?.progress != null) {
            taskProgress.text = currentItem?.progress.toString() + "% Done"
            taskProgressBar.progress = currentItem?.progress!!
        } else {
            taskProgress.text = "0% Done"
            taskProgressBar.progress = 0
        }

        return binding.root
    }

    private fun initViews() {
        taskTitle = binding.taskDTitle
        taskDepartment = binding.taskDDepartment
        taskAssignedBy = binding.taskDAssignedBy
        taskAssignedDate = binding.taskDAssignedDate
        taskAssignee = binding.taskDAssignee
        taskProgress = binding.taskDProgress
        taskProgressBar = binding.taskDProgressBar
        taskTagSpinner = binding.taskDTagSpinner
        taskDeadline = binding.taskDDeadline
        taskPriorityCircle = binding.taskDPriorityCircle
        taskPriority = binding.taskDPriority
        taskDescription = binding.taskDDescription
    }
}