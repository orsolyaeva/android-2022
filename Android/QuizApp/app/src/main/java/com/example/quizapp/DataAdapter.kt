package com.example.quizapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
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
        private val detailsButton: TextView = itemView.findViewById(R.id.detailsButton)
        private val deleteButton: TextView = itemView.findViewById(R.id.deleteButton)

        init {
            itemView.setOnClickListener(this)
            deleteButton.setOnClickListener {
                val currentPosition = this.adapterPosition
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Delete Question")
                builder.setMessage("Are you sure you want to delete the question?")
                builder.setPositiveButton("Yes") { _, _ ->
                    deleteListener.onItemDelete(currentPosition)
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
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

    // onCreateViewHolder is called when the RecyclerView needs a new ViewHolder of the given type to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.question_card, parent, false)
        return DataViewHolder(itemView)
    }

    // onBindViewHolder is called by the RecyclerView to display the data at the specified position.
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentItem = list[position]
        holder.questionText.text = currentItem.question
        if (holder.questionText.text.length > 50) {
            holder.questionText.text = holder.questionText.text.subSequence(0, 35).toString() + "..."
        }
        holder.answerText.text = currentItem.answers[0]
        holder.answerText.setTextColor(Color.parseColor("#3e9657"))
        holder.answerText.setTypeface(null, android.graphics.Typeface.BOLD)
        when(currentItem.type) {
            QuestionType.MULTIPLE_CHOICE.ordinal -> holder.answerType.text = "Multiple Choice"
            QuestionType.SINGLE_CHOICE.ordinal -> holder.answerType.text = "Single choice"
            QuestionType.TRUE_FALSE.ordinal -> holder.answerType.text = "True or False"
        }
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: MutableList<Item>) {
        list = newData
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Item {
        return list[position]
    }
}