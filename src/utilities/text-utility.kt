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

fun getText(string: String, position: Vector2i): Text {
    val text = Text(string, font)
    text.setColor(Color.BLACK)
    text.setCharacterSize(16)
    text.setPosition(position.toFloats())
    return text;
}

fun drawText(string: String, position: Vector2i, target: RenderTarget) = target.draw(getText(string, position))

fun drawTextCentered(string: String, position: Vector2i, color: Color, target: RenderTarget) {
    val text = getText(string, position)
    text.setColor(color)
    val bounds = text.getLocalBounds()
    text.setOrigin(Math.floor(bounds.width / 2.0).toFloat(), Math.floor(bounds.height / 2.0).toFloat())
    target.draw(text)
}