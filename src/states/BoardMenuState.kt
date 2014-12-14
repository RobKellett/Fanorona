package states

import org.jsfml.graphics.*
import states.MenuState.MenuItem
import fanorona.changeState

class BoardMenuState(window: RenderWindow, view: ConstView, whitePlayer: PlayerType, blackPlayer: PlayerType) : MenuState(window, view) {
    override val menuItems: Iterable<MenuState.MenuItem> = listOf(
        MenuItem("3 x 3", { changeState(GameplayState(3, 3, whitePlayer, blackPlayer, window)) }, 100),
        MenuItem("5 x 5", { changeState(GameplayState(5, 5, whitePlayer, blackPlayer, window)) }, 120),
        MenuItem("9 x 5", { changeState(GameplayState(9, 5, whitePlayer, blackPlayer, window)) }, 140),
        MenuItem("18 x 10", { changeState(GameplayState(18, 10, whitePlayer, blackPlayer, window)) }, 160),
        MenuItem("Go back", { changeState(MainMenuState(window, view)) }, 180)
    )
}
