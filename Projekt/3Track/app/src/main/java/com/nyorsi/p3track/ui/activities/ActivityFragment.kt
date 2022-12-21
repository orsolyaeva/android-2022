package com.nyorsi.p3track.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nyorsi.p3track.databinding.FragmentActvitiesBinding
import com.nyorsi.p3track.R
import com.nyorsi.p3track.adapters.DataAdapter
import com.nyorsi.p3track.models.ActivityModel
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.ActivityViewModel

class ActivityFragment : Fragment(), DataAdapter.OnItemClickListener {
    private var _binding: FragmentActvitiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: DataAdapter

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
        _binding = FragmentActvitiesBinding.inflate(inflater, container, false)
        // hide bottom navigation bar from login fragment
        recyclerView = binding.recyclerView
        dataAdapter = DataAdapter(ArrayList(), this)
        recyclerView.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        recyclerView.setHasFixedSize(true)

        activityViewModel.getActivitiesState.observe(viewLifecycleOwner) {
            if (it == RequestState.LOADING) {
                return@observe
            }
            if (it == RequestState.SUCCESS) {
                dataAdapter.setData(activityViewModel.activityList.value!! as MutableList<ActivityModel>)
            }
        }

        (activity as AppCompatActivity?)!!.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
        return binding.root
    }

    override fun onItemClick(position: Int) {
        Log.i(TAG, "Clicked item $position")
    }
}