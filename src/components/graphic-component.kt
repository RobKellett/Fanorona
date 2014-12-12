package components

import subsystems.GraphicSubsystem
import org.jsfml.graphics.RenderTarget

abstract class GraphicComponent(subsystem: GraphicSubsystem) : Component {
    {
        subsystem.register(this)
    }

    public abstract fun draw(deltaTime: Float, target: RenderTarget)
}