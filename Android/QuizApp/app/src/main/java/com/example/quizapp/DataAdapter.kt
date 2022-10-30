package com.example.quizapp

import com.example.quizapp.models.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.models.QuestionType

class DataAdapter(
    private var list: MutableList<Item>,
    private val clickListener: OnItemClickListener,
    private val deleteListener: OnItemDeleteListener,
    private val detailsListener: OnItemDetailsListener,
): RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemDeleteListener {
        fun onItemDelete(position: Int)
    }

    interface OnItemDetailsListener {
        fun onItemDetails(position: Int)
    }

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val questionText: TextView = itemView.findViewById(R.id.questionCardText)
        val answerText: TextView = itemView.findViewById(R.id.cardCorrectAnswer)
        val answerType : TextView = itemView.findViewById(R.id.cardAnswerType)
        val detailsButton: TextView = itemView.findViewById(R.id.detailsButton)
        private val deleteButton: TextView = itemView.findViewById(R.id.deleteButton)

        init {
            itemView.setOnClickListener(this)
            deleteButton.setOnClickListener {
                val currentPosition = this.adapterPosition
                deleteListener.onItemDelete(currentPosition)
            }
            detailsButton.setOnClickListener {
                val currentPosition = this.adapterPosition
                detailsListener.onItemDetails(currentPosition)
            }
        }

        override fun onClick(v: View?) {
            val currentPosition = this.adapterPosition
            clickListener.onItemClick(currentPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.question_card, parent, false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = list[position]
        holder.questionText.text = currentItem.question
        holder.answerText.text = currentItem.answers[0]
        when(currentItem.type) {
            QuestionType.MULTIPLE_CHOICE.ordinal -> holder.answerType.text = "Multiple Choice"
            QuestionType.SINGLE_CHOICE.ordinal -> holder.answerType.text = "Single choice"
            QuestionType.SPINNER.ordinal -> holder.answerType.text = "Spinner"
        }
    }

    override fun getItemCount() = list.size

    fun setData(newData: MutableList<Item>) {
        list = newData
    }
}