package states

import org.jsfml.graphics.*
import states.MenuState.MenuItem
import fanorona.changeState

class MainMenuState(window: RenderWindow, view: ConstView) : MenuState(window, view) {
    override val menuItems: Iterable<MenuState.MenuItem> = listOf(
        MenuItem("Human vs. Human",     { changeState(BoardMenuState(window, view, PlayerType.HUMAN,   PlayerType.HUMAN))   }, 100),
        MenuItem("Human vs. Machine",   { changeState(BoardMenuState(window, view, PlayerType.HUMAN,   PlayerType.MACHINE)) }, 120),
        MenuItem("Machine vs. Human",   { changeState(BoardMenuState(window, view, PlayerType.MACHINE, PlayerType.HUMAN))   }, 140),
        MenuItem("Machine vs. Machine", { changeState(BoardMenuState(window, view, PlayerType.MACHINE, PlayerType.MACHINE)) }, 160)
    )
}