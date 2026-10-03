package dev.mobilejourney.kata

interface Modifier {
    val elements: List<String>

    fun then(element: String): Modifier {
        val newElements = this.elements + element
        return object : Modifier {
            override val elements: List<String>
                get() = newElements

            override fun toString(): String {
                return "Modifier$elements"
            }
        }
    }

    companion object : Modifier {
        override val elements: List<String>
            get() = listOf()

        override fun toString(): String {
            return "Modifier"
        }
    }
}

fun Modifier.padding(dp: Int): Modifier {
    return this.then("padding($dp)")
}

interface FakeColumnScope {
    fun Modifier.weight(weight: Float): Modifier {
        require(weight > 0) { "weight phải > 0" }
        return this.then("weight($weight)")
    }
}

private object FakeColumnScopeImpl : FakeColumnScope

fun fakeColumn(modifier: Modifier = Modifier, content: FakeColumnScope.() -> Unit) {
    println("Column($modifier) {")
    FakeColumnScopeImpl.content()
    println("}")
}

fun fakeText(text: String, modifier: Modifier = Modifier) {
    println("  Text(\"$text\", $modifier)")
}

fun runK3() {
    fakeColumn(Modifier.padding(16)) {
        fakeText("Header")
        fakeText("Body", Modifier.weight(1f))
    }
//    Modifier.weight(1f)
//    Unresolved reference 'weight'.
}