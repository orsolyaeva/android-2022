package com.example.quizapp.repositories

import com.example.quizapp.models.Item

object ItemRepository {
    private var items =  mutableListOf<Item>()

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