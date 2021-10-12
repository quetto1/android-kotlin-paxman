package com.example.paxman

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

open class Entity {
    var x: Int
        private set
    var y: Int
        private set
    var speed = 0
    var bitmap: Bitmap
        private set
    var isInGame = true
    private var direction: Direction = Direction.NONE

    constructor(x: Int, y: Int, bitmap: Bitmap, speed: Int, direction: Direction) {
        this.x = x
        this.y = y
        this.bitmap = bitmap
        this.speed = speed
        this.direction = direction
    }

    constructor(x: Int, y: Int, bitmap: Bitmap) {
        this.x = x
        this.y = y
        this.bitmap = bitmap
    }

    fun setDirection(direction: Direction) {
        this.direction = direction
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        canvas.drawBitmap(bitmap, x.toFloat(), y.toFloat(), paint)
    }

    fun move(canvasWidth: Int, canvasHeight: Int) {
        when (direction) {
            Direction.LEFT -> if (x - speed > 0) {
                x -= speed
            }
            Direction.RIGHT -> if (x + speed + bitmap.width < canvasWidth) {
                x += speed
            }
            Direction.UP -> if (y - speed > 0) {
                y -= speed
            }
            Direction.DOWN -> if (y + speed + bitmap.width < canvasHeight) {
                y += speed
            }
        }
    }

    fun outOfBounds(): Boolean {
        return (x - speed <= 0);
    }

    fun isColliding(entity: Entity): Boolean {
        val current = Rect(x, y, x + bitmap.width, y + bitmap.height)
        val toCheck = Rect(
            entity.x,
            entity.y,
            entity.x + entity.bitmap.width,
            entity.y + entity.bitmap.height
        )
        return current.intersect(toCheck)
    }
}