package states

import org.jsfml.graphics.RenderTarget
import subsystems.*

abstract class GameState {
    protected val updateSubsystem: UpdateSubsystem = UpdateSubsystem()
    protected val graphicSubsystem: GraphicSubsystem = GraphicSubsystem()

    public open fun update(deltaTime: Float): Unit = updateSubsystem.update(deltaTime)
    public open fun draw(deltaTime: Float, target: RenderTarget): Unit = graphicSubsystem.draw(deltaTime, target)
}