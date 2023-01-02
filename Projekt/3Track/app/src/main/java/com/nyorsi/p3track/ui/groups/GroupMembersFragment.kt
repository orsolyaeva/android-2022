package com.nyorsi.p3track.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nyorsi.p3track.R
import com.nyorsi.p3track.adapters.UserDataAdapter
import com.nyorsi.p3track.databinding.FragmentMyGroupsBinding
import com.nyorsi.p3track.models.UserModel
import com.nyorsi.p3track.utils.RequestState
import com.nyorsi.p3track.viewModels.GlobalViewModel
import org.w3c.dom.Text

class GroupMembersFragment : Fragment(), UserDataAdapter.OnItemClickListener {
    private var _binding: FragmentMyGroupsBinding? = null
    private val binding get() = _binding!!
    private val globalViewModel: GlobalViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: UserDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyGroupsBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        dataAdapter = UserDataAdapter(ArrayList(), this)
        recyclerView.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(context)
        }
        recyclerView.setHasFixedSize(true)

        // get department id from bundle
        val departmentId = arguments?.getInt("department")
        Log.d("GroupMembersFragment", "departmentId: $departmentId")

        globalViewModel.loadUsersWithDepartmentId(departmentId!!)
        globalViewModel.requestState.observe(viewLifecycleOwner) {
            if (it == RequestState.SUCCESS) {
                dataAdapter.setData(globalViewModel.getUsersWithDepartmentId() as MutableList<UserModel>)
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
       // @TODO("Implement this")
    }
}