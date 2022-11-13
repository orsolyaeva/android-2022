package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.DataAdapter
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuestionListBinding
import com.example.quizapp.models.Item
import com.example.quizapp.viewModels.QuizViewModel

class QuestionListFragment : Fragment(), DataAdapter.OnItemClickListener, DataAdapter.OnItemDeleteListener, DataAdapter.OnItemDetailsListener {
    private lateinit var dataAdapter: DataAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentQuestionListBinding
    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionListBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerView
        dataAdapter = DataAdapter(ArrayList<Item>(), this, this, this)

        recyclerView.apply {
            adapter = dataAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        recyclerView.setHasFixedSize(true)

        viewModel.getAllQuestions().observe(viewLifecycleOwner) {
            dataAdapter.setData(it)
        }

        return binding.root
    }


    override fun onItemClick(position: Int) {
        Log.i("QuestionListFragment", "Clicked item $position")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemDelete(position: Int) {
        viewModel.deleteQuestion(position)
        dataAdapter.notifyDataSetChanged()
        Log.i("QuestionListFragment", "Deleted item $position")
    }

    override fun onItemDetails(position: Int) {
        findNavController().navigate(R.id.questionDetailFragment, Bundle().apply {
            putInt("id", position)
        })
    }
}