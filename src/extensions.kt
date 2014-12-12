package fanorona

import org.jsfml.system.*

public fun Vector2i.div(other: Vector2i): Vector2i {
    return Vector2i(x / other.x, y / other.y)
}

public fun Vector2f.div(other: Vector2f): Vector2f {
    return Vector2f(x / other.x, y / other.y)
}

public fun Vector2f.times(other: Float): Vector2f {
    return Vector2f(x * other, y * other)
}

public fun Vector2i.toFloats(): Vector2f {
    return Vector2f(x.toFloat(), y.toFloat())
}

public val Vector2f.lengthSquared: Float get() = x * x + y * y
public val Vector2f.length: Float get() = Math.sqrt(lengthSquared as Double) as Float