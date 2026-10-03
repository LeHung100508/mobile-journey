package dev.mobilejourney.kata

@DslMarker
annotation class MarkdownDsl

@MarkdownDsl
class ListBuilder {
    private val lines = mutableListOf<String>()

    fun item(text: String) {
        lines += "- $text"
    }

    fun build(): String = lines.joinToString("\n")

}

@MarkdownDsl
class ParagraphBuilder {
    private val sb = StringBuilder()

    operator fun String.unaryPlus() {
        sb.append(this)
    }

    fun bold(text: String) {
        sb.append("**").append(text).append("**")
    }

    fun build(): String = sb.toString()
}

@MarkdownDsl
class MarkdownBuilder {
    private val blocks = mutableListOf<String>()

    fun h1(text: String) {
        // TODO: thêm block "# <text>"
        blocks.add("# $text")
    }

    fun paragraph(block: ParagraphBuilder.() -> Unit) {
        // TODO: tạo ParagraphBuilder, chạy block trên nó,
        //       lấy build() rồi thêm vào blocks
        val builder = ParagraphBuilder()
        builder.block()
        blocks.add(builder.build())
    }

    fun bulletList(block: ListBuilder.() -> Unit) {
        // TODO: tương tự paragraph, nhưng với ListBuilder
        val builder = ListBuilder()
        builder.block()
        blocks.add(builder.build())
    }

    fun build(): String = blocks.joinToString("\n\n")

}

fun markdown(block: MarkdownBuilder.() -> Unit): String = MarkdownBuilder().apply { block() }.build()

fun runK4() {
    val text = markdown {
        h1("Tuần 1")
        paragraph {
            +"Học "
            bold("Kotlin")
            +" và Swift"
//            h1("Hello")
//            fun h1(text: String): Unit' cannot be called in this context with an implicit receiver. Use an explicit receiver if necessary.
        }
        bulletList {
            item("lambda")
            item("sealed")
        }
    }
    println(text)
}