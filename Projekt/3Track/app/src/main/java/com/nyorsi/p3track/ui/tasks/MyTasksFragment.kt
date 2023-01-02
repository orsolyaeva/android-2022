package com.nyorsi.p3track.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nyorsi.p3track.R
import com.nyorsi.p3track.adapters.TaskDataAdapter
import com.nyorsi.p3track.databinding.FragmentMyTasksBinding
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.GlobalViewModel

class MyTasksFragment : Fragment(), TaskDataAdapter.OnItemClickListener {
    private var _binding: FragmentMyTasksBinding? = null
    private val binding get() = _binding!!
    private val globalViewModel: GlobalViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: TaskDataAdapter
    private lateinit var taskFilter: Spinner

    private var taskList: List<TaskModel> = listOf()

    companion object {
        const val TAG = "MyTasksFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.findItem(R.id.add_new_task).isVisible = true
                menu.findItem(R.id.edit_task).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.add_new_task -> {
                        findNavController().navigate(R.id.action_myTasksFragment_to_createTaskFragment)
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyTasksBinding.inflate(inflater, container, false)
        taskFilter = binding.taskFilter

        val taskCategoryList = listOf("All", "Recently added", "Active tasks", "Completed tasks")
        val taskAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, taskCategoryList)
        taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskFilter.adapter = taskAdapter

        recyclerView = binding.recyclerView
        dataAdapter = TaskDataAdapter(ArrayList(), this)
        recyclerView.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(context)
        }
        recyclerView.setHasFixedSize(true)

        Log.d(TAG, "onCreateView: ")

        globalViewModel.loadActivities()
        globalViewModel.requestState.observe(viewLifecycleOwner) {
            if (it == RequestState.SUCCESS) {
                Log.d(TAG, "onCreateView: ${globalViewModel.getTaskList().value}")
                // get userId from shared preferences
                val sharedPref =
                    activity?.getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
                val userIdString = sharedPref?.getString("userID", null)
                Log.d(TAG, "onGetID: $userIdString")
                if (userIdString != null) {
                    val userId = userIdString.toInt()
                    val taskListTemp = globalViewModel.getTaskList().value
                    val filteredTaskList = taskListTemp?.filter { task -> task.assignedTo?.id == userId }

                    if (filteredTaskList != null) {
                        if(filteredTaskList.isEmpty()) {
                            taskList = globalViewModel.getTaskList().value as List<TaskModel>
                            dataAdapter.setData(globalViewModel.getTaskList().value!! as MutableList<TaskModel>)
                        } else {
                            dataAdapter.setData(filteredTaskList as ArrayList<TaskModel>)
                        }
                    }

                    onTaskFilterChanged()
                }
//                val taskList = globalViewModel.getTaskList().value
//                dataAdapter.setData(globalViewModel.getTaskList().value!! as MutableList<TaskModel>)
            }
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val taskId = dataAdapter.getItemAt(position).id
        Log.d(TAG, "onItemClick: $taskId")
        val parsedValue = Gson().toJson(dataAdapter.getItemAt(position), object: TypeToken<TaskModel>() {}.type)
        val bundle = Bundle()
        findNavController().navigate(R.id.taskDescriptionFragment, Bundle().apply {
            putString("currentTask", parsedValue)
        })
    }

    private fun onTaskFilterChanged() {
        taskFilter.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val category = parent?.getItemAtPosition(position).toString()
                Log.d(TAG, "onItemSelected: $category")

                if(category == "All") {
                    val sharedPref =
                        activity?.getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
                    val userIdString = sharedPref?.getString("userID", null)
                    Log.d(TAG, "onGetID: $userIdString")
                    if (userIdString != null) {
                        val userId = userIdString.toInt()
                        val filteredTaskList = taskList.filter { task -> task.assignedTo?.id == userId }

                        if(filteredTaskList.isEmpty()) {
                            dataAdapter.setData(taskList as MutableList<TaskModel>)
                        } else {
                            dataAdapter.setData(filteredTaskList as ArrayList<TaskModel>)
                        }
                    }
                } else {
                        when(category) {
                        "Recently added" -> {
                            val sortedTaskList = taskList.filter { task -> task.status.ordinal == 0 }
                            dataAdapter.setData(sortedTaskList as ArrayList<TaskModel>)
                        }
                        "Active tasks" -> {
                            val filteredTaskList = taskList.filter { task -> task.status.ordinal == 1 || task.status.ordinal == 2 }
                            dataAdapter.setData(filteredTaskList as ArrayList<TaskModel>)
                        }
                        "Completed tasks" -> {
                            val filteredTaskList = taskList.filter { task -> task.status.ordinal == 3 }
                            dataAdapter.setData(filteredTaskList as ArrayList<TaskModel>)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val sharedPref =
                    activity?.getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
                val userIdString = sharedPref?.getString("userID", null)
                Log.d(TAG, "onGetID: $userIdString")
                if (userIdString != null) {
                    val userId = userIdString.toInt()
                    val filteredTaskList = taskList.filter { task -> task.assignedTo?.id == userId }

                    if(filteredTaskList.isEmpty()) {
                        dataAdapter.setData(taskList as MutableList<TaskModel>)
                    } else {
                        dataAdapter.setData(filteredTaskList as ArrayList<TaskModel>)
                    }
                }
            }
        }
    }
}