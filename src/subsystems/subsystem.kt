package subsystems

import components.Component
import java.util.ArrayList

abstract class Subsystem<in T : Component> {
    protected val components: ArrayList<T> = ArrayList()
    private val componentsToAdd = ArrayList<T>()
    private val componentsToRemove = ArrayList<T>()

    public fun register(component: T): Unit {
        componentsToAdd.add(component)
    }

    public fun unregister(component: T): Unit {
        componentsToRemove.add(component)
    }

    protected fun synchronize() {
        components.removeAll(componentsToRemove)
        components.addAll(componentsToAdd)
        componentsToRemove.clear()
        componentsToAdd.clear()
    }
}