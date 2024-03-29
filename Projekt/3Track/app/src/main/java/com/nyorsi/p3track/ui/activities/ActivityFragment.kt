package com.nyorsi.p3track.ui.activities

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nyorsi.p3track.databinding.FragmentActvitiesBinding
import com.nyorsi.p3track.R
import com.nyorsi.p3track.adapters.ActivityDataAdapter
import com.nyorsi.p3track.models.ActivityModel
import com.nyorsi.p3track.models.DepartmentModel
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.ActivityViewModel
import com.nyorsi.p3track.viewModels.GlobalViewModel

class ActivityFragment : Fragment(), ActivityDataAdapter.OnItemClickListener {
    private var _binding: FragmentActvitiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: ActivityDataAdapter
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
        val sharedPref = requireActivity().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
        val token = sharedPref?.getString("token", null)

        _binding = FragmentActvitiesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        // check if the last fragment transition was the login fragment
        // if so, then we need to update the toke

//        val fragmentManager = getFragmentManager()
//        val counter = fragmentManager?.backStackEntryCount
//        Log.d(TAG, "onCreateViewFM: ${fragmentManager?.getBackStackEntryAt(counter!! - 2)?.name}")

//        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Activities"
//        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(Html.fromHtml("<font color=\"black\">" + "Activities" + "</font>"));

        recyclerView = binding.recyclerView
        dataAdapter = ActivityDataAdapter(ArrayList(), this)
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
                Log.d(TAG, "actvModel: ${globalViewModel.getTaskList().value}")
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