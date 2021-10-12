package com.example.paxman

import android.graphics.Bitmap
import org.example.paxman.MovementType

class Enemy(
    x: Int,
    y: Int,
    bitmap: Bitmap?,
    speed: Int,
    direction: Direction?,
    movementType: MovementType
) :
    Entity(x, y, bitmap!!, speed, direction!!) {
    private var previousDirection: Direction? = null
    private val movementType: MovementType = movementType
    private var goingUp = false
    private var timesUp = 0
    private var timesDown = 0
    private val originalSpeed: Int = speed

    fun getMovementType(): MovementType {
        return movementType
    }

    fun straight() {
        speed = originalSpeed + 4
        super.setDirection(Direction.LEFT)
    }

    fun zigZagEnemy() {
        if (previousDirection != Direction.LEFT) {
            previousDirection = Direction.LEFT
            super.setDirection(Direction.LEFT)
            return
        }
        if (goingUp) {
            if (timesUp < 50) {
                super.setDirection(Direction.UP)
                previousDirection = Direction.UP
                timesUp++
            } else {
                goingUp = false
                timesUp = 0
            }
        } else {
            if (timesDown < 50) {
                super.setDirection(Direction.DOWN)
                previousDirection = Direction.DOWN
                timesDown++
            } else {
                goingUp = true
                timesDown = 0
            }
        }
    }

    fun trackSomething(entity: Entity?) {
        val x = 0
        val y = 0
        if (Math.abs(x) > Math.abs(y)) {
            if (x > 0) super.setDirection(Direction.RIGHT)
            if (x < 0) super.setDirection(Direction.LEFT)
        } else {
            if (y > 0) super.setDirection(Direction.DOWN)
            if (y < 0) super.setDirection(Direction.UP)
        }
    }

}