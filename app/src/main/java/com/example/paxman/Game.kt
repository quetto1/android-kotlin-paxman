package com.example.paxman

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.widget.TextView
import org.example.paxman.MovementType
import java.util.*

/**
 * This class should contain all your game logic
 */
class Game(
    private val context: Context,
    private val levelView: TextView,
    private val healthView: TextView,
    private val pointsView: TextView
) {
    private var gameView: GameView? = null
    private var h = 0
    private var w = 0

    private var health = 500
    private var healthValue = 30
    private var hearts: ArrayList<Entity> = ArrayList<Entity>()
    private var totalHearts = 0
    private var heartsOnScreen = 0

    private var points = 0
    private var level = 1
    private var player: Entity? = null
    private var paused = false
    private var enemies: ArrayList<Enemy> = ArrayList<Enemy>()
    private var totalEnemies = 0
    private var enemiesOnScreen = 0
    private val gameHandler = Handler()
    private val gameRunnable: Runnable = object : Runnable {
        override fun run() {
            handleGameLogic()
            gameHandler.postDelayed(this, 30)
        }
    }

    fun setGameView(view: GameView?) {
        gameView = view
    }

    fun newGame() {
        player = Entity(
            50,
            400,
            BitmapFactory.decodeResource(context.resources, R.drawable.pacman),
            12,
            Direction.NONE
        )
        enemies = ArrayList<Enemy>()
        totalEnemies = 2
        enemiesOnScreen = 0
        hearts = ArrayList<Entity>()
        totalHearts = 10
        heartsOnScreen = 0
        healthValue = 30
        points = 0
        health = 500
        level = 1
        pointsView.text = String.format(context.resources.getString(R.string.points) + "%d", points)
        healthView.text = String.format("Health:" + "%d", health)
        levelView.text = String.format(context.resources.getString(R.string.level) + "%d", level)
        paused = false
        // Redraw the screen
        gameView!!.invalidate()
    }

    fun togglePause(): Boolean {
        paused = !paused
        return paused
    }

    fun setSize(w: Int, h: Int) {
        this.w = w
        this.h = h
    }

    fun getPlayer(): Entity? {
        return player
    }

    fun getHearts(): ArrayList<Entity> {
        return hearts
    }

    fun getEnemies(): ArrayList<Enemy> {
        return enemies
    }

    fun spawnHearts(canvasWidth: Int, canvasHeight: Int) {
        if (heartsOnScreen < totalHearts) {
            var i = 0
            while (i < totalHearts - heartsOnScreen) {
                spawnNewHeart(canvasWidth, canvasHeight)
                heartsOnScreen++
                i++
            }
        }
    }

    fun spawnEnemies(canvasWidth: Int, canvasHeight: Int) {
        if (enemiesOnScreen < totalEnemies) {
            var i = 0
            while (i < totalEnemies - enemiesOnScreen) {
                spawnNewEnemy(canvasWidth, canvasHeight)
                enemiesOnScreen++
                i++
            }
        }
    }

    private fun handleGameLogic() {
        if (paused) return
        heartCollisionCheck()
        enemyCollisionCheck()
        player?.move(w, h)
        health--
        healthView.text = String.format("Health:" + "%d", health)
        pointsView.text = String.format(context.resources.getString(R.string.points) + "%d", points)
        if (health == 0) newGame()
        handleLevels()
        gameView!!.invalidate()
    }

    private fun spawnNewHeart(canvasWidth: Int, canvasHeight: Int) {
        val r = Random()
        val heartBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.heart)
        val x = r.nextInt(canvasWidth - heartBitmap.width)
        val y = r.nextInt(canvasHeight - heartBitmap.height)
        hearts.add(Entity(x, y, heartBitmap, 0, Direction.LEFT))
    }

    private fun spawnNewEnemy(canvasWidth: Int, canvasHeight: Int) {
        val r = Random()
        val enemyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
        val x = canvasWidth - enemyBitmap.width
        val y = r.nextInt(canvasHeight - enemyBitmap.height)
        val enemyType = r.nextInt(2)
        if (enemyType == 1) {
            enemies.add(Enemy(x, y, enemyBitmap, 7, Direction.LEFT, MovementType.ZIGZAG))
        } else {
            enemies.add(Enemy(x, y, enemyBitmap, 7, Direction.LEFT, MovementType.STRAIGHT))
        }
    }

    private fun heartCollisionCheck() {
        for (i in hearts.indices) {
            val heart: Entity = hearts[i]
            if (heart.isInGame) {
                if (heart.outOfBounds()) {
                    heart.isInGame = false
                    heartsOnScreen--
                } else if (player!!.isColliding(heart)) {
                    hearts[i].isInGame = false
                    heartsOnScreen--
                    health += healthValue
                    points += healthValue
                    healthView.text =
                        String.format("Health" + "%d", health)
                }
                heart.move(w, h)
            }
        }
    }

    private fun enemyCollisionCheck() {
        var startNewGame = false;
        for (i in enemies.indices) {
            val enemy: Enemy = enemies[i]
            if (enemy.isInGame) {
                if (enemy.getMovementType() === MovementType.ZIGZAG) {
                    enemy.zigZagEnemy()
                } else {
                    enemy.straight()
                }
                if (enemy.outOfBounds()) {
                    enemy.isInGame = false
                    enemiesOnScreen--
                } else if (player!!.isColliding(enemy)) {
                    startNewGame = true;
                }
                enemy.move(w, h)
            }
        }
        if(startNewGame) {
            newGame()
        }
    }

    private fun handleLevels() {
        if (points == 300 && level != 2) {
            level = 2
            levelView.text =
                String.format(context.resources.getString(R.string.level) + "%d", level)
            for (i in enemies.indices) {
                enemies[i].speed = (enemies[i].speed + 2)
            }
            totalEnemies += 1
        } else if (points == 600 && level != 3) {
            level = 3
            levelView.text =
                String.format(context.resources.getString(R.string.level) + "%d", level)
            for (i in enemies.indices) {
                enemies[i].speed = (enemies[i].speed + 2)
            }
            totalHearts -= 1
        } else if (points == 900 && level != 4) {
            level = 4
            levelView.text =
                String.format(context.resources.getString(R.string.level) + "%d", level)
            for (i in enemies.indices) {
                enemies[i].speed = (enemies[i].speed + 2)
            }
            totalEnemies += 2
            totalHearts -= 1
        } else if (points == 1200 && level != 5) {
            level = 5
            levelView.text =
                String.format(context.resources.getString(R.string.level) + "%d", level)
            totalEnemies += 1
            totalHearts -= 2
            healthValue -= 10
        }
    }

    init {
        gameHandler.postDelayed(gameRunnable, 0)
    }
}