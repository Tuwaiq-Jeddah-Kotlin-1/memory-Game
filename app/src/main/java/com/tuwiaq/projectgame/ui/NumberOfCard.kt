package com.tuwiaq.projectgame.ui

enum class NumberOfCard {
    EASY,
    MEDIUM,
    HARD;

    fun getWidth(): Int {
        return when (this) {
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }

    fun getHeight(): Int {
        return when (this) {
            EASY -> 8
            MEDIUM -> 12
            HARD -> 16
        }
    }

    fun numberOfCardToString(): String {
        return this.name
    }
}