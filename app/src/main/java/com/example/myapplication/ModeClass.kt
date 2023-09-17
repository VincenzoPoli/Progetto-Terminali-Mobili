package com.example.myapplication

enum class ModeClass(private val mode : Int) {
    TOUCHPAD(0),
    DIRECT(1);

    fun getMode() = this.mode
}