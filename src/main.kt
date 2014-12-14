package fanorona

import org.jsfml.internal.SFMLNative
import org.jsfml.graphics.*
import org.jsfml.window.*
import org.jsfml.window.event.Event

import java.util.Date

import org.jsfml.system.Vector2i
import states.*

public val screenWidth: Int = 256
public val screenHeight: Int = 240

private var gameState: GameState? = null;

public fun changeState(newState: GameState) {
    gameState!!.kill()
    gameState = newState
}

fun main(args: Array<String>) {
    SFMLNative.loadNativeLibraries()

    val window = RenderWindow(VideoMode(screenWidth * 3, screenHeight * 3), "A Game Called Fanorona")
    window.setFramerateLimit(60)

    utilities.initialize()

    val targetTexture = RenderTexture()
    targetTexture.create(screenWidth, screenHeight)

    gameState = MainMenuState(window, targetTexture.getDefaultView())

    var lastTime = Date()
    while (window.isOpen()) {
        for (event in window.pollEvents()) {
            when (event.type) {
                Event.Type.CLOSED -> window.close()
                Event.Type.RESIZED -> {
                    val newSize = event.asSizeEvent().size
                    window.setView(View(FloatRect(0f, 0f, newSize.x.toFloat(), newSize.y.toFloat())))
                }
            }
        }

        val now = Date()
        val deltaTime = (now.getTime() - lastTime.getTime()) / 1000f

        gameState!!.update(deltaTime)

        gameState!!.draw(deltaTime, targetTexture)
        targetTexture.display()

        val sprite = Sprite(targetTexture.getTexture())
        val sizeComp = window.getSize().toFloats() / targetTexture.getSize().toFloats()
        val scale = Math.min(sizeComp.x, sizeComp.y)
        sprite.setPosition((window.getSize().x - targetTexture.getSize().x * scale) / 2f,
                           (window.getSize().y - targetTexture.getSize().y * scale) / 2f)
        sprite.setScale(scale.toFloat(), scale.toFloat())

        window.clear(Color.BLACK)
        window.draw(sprite)
        window.display()

        lastTime = now
    }
}