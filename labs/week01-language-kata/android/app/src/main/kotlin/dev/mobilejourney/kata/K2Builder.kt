package dev.mobilejourney.kata

fun <T> myBuildList(builder: MutableList<T>.() -> Unit): List<T> {
    val list = mutableListOf<T>()
    list.builder()
    return list.toList()
}

fun myBuildString(builder: StringBuilder.() -> Unit): String {
    val str = StringBuilder()
    str.builder()
    return str.toString()
}

fun <T> T.myApply(block: T.() -> Unit): T {
    block()
    return this
}

fun runK2() {
    println(myBuildList {
        add("x")
        add("y")
    })
    println(myBuildString {
        append("run")
        append("K2")
    })
    println(mutableListOf<Int>().myApply {
        add(1)
        add(2)
        add(3)
    })
}