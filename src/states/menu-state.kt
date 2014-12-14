package states

import org.jsfml.graphics.*
import org.jsfml.system.Vector2i
import fanorona.*
import utilities.getText
import org.jsfml.window.Mouse
import utilities.drawTextCentered

abstract class MenuState(private val window: RenderWindow, private val view: ConstView) : GameState() {
    class MenuItem(public val name: String, public val action: () -> Unit, public val y: Int, public var hover: Boolean = false) {
    }

    protected abstract val menuItems: Iterable<MenuItem>

    private var mouseWasDown = false
    override fun update(deltaTime: Float) {
        val cursorPos = window.mapPixelToCoords(Mouse.getPosition(window), view)
        for (item in menuItems)
            item.hover = cursorPos.y > item.y && cursorPos.y < item.y + 10

        if (Mouse.isButtonPressed(Mouse.Button.LEFT))
            mouseWasDown = true
        else if (mouseWasDown) {
            mouseWasDown = false
            //menuItems.singleOrNull({ it.hover })?.action() // strange runtime error
            val selectedItem = menuItems singleOrNull { it.hover }
            if (selectedItem != null)
                selectedItem.action()
        }
    }

    private val color = Color(128, 128, 128)
    override fun draw(deltaTime: Float, target: RenderTarget) {
        target.clear(Color.WHITE)

        drawTextCentered("A Game Called Fanorona", Vector2i(screenWidth / 2, 50), Color.BLACK, target)
        drawTextCentered("by Rob Kellett", Vector2i(screenWidth / 2, 70), color, target)

        for (item in menuItems) {
            drawTextCentered(item.name, Vector2i(screenWidth / 2, item.y), if (item.hover) Color.BLUE else color, target)
        }
    }
}