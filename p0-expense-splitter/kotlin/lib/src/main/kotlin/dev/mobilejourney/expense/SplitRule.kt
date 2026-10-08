package dev.mobilejourney.expense

sealed interface SplitRule {
    val participants: Set<Person>

    fun summary(): String =
        when (this) {
            is Equal -> {
                val names = participants.joinToString { it.name }
                "Chia đều cho ${participants.size} người"
            }

            is ExactAmounts -> {
                val details = amounts.entries.joinToString { "${it.key.name}: ${it.value}" }
                "Theo số tiền: $details"
            }

            is Percentages -> {
                val details = percents.entries.joinToString { "${it.key.name}: ${it.value}%" }
                "Chia t% $details"
            }
        }

    data class Equal(
        override val participants: Set<Person>
    ) : SplitRule {
//        init {
//            require(participants.isNotEmpty()) { "Danh sách người tham gia không được rỗng" }
//        }
    }

    data class ExactAmounts(
        val amounts: Map<Person, Money>
    ) : SplitRule {
        override val participants: Set<Person>
            get() = amounts.keys

//        init {
//            require(amounts.isNotEmpty()) { "Danh sách không được rỗng" }
//            require(amounts.values.none() { it.isNegative }) { "Số tiền của từng người không được âm" }
//        }
    }

    data class Percentages(
        val percents: Map<Person, Int>
    ) : SplitRule {
        override val participants: Set<Person>
            get() = percents.keys

//        init {
//            require(percents.isNotEmpty()) { "Danh sách không được rỗng" }
//            require(percents.values.all { it in 0..100 }) {
//                "Từng phần trăm phải nằm trong khoảng từ 0 đến 100"
//            }
//            require(percents.values.sum() == 100) {
//                "Tổng phần trăm phải bằng 100%, hiện tại ${percents.values.sum()}%"
//            }
//        }
    }

}