package fanorona

import org.jsfml.internal.SFMLNative
import org.jsfml.graphics.*
import org.jsfml.window.*
import org.jsfml.window.event.Event

import java.util.Date

import states.GameplayState
import org.jsfml.system.Vector2i

fun main(args: Array<String>) {
    SFMLNative.loadNativeLibraries()

    val window = RenderWindow(VideoMode(768, 720), "A Game Called Fanorona")
    window.setFramerateLimit(60)

    utilities.initialize()

    var gameState = GameplayState()

    val targetTexture = RenderTexture()
    targetTexture.create(256, 240)

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

        gameState.update(deltaTime)

        targetTexture.clear(Color.WHITE)
        gameState.draw(deltaTime, targetTexture)
        utilities.drawText("${Math.round(1f / deltaTime)} FPS", Vector2i(4, 0), targetTexture)
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