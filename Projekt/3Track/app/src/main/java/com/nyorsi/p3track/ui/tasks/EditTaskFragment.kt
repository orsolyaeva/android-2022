package com.nyorsi.p3track.ui.tasks

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nyorsi.p3track.R
import com.nyorsi.p3track.api.queryModels.task.CreateTaskRequest
import com.nyorsi.p3track.api.queryModels.task.UpdateTaskRequest
import com.nyorsi.p3track.databinding.FragmentCreateTaskBinding
import com.nyorsi.p3track.databinding.FragmentEditTaskBinding
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.models.UserType
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.GlobalViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditTaskFragment : Fragment() {
    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!
    private val globalViewModel: GlobalViewModel by viewModels()
    private var currentItem: TaskModel? = null

    private lateinit var projectSpinner: Spinner
    private lateinit var assignedSpinner: Spinner
    private lateinit var prioritySpinner: Spinner
    private lateinit var taskName: EditText
    private lateinit var dateSelect: ImageView
    private lateinit var taskDesc: EditText
    private lateinit var updateTaskButton: Button
    private lateinit var deadlineText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.findItem(R.id.add_new_task).isVisible = false
                menu.findItem(R.id.edit_task).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        initViews()
        initListeners()

        val parsedValue = arguments?.getString("taskToUpdate")
        currentItem = Gson().fromJson(parsedValue, object : TypeToken<TaskModel>() {}.type)

        Log.d("EDIT_TASK", currentItem.toString())

        taskName.text.append(currentItem?.title)
        taskDesc.text.append(currentItem?.description)
        taskDesc.setScroller(Scroller(requireContext()))
        taskDesc.maxLines = 10
        taskDesc.isVerticalScrollBarEnabled = true
        taskDesc.movementMethod = ScrollingMovementMethod()

        val prioritySpinnerData = arrayOf("Low", "Medium", "High")
        val priorityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prioritySpinnerData)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = priorityAdapter
        prioritySpinner.setSelection(currentItem?.priority?.ordinal ?: 0)

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(today)
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select a deadline")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        // on deadline select click listener
        dateSelect.setOnClickListener {
            datePicker.show(childFragmentManager, "DATE_PICKER")

            // get the selected date
            datePicker.addOnPositiveButtonClickListener {
                val date = datePicker.headerText
                deadlineText.text = date
            }
        }

        val deadline = currentItem?.deadline
        if (deadline != null) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.format(Date(deadline * 1000))
            deadlineText.text = date
        }

        globalViewModel.loadTaskCreateData()
        globalViewModel.requestState.observe(viewLifecycleOwner) { state ->
            if (state == RequestState.SUCCESS) {
                val userList = globalViewModel.getUserList()
                val projectList = globalViewModel.getDepartmentList()

                val userAdapterData = userList.value?.map { it.firstName + " " + it.lastName }
                val userAdapter = context?.let { ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    userAdapterData!!
                )}
                userAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                assignedSpinner.adapter = userAdapter

                // set the current user as the default selected user
                assignedSpinner.setSelection(userList.value?.indexOfFirst { it.id == currentItem?.assignedTo?.id } ?: 0)

                val projectAdapterData = projectList.value?.map { it.name }

                val currentUser = globalViewModel.getCurrentUser()
                val currentUserDepartment = globalViewModel.getCurrentUserDepartment()

                if(currentUser?.type != UserType.HR_MANAGER) {
                    val projectAdapter = context?.let { ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        listOf(currentUserDepartment?.name!!)
                    )}
                    projectAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    projectSpinner.isEnabled = false
                    projectSpinner.isClickable = false
                    projectSpinner.adapter = projectAdapter
                } else {
                    val projectAdapter = context?.let { ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        projectAdapterData!!
                    )}
                    projectAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    projectSpinner.adapter = projectAdapter
                    projectSpinner.setSelection(projectList.value?.indexOfFirst { it.id == currentItem?.department?.id } ?: 0)
                }
            }
        }


        return binding.root
    }

    @SuppressLint("ShowToast", "SimpleDateFormat")
    private fun initListeners() {
        updateTaskButton.setOnClickListener {
            val taskName = taskName.text.toString()
            val taskDesc = taskDesc.text.toString()
            val priority = prioritySpinner.selectedItem.toString()
            val assigned = assignedSpinner.selectedItem.toString()
            val project = projectSpinner.selectedItem.toString()
            // convert deadline text to unix timestamp
            val deadline = deadlineText.text.toString()

            if(deadline == "Pick a deadline:") {
                Toast.makeText(requireContext(), "Please select a deadline", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val str_date = deadlineText.text.toString()
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val date: Date = formatter.parse(str_date) as Date
            Log.d(CreateTaskFragment.TAG, "today is: ${date.time}")

            if(taskName.isEmpty()) {
                binding.taskName.setBackgroundResource(R.drawable.text_error_border)
                // add margin bottom to snackbar
                val snackBar = Snackbar.make(requireView(), "Task name cannot be empty", Snackbar.LENGTH_SHORT)
                snackBar.anchorView = binding.updateTaskButton
                snackBar.show()
                return@setOnClickListener
            } else {
                // remove background resource
                binding.taskName.setBackgroundResource(0)
            }

            if(taskDesc.isEmpty()) {
                binding.taskDesc.setBackgroundResource(R.drawable.text_error_border)
                val snackBar = Snackbar.make(requireView(), "Task description cannot be empty", Snackbar.LENGTH_SHORT)
                snackBar.anchorView = binding.updateTaskButton
                snackBar.show()
                return@setOnClickListener
            } else {
                binding.taskDesc.setBackgroundResource(0)
            }

            val assignedToUserId = globalViewModel.getUserList().value?.find { it.firstName + " " + it.lastName == assigned }?.id
            val departmentId = globalViewModel.getDepartmentList().value?.find { it.name == project }?.id
            val priorityId = when(priority) {
                "Low" -> 1
                "Medium" -> 2
                "High" -> 3
                else -> 1
            }

            val updateRequest = UpdateTaskRequest(
                taskId = currentItem?.id!!,
                title = taskName,
                description = taskDesc,
                assignedToUserId = assignedToUserId!!,
                priority = priorityId,
                deadline = date.time / 1000,
                departmentId = departmentId!!,
                status = currentItem?.status!!.ordinal,
                progress = currentItem?.progress
            )

            globalViewModel.updateTask(updateRequest)
            globalViewModel.requestState.observe(viewLifecycleOwner) { state ->
                if (state == RequestState.SUCCESS) {
                    val snackBar = Snackbar.make(requireView(), "Task updated successfully", Snackbar.LENGTH_SHORT)
                    snackBar.anchorView = binding.updateTaskButton
                    snackBar.show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun initViews() {
        projectSpinner = binding.projectSpinner
        assignedSpinner = binding.assigneeSpinner
        prioritySpinner = binding.prioritySpinner
        taskName = binding.taskName
        dateSelect = binding.selectDate
        taskDesc = binding.taskDesc
        updateTaskButton = binding.updateTaskButton
        deadlineText = binding.deadline
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(CreateTaskFragment.TAG, "onAttach: ")
        val callback : OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Exit")
                builder.setMessage("Are you sure you want to exit? You will lose all the data you entered.")
                builder.setPositiveButton("Yes") { _, _ ->
                    arguments?.clear()
                    findNavController().navigateUp()
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}