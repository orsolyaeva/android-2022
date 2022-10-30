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
}