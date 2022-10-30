package com.example.quizapp.services

import com.example.quizapp.repositories.ItemRepository
import com.example.quizapp.models.Item
import com.example.quizapp.models.QuestionType

class ItemService(private var itemRepository: ItemRepository) {
    private var items =  ArrayList<Item>()

    constructor() : this(ItemRepository())

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
                    val answer = question.answers[question.correct[0]]
                    question.answers.shuffle()
                    val tempIndex = question.answers.indexOf(answer)
                    question.correct = mutableListOf(tempIndex)
                }
                QuestionType.MULTIPLE_CHOICE.ordinal -> {
                    val correctAnswers = question.correct.map { question.answers[it] }

                    question.answers.shuffle()

                    val newCorrectAnswers = mutableListOf<Int>()
                    for (answer in correctAnswers) {
                        newCorrectAnswers.add(question.answers.indexOf(answer))
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
        itemRepository.deleteItem(items[position])
    }
}