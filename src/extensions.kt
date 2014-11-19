package fanorona

import org.jsfml.system.*

fun Vector2i.div(other: Vector2i): Vector2i {
    return Vector2i(this.x / other.x, this.y / other.y)
}

fun Vector2f.div(other: Vector2f): Vector2f {
    return Vector2f(this.x / other.x, this.y / other.y)
}

fun Vector2i.toFloats(): Vector2f {
    return Vector2f(this.x.toFloat(), this.y.toFloat())
}