package com.example.quizapp.repositories

import com.example.quizapp.models.Item

// item repository stores all the items in the app and provides methods to access them
object ItemRepository {
    private var items =  mutableListOf<Item>()

    // add item to the list
    fun loadItems(items: MutableList<Item>) {
        if(this.items.isEmpty()) {
            this.items = com.example.quizapp.models.items
        } else {
            this.items = items
        }
    }

    // get a random item from the list
    fun randomItem(): Item {
        return items.random()
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