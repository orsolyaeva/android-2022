package com.nyorsi.p3track.ui.activities

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nyorsi.p3track.databinding.FragmentActvitiesBinding
import com.nyorsi.p3track.R
import com.nyorsi.p3track.adapters.DataAdapter
import com.nyorsi.p3track.models.ActivityModel
import com.nyorsi.p3track.models.DepartmentModel
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.ActivityViewModel
import com.nyorsi.p3track.viewModels.GlobalViewModel

class ActivityFragment : Fragment(), DataAdapter.OnItemClickListener {
    private var _binding: FragmentActvitiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: DataAdapter
    private val globalViewModel: GlobalViewModel by viewModels()

    companion object {
        const val TAG: String = "ActivityFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Activities"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activityViewModel = ViewModelProvider(this)[ActivityViewModel::class.java]

        val sharedPref = requireActivity().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
        val token = sharedPref?.getString("token", null)

        _binding = FragmentActvitiesBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        dataAdapter = DataAdapter(ArrayList(), this)
        recyclerView.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        recyclerView.setHasFixedSize(true)

//        userViewModel.getUsers()
//
//        userViewModel.requestState.observe(viewLifecycleOwner) { itu ->
//            when (itu) {
//                RequestState.SUCCESS -> {
//                    Log.d(TAG, "onCreateView: ${userViewModel.userList.value}")
//                    activityViewModel.getActivities()
//                    activityViewModel.userList = userViewModel.userList
//                    activityViewModel.getActivitiesState.observe(viewLifecycleOwner) {
//                        if (it == RequestState.SUCCESS) {
//                            Log.d(TAG, "actvModel: ${activityViewModel.activityList.value}")
//                            dataAdapter.setData(activityViewModel.activityList.value!! as MutableList<ActivityModel>)
//                        } else {
//                            return@observe
//                        }
//                    }
//                }
//                else -> {
//                    Log.d(TAG, "User request state: $itu")
//                }
//            }
//        }

        globalViewModel.loadActivities()
        globalViewModel.requestState.observe(viewLifecycleOwner) {
            if (it == RequestState.SUCCESS) {
                Log.d(TAG, "actvModel: ${globalViewModel.getActivityList().value}")
                Log.d(TAG, "actvModel: ${globalViewModel.getUserList().value}")
                dataAdapter.setData(globalViewModel.getActivityList().value!! as MutableList<ActivityModel>,
                                    globalViewModel.getDepartmentList().value!! as MutableList<DepartmentModel>,
                                    globalViewModel.getTaskList().value!! as MutableList<TaskModel>)
            }
        }

        (activity as AppCompatActivity?)!!.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
        return binding.root
    }

    override fun onItemClick(position: Int) {
        Log.i(TAG, "Clicked item $position")
    }
}