package com.example.quizapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.DataAdapter
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuestionListBinding
import com.example.quizapp.viewModels.QuizViewModel

class QuestionListFragment : Fragment(), DataAdapter.OnItemClickListener, DataAdapter.OnItemDeleteListener, DataAdapter.OnItemDetailsListener {
    private lateinit var categoryFilter: Spinner
    private lateinit var dataAdapter: DataAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentQuestionListBinding
    private val viewModel: QuizViewModel by activityViewModels()

    companion object {
        const val TAG = "QuestionListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionListBinding.inflate(inflater, container, false)
        categoryFilter = binding.categoryFilter

        onCategoryFilterChanged()
        loadCategories()

        recyclerView = binding.recyclerView
        dataAdapter = DataAdapter(ArrayList(), this, this, this)

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

    // function to load categories from the view model and set them to the spinner
    private fun loadCategories() {
        val categoryList = viewModel.getCategoryList()

        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("Any category") + categoryList
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        categoryFilter.adapter = categoryAdapter
    }


    override fun onItemClick(position: Int) {
        Log.i(TAG, "Clicked item $position")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemDelete(position: Int) {
        val questionText = dataAdapter.getItem(position).question
        // get the current questions actual position in the list since their position in the recycler view is not the same
        // for example if the user has filtered the list by category, the position in the recycler view will not be the same as the position in the list
        val questionPosition =
            viewModel.getAllQuestions().value?.indexOfFirst { it.question == questionText }
        val category = questionPosition?.let { dataAdapter.getItem(it).category }
        val currentCategory = categoryFilter.selectedItem.toString()
        viewModel.deleteQuestion(questionPosition!!)

        Log.i(TAG, "category item $category")
        Log.i(TAG, "question item $questionText")

        val categoryList = viewModel.getCategoryList()

        // if the category of the deleted question is not in the category list anymore, remove it from the spinner
        // this is done to prevent the user from filtering by a category that no longer exists
        if (!categoryList.contains(category)) {
            val categoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listOf("Any category") + categoryList
            )
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // set the spinner to "Any category" if the deleted question was the last question in the category
            categoryFilter.adapter = categoryAdapter
        } else {
            if (currentCategory == "Any category") {
                viewModel.getAllQuestions().observe(viewLifecycleOwner) {
                    dataAdapter.setData(it)
                }
            } else {
                // if the deleted question was not the last question in the category, filter the list by the category
                if (category != null) {
                    viewModel.filterQuestions(category).observe(viewLifecycleOwner) {
                        dataAdapter.setData(it)
                    }
                }
            }
        }

        dataAdapter.notifyDataSetChanged()
    }

    override fun onItemDetails(position: Int) {
        val questionText = dataAdapter.getItem(position).question
        val questionPosition =
            viewModel.getAllQuestions().value?.indexOfFirst { it.question == questionText }
        findNavController().navigate(R.id.questionDetailFragment, Bundle().apply {
            putInt("id", questionPosition!!)
        })
    }

    private fun onCategoryFilterChanged() {
        // when the user selects a category from the spinner, filter the list by the selected category
        categoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val category = parent?.getItemAtPosition(position).toString()
                if (category == "Any category") {
                    viewModel.getAllQuestions().observe(viewLifecycleOwner) {
                        dataAdapter.setData(it)
                    }
                } else {
                    viewModel.filterQuestions(category).observe(viewLifecycleOwner) {
                        dataAdapter.setData(it)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.getAllQuestions().observe(viewLifecycleOwner) {
                    dataAdapter.setData(it)
                }
            }
        }
    }
}