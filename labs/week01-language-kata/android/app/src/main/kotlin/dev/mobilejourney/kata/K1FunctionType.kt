package dev.mobilejourney.kata

fun <T> measure(label: String = "block", block: () -> T): T {
    val start = System.nanoTime()
    val result = block()
    println("[$label] ${(System.nanoTime() - start) / 1_000} µs")
    return result
}

fun <A, B, C> compose(f: (A) -> B, g: (B) -> C): (A) -> C {
    val func: (A) -> C = { a -> g(f.invoke(a)) }
    return func
}

fun <T> List<T>.applyEach(vararg transforms: (T) -> T): List<T> {
    val list = this.map { item -> transforms.fold(item) { acc, step -> step(acc) } }
    return list
}

fun runK1() {
    println(listOf(1, 2).applyEach({ it + 1 }, { it * 10 }))
    println(compose<String, String, String>({ it.trim() }, { it.uppercase() })("  hi "))
    println(measure("sum") { (1..1000).sum() })
}