package com.example.paxman

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.Menu
import android.view.MenuItem


class MainActivity : AppCompatActivity() {
    // Reference to the main view
    var gameView: GameView? = null
    // Reference to the game class.
    var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Saying we want the game to run only in landscape
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)

        gameView = findViewById(R.id.gameView)
        val levelTextView: TextView = findViewById(R.id.level)
        val oxygenTextView: TextView = findViewById(R.id.Health)
        val pointTextView: TextView = findViewById(R.id.points)

        game = Game(this, levelTextView, oxygenTextView, pointTextView)
        game!!.setGameView(gameView)
        gameView!!.initGame(game)

        this.initializeSwipeGestures()
        game!!.newGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_pause) {
            if (this.game!!.togglePause()) {
                item.title = this.resources.getString(R.string.action_unpause)
            } else {
                item.title = this.resources.getString(R.string.action_pause)
            }
            return true
        } else if (id == R.id.action_newGame) {
            game!!.newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeSwipeGestures() {
        gameView!!.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeTop() {
                game!!.getPlayer()!!.setDirection(Direction.UP)
            }

            override fun onSwipeRight() {
                game!!.getPlayer()!!.setDirection(Direction.RIGHT)
            }

            override fun onSwipeLeft() {
                game!!.getPlayer()!!.setDirection(Direction.LEFT)
            }

            override fun onSwipeBottom() {
                game!!.getPlayer()!!.setDirection(Direction.DOWN)
            }
        })
    }
}