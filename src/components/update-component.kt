package components

import subsystems.UpdateSubsystem

abstract class UpdateComponent(subsystem: UpdateSubsystem) : Component {
    {
        subsystem.register(this)
    }

    public abstract fun update(deltaTime: Float)
}