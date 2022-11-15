package com.example.quizapp.services

import android.util.Log
import com.example.quizapp.repositories.ItemRepository
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionType
import kotlinx.coroutines.CoroutineScope

class ItemService(private var itemRepository: ItemRepository) {
    private var items =  ArrayList<Item>()

    constructor() : this(ItemRepository())

    fun loadItems(items: ArrayList<Item>) {
        this.items = items
        itemRepository.loadItems(items)
    }

    fun filterQuestions(category: String): ArrayList<Item> {
        return getAllItems().filter { it.category == category } as ArrayList<Item>
    }

    // select a given number of items from the repository
    fun selectRandomItems(count: Int): ArrayList<Item> {
        items.clear()
        var counter = count

        // if the given number is greater than the number of items in the repository or if its negative, return all items
        if(count > itemRepository.size() || count < 0) {
            counter = itemRepository.size()
        }

        while(items.size < counter) {
            val item = itemRepository.randomItem()

            // make sure we don't select the same item twice
            if (!items.contains(item)) {
                items.add(item)
            }
        }

        return items
    }

    fun randomizeQuestions() {
        items.shuffle()

        for (question in items) {
            question.answers.shuffle()
        }
    }

    fun getNumberTotalQuestions(): Int {
        return itemRepository.size()
    }

    fun addItem(item: Item) {
        itemRepository.addItem(item)
    }

    fun deleteItem(position: Int) {
        itemRepository.deleteItem(position)
    }

    fun getAllItems(): ArrayList<Item> {
        return itemRepository.getAllItems() as ArrayList<Item>
    }
}