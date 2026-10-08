package dev.mobilejourney.expense

data class Expense(
    val id: String,
    val title: String,
    val amount: Money,
    val paidBy: Person,
    val rule: SplitRule
)
