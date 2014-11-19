package subsystems

import components.GraphicComponent
import org.jsfml.graphics.RenderTarget

class GraphicSubsystem : Subsystem<GraphicComponent>() {
    public fun draw(deltaTime: Float, target: RenderTarget): Unit {
        components.forEach { it.draw(deltaTime, target) }
        synchronize()
    }
}