package com.example.myapplication

class Point {

    private var X: Int? = null
    private var Y: Int? = null

    fun getX(): Int? {
        return this.X
    }

    fun getY(): Int? {
        return this.Y
    }

    fun setXY(x: Int?, y: Int?)
    {
        this.X = x
        this.Y = y
    }

}