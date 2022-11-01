package com.example.quizapp.repositories

import com.example.quizapp.models.Item

class ItemRepository {
    private var items =  mutableListOf<Item>()

    init {
        items = com.example.quizapp.models.items
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