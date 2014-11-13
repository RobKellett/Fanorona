package fanorona

import org.jsfml.internal.SFMLNative
import org.jsfml.graphics.*
import org.jsfml.window.*
import org.jsfml.window.event.Event

fun main(args: Array<String>) {
    SFMLNative.loadNativeLibraries()

    val window = RenderWindow(VideoMode(800, 600), "A Game Called Fanorona")
    window.setFramerateLimit(60)

    while (window.isOpen())
    {
        for (event in window.pollEvents()) {
            if (event.type == Event.Type.CLOSED)
                window.close()
        }

        window.clear(Color.BLACK)

        window.display()
    }
}