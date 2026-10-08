package dev.mobilejourney.expense

@JvmInline
value class Money(val minorUnits: Long): Comparable<Money> {
    companion object {
        val ZERO = Money(0)
    }
    val isNegative get() = minorUnits < 0

    override fun compareTo(other: Money): Int {
        return minorUnits.compareTo(other.minorUnits)
    }

    operator fun plus(other: Money): Money {
        return Money(minorUnits + other.minorUnits)
    }

    operator fun minus(other: Money): Money {
        return Money(minorUnits - other.minorUnits)
    }

    operator fun unaryMinus(): Money {
        return Money(-minorUnits)
    }

    override fun toString(): String = minorUnits.toString()
}

fun Iterable<Money>.total(): Money = fold(Money.ZERO) { acc, step ->
    Money(acc.minorUnits + step.minorUnits)
}
