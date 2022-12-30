package com.nyorsi.p3track.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.*
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
                    val taskList = globalViewModel.getTaskList().value
                    val filteredTaskList = taskList?.filter { task -> task.assignedTo?.id == userId }

                    if (filteredTaskList != null) {
                        if(filteredTaskList.isEmpty()) {
                            dataAdapter.setData(globalViewModel.getTaskList().value!! as MutableList<TaskModel>)
                        } else {
                            dataAdapter.setData(filteredTaskList as ArrayList<TaskModel>)
                        }
                    }
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
}