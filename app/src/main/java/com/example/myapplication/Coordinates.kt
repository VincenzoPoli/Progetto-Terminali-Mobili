package com.example.myapplication

class Coordinates {

    private var difference = Point()
    private var current = Point()
    private var old = Point()

    fun getX(): Int? {
        return current.getX()
    }

    fun getY(): Int? {
        return current.getY()
    }

    fun getOldX(): Int? {
        return old.getX()
    }

    fun getOldY(): Int? {
        return old.getY()
    }

    fun areChanged(): Boolean {
        if (current.getX() != old.getX() || current.getY() != old.getY())
            return true
        return false
    }

    fun areNotNull(): Boolean {
        if (current.getX() != null && current.getY() != null && old.getX() != null && old.getY() != null)
            return true
        return false
    }

    fun setXY(x: Int?, y: Int?)
    {
        current.setXY(x, y)
    }

    fun setOldXY(x: Int?, y: Int?)
    {
        old.setXY(x, y)
    }

    fun updateOld() {
        old.setXY(current.getX(), current.getY())
    }

    fun computeDifference(resX: Int, resY: Int, widthTouchpad: Int, heightTouchPad: Int) : String {
        difference.setXY((resX * current.getX()!!) / widthTouchpad, (resY * current.getY()!!) / heightTouchPad)
        return "MP_${difference.getX()}_${difference.getY()}"
    }

    fun computeDifference(speed: Int) : String {
        difference.setXY(current.getX()!! - old.getX()!!, current.getY()!! - old.getY()!!)
        difference.setXY(difference.getX()!! * speed,difference.getY()!! * speed)
        return "MX${difference.getX()},MY${difference.getY()}"
    }

}
