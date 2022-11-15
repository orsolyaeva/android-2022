package com.example.quizapp.repositories

import android.util.Log
import com.example.quizapp.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemRepository() {
    private var items =  mutableListOf<Item>()

    init {
        items = com.example.quizapp.models.items
    }

//    fun generateItems(): MutableList<Item> {
//        Log.d("ItemRepository", "init")
//
//        scope.launch {
//            try {
//                Log.d("ItemRepository", "Started loading questions")
//                val response = withContext(Dispatchers.IO) {
//                    QuestionRepository.getQuestions(5)
//                }
//                Log.d("ItemRepository", "Finished loading questions 2")
//                if (response.isSuccessful) {
//                    Log.d("ItemRepository", "Finished loading questions")
//
//                    val questions = response.body()
//                    items = (questions?.results?.map {
//                        Item(
//                            0,
//                            it.question,
//                            mutableListOf(it.correctAnswer),
//                            mutableListOf(it.correctAnswer).plus(it.incorrectAnswers) as MutableList<String>
//                        )
//                    } as MutableList<Item>?)!!
//                } else {
//                    Log.d("ItemRepository", "Error: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.d("ItemRepository", "Error: ${e.message}")
//            }
//        }
//
//        Log.d("ItemRepository",  items.toString())
//
//        return items
//    }

    fun loadItems(items: MutableList<Item>) {
        this.items = items
    }

    fun randomItem(): Item {
        return items.random()
    }

    fun save(item: Item) {
        items.add(item)
    }

    fun size() : Int {
        return items.size
    }

    fun printItems() {
        for (item in items) {
            println(item)
        }
    }

    fun addItem(item: Item) {
        items.add(item)
    }

    fun deleteItem(position: Int) {
        items.remove(items[position])
    }

    fun getAllItems() : MutableList<Item> {
        return items
    }
}