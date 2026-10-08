package dev.mobilejourney.expense

data class ExpenseGroup(
    val name: String,
    val members: List<Person>,
    val expenses: List<Expense> = emptyList(),
) {
    val totalSpent: Money get() = expenses.map { it.amount }.total()

    fun adding(expense: Expense): ExpenseGroup {
        return this.copy(expenses = this.expenses + expense)
    }

    fun expensesPaidBy(person: Person): List<Expense> {
        return expenses.filter { it.paidBy.id == person.id }
    }
}
