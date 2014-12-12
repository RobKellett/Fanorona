package components

import subsystems.UpdateSubsystem

class SimpleUpdateComponent(subsystem: UpdateSubsystem, updateFunction: (Float) -> Unit) : UpdateComponent(subsystem) {
    private val updateFunction = updateFunction
    override fun update(deltaTime: Float) = updateFunction(deltaTime)
}