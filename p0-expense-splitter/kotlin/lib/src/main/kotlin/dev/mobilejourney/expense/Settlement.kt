package dev.mobilejourney.expense

data class Balance(
    val person: Person,
    val net: Money
)

data class Transfer(
    val from: Person,
    val to: Person,
    val amount: Money
)