package subsystems

import components.UpdateComponent

class UpdateSubsystem : Subsystem<UpdateComponent>() {
    public fun update(deltaTime: Float) {
        components.forEach { it.update(deltaTime) }
        synchronize()
    }
}