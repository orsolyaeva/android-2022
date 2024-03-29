package com.nyorsi.p3track.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nyorsi.p3track.R
import com.nyorsi.p3track.adapters.GroupDataAdapter
import com.nyorsi.p3track.databinding.FragmentMyGroupsBinding
import com.nyorsi.p3track.models.DepartmentModel
import com.nyorsi.p3track.ui.activities.ActivityFragment
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.GlobalViewModel


class MyGroupsFragment : Fragment(), GroupDataAdapter.OnItemClickListener {
    private var _binding: FragmentMyGroupsBinding? = null
    private val binding get() = _binding!!
    private val globalViewModel: GlobalViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: GroupDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyGroupsBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        dataAdapter = GroupDataAdapter(ArrayList(), this)
        recyclerView.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(context)
        }
        recyclerView.setHasFixedSize(true)

        globalViewModel.loadDepartments()
        globalViewModel.requestState.observe(viewLifecycleOwner) {
            if (it == RequestState.SUCCESS) {
                dataAdapter.setData(globalViewModel.getDepartmentList().value!! as MutableList<DepartmentModel>)
            }
        }

        return binding.root
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

    override fun onItemClick(position: Int) {
        val department = dataAdapter.getItem(position)
        findNavController().navigate(R.id.action_myGroupsFragment_to_groupMembersFragment, Bundle().apply {
            putInt("department", department.id)
        })
    }
}