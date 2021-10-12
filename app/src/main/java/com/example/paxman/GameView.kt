package com.example.paxman

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

class GameView : View {
    private var game: Game? = null
    var h = 0
    var w = 0

    fun initGame(game: Game?) {
        this.game = game
    }

    /* The next 3 constructors are needed for the Android view system,
    when we have a custom view.
     */
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    // In the onDraw we put all our code that should be
    // drawn whenever we update the screen.
    override fun onDraw(canvas: Canvas) {
        // Here we get the width and height
        w = canvas.width
        h = canvas.height
        game?.setSize(w, h)
        game?.spawnHearts(w, h)
        game?.spawnEnemies(w, h)


        // Clear entire canvas to white color
        canvas.drawColor(Color.BLACK)

        // Draw the pacman
        game?.getPlayer()?.draw(canvas)
        // Draw the orbs
        var orbsSize = game?.getHearts()?.size!!;
        for (i in 0 until orbsSize) {
            if (game?.getHearts()?.get(i)?.isInGame!!) game!!.getHearts().get(i).draw(canvas)
        }

        var enemySize = game?.getEnemies()?.size!!;
        for (i in 0 until enemySize) {
            if (game?.getEnemies()?.get(i)?.isInGame!!) game!!.getEnemies().get(i).draw(canvas)
        }
        super.onDraw(canvas)
    }
}