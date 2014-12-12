package utilities

import org.jsfml.graphics.Font
import java.nio.file.Paths
import org.jsfml.system.Vector2i
import org.jsfml.graphics.*
import fanorona.toFloats

private val font = Font()

fun initialize() {
    val thread = Thread.currentThread()
    val loader = thread.getContextClassLoader()
    val resource = loader.getResourceAsStream("font.ttf")
    font.loadFromStream(resource)
    resource.close()
}

fun drawText(string: String, position: Vector2i, target: RenderTarget) {
    val text = Text(string, font)
    text.setColor(Color.BLACK)
    text.setCharacterSize(16)
    text.setPosition(position.toFloats())
    target.draw(text)
}