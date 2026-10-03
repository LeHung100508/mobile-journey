package dev.mobilejourney.kata

data class Todo(
    val id: Int,
    val title: String,
    val done: Boolean = false
)

sealed interface Command {
    data class Add(val title: String) : Command
    data class Toggle(val id: Int) : Command
    data class Rename(val id: Int, val newTitle: String) : Command
    data class Remove(val id: Int) : Command
    data object ClearDone : Command
}

sealed interface ParseResult {
    data class OK(val command: Command) : ParseResult
    data class Error(val reason: String) : ParseResult
}

fun parse(input: String): ParseResult {
    val splits = input.trim().split(" ", limit = 2)
    when (val command = splits.firstOrNull()) {
        "add" -> {
            if (splits.size < 2) {
                return ParseResult.Error("Không có title")
            }
            val title = splits[1]
            return ParseResult.OK(Command.Add(title))
        }

        "toggle" -> {
            if (splits.size < 2) {
                return ParseResult.Error("Không nhập id")
            }
            val id = splits[1].toIntOrNull() ?: return ParseResult.Error("id không hợp lệ: '${splits[1]}'")
            return ParseResult.OK(Command.Toggle(id))
        }

        "rename" -> {
            val rest = splits.getOrNull(1)?.split(" ", limit = 2).orEmpty()
            val id = rest.getOrNull(0)?.toIntOrNull()
                ?: return ParseResult.Error("id không hợp lệ: '${rest.getOrNull(0).orEmpty()}'")
            val newTitle = rest.getOrNull(1)?.takeIf { it.isNotBlank() }
                ?: return ParseResult.Error("Thiếu title cho rename")
            return ParseResult.OK(Command.Rename(id, newTitle))
        }

        "remove" -> {
            if (splits.size < 2) {
                return ParseResult.Error("Không nhập id")
            }
            val id = splits[1].toIntOrNull() ?: return ParseResult.Error("id không hợp lệ: '${splits[1]}'")
            return ParseResult.OK(Command.Remove(id))
        }

        "clear" -> {
            return ParseResult.OK(Command.ClearDone)
        }

        else -> {
            return ParseResult.Error("Lệnh không hỗ trợ: '$command'")
        }
    }
}

fun List<Todo>.applyCommand(command: Command): List<Todo> {
    return when (command) {
        is Command.Add -> {
            this + Todo(
                id = (maxOfOrNull { it.id } ?: 0) + 1,
                title = command.title
            )
        }

        is Command.Rename -> map { if (it.id == command.id) it.copy(title = command.newTitle) else it }
        is Command.Toggle -> map { if (it.id == command.id) it.copy(done = !it.done) else it }
        is Command.Remove -> filterNot { it.id == command.id }
        Command.ClearDone -> filterNot { it.done }
    }
}

fun runK5() {
    var todoList = listOf<Todo>()
    val commandList = listOf("add Mua sữa", "add Học Swift", "toggle 1", "rename 2 Học Swift cơ bản", "remove x", "fly", "clear")
    commandList.forEach {
        when(val result = parse(it)) {
            is ParseResult.OK -> {
                todoList = todoList.applyCommand(result.command)
            }

            is ParseResult.Error -> {
                println(result.reason)
            }
        }
    }
    println(todoList)
}