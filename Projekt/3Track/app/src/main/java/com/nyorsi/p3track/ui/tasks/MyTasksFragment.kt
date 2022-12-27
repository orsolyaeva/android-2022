package com.nyorsi.p3track.ui.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nyorsi.p3track.adapters.TaskDataAdapter
import com.nyorsi.p3track.databinding.FragmentMyTasksBinding
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.ui.activities.ActivityFragment
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
                dataAdapter.setData(globalViewModel.getTaskList().value!! as MutableList<TaskModel>)
            }
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        Log.i(ActivityFragment.TAG, "Clicked item $position")
    }
}