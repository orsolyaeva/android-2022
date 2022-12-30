package com.nyorsi.p3track.ui.tasks

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.nyorsi.p3track.R
import com.nyorsi.p3track.databinding.FragmentCreateTaskBinding
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.GlobalViewModel


class CreateTaskFragment : Fragment() {
    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!
    private val globalViewModel: GlobalViewModel by viewModels()

    private lateinit var projectSpinner: Spinner
    private lateinit var assignedSpinner: Spinner
    private lateinit var prioritySpinner: Spinner
    private lateinit var taskName: EditText
    private lateinit var dateSelect: ImageView
    private lateinit var taskDesc: EditText
    private lateinit var createTaskButton: Button

    companion object {
        const val TAG = "CreateTaskFragment"
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
        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)

        initViews()
        taskName.hint = "Enter task name here"
        taskDesc.hint = "Task Description"

        taskDesc.setScroller(Scroller(requireContext()))
        taskDesc.maxLines = 10
        taskDesc.isVerticalScrollBarEnabled = true
        taskDesc.movementMethod = ScrollingMovementMethod()

        val prioritySpinnerData = arrayOf("Low", "Medium", "High")
        val priorityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prioritySpinnerData)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = priorityAdapter

        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select a deadline")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        // on deadline select click listener
        dateSelect.setOnClickListener {
            datePicker.show(childFragmentManager, "DATE_PICKER")
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

                val projectAdapterData = projectList.value?.map { it.name }
                val projectAdapter = context?.let { ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    projectAdapterData!!
                )}
                projectAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                projectSpinner.adapter = projectAdapter
            }
        }

        return binding.root
    }

    private fun initViews() {
        projectSpinner = binding.projectSpinner
        assignedSpinner = binding.assigneeSpinner
        prioritySpinner = binding.prioritySpinner
        taskName = binding.taskName
        dateSelect = binding.selectDate
        taskDesc = binding.taskDesc
        createTaskButton = binding.createTaskButton
    }
}