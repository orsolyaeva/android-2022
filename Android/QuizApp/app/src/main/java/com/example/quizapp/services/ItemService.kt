package com.example.quizapp.services

import com.example.quizapp.repositories.ItemRepository
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionType

class ItemService(private var itemRepository: ItemRepository) {
    private var items =  ArrayList<Item>()

    constructor() : this(ItemRepository())

    suspend fun generateItems() {
        itemRepository.generateItems()
    }

    fun loadItems(items: ArrayList<Item>) {
        this.items = items
        itemRepository.loadItems(items)
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
            when(question.type) {
                QuestionType.SINGLE_CHOICE.ordinal -> {
                    val correctIndex = question.answers.indexOf(question.correct[0])
                    val answer = question.answers[correctIndex]
                    question.answers.shuffle()
                    val tempIndex = question.answers.indexOf(answer)
                    question.correct = mutableListOf(question.answers[tempIndex])
                }
                QuestionType.MULTIPLE_CHOICE.ordinal -> {
                    // get all correct answers
                    val correctAnswers = question.correct

                    question.answers.shuffle()

                    val newCorrectAnswers = mutableListOf<String>()
                    for (answer in correctAnswers) {
                        // get the index of the correct answer in the shuffled list
                        val tempIndex = question.answers.indexOf(answer)
                        newCorrectAnswers.add(question.answers[tempIndex])
                    }

                    question.correct = newCorrectAnswers
                }
            }
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