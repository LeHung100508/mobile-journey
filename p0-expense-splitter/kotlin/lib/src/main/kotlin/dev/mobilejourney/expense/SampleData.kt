package dev.mobilejourney.expense

object SampleData {
    val an = Person(
        id = "p1",
        name = "An"
    )

    val binh = Person(
        id = "p2",
        name = "Bình"
    )

    val chi = Person(
        id = "p3",
        name = "Chi"
    )

    val e1 = Expense(
        id = "e1",
        title = "Khách sạn",
        amount = Money(3_000_000),
        paidBy = an,
        rule = SplitRule.Equal(
            participants = setOf(an, binh, chi)
        )
    )

    val e2 = Expense(
        id = "e2",
        title = "Ăn tối",
        amount = Money(900_000),
        paidBy = binh,
        rule = SplitRule.ExactAmounts(
            amounts = mapOf(
                an to Money(400_000),
                binh to Money(300_000),
                chi to Money(200_000)
            )
        )
    )

    val e3 = Expense(
        id = "e3",
        title = "Thuê xe",
        amount = Money(1000_000),
        paidBy = chi,
        rule = SplitRule.Percentages(
            percents = mapOf(
                an to 50,
                binh to 30,
                chi to 20
            )
        )
    )

    val group = ExpenseGroup(
        name = "Đà Lạt 2026",
        members = listOf(an, binh, chi),
        expenses = listOf(e1, e2, e3)
    )
}
