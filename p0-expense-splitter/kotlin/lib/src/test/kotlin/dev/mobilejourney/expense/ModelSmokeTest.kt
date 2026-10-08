package dev.mobilejourney.expense

import kotlin.test.Test
import kotlin.test.assertEquals

class ModelSmokeTest {

    // 1. Kiểm tra totalSpent của nhóm mẫu bằng Money(4_900_000)
    @Test
    fun totalSpentOfSampleGroupEqualsFourMillionNineHundred() {
        val expected = Money(4_900_000)
        val actual = SampleData.group.totalSpent
        assertEquals(expected = expected, actual = actual)
    }

    // 2. Kiểm tra tính bất biến (immutability) của ExpenseGroup
    @Test
    fun addingExpensePreservesOriginalGroupExpensesCount() {
        val extraExpense = Expense(
            id = "e4",
            title = "Cà phê sáng",
            amount = Money(150_000),
            paidBy = SampleData.an,
            rule = SplitRule.Equal(
                participants = setOf(SampleData.an, SampleData.binh)
            )
        )

        val updatedGroup = SampleData.group.adding(extraExpense)

        assertEquals(expected = 3, actual = SampleData.group.expenses.size)
        assertEquals(expected = 4, actual = updatedGroup.expenses.size)
    }

    // 3. Case 1: Chia đều (SplitRule.Equal - e1)
    @Test
    fun equalSplitRuleMatchesExpectations() {
        val rule = SampleData.e1.rule as SplitRule.Equal
        val expectedSummary = "Chia đều cho 3 người"
        assertEquals(expected = expectedSummary, actual = rule.summary())

        val expectedParticipants = setOf(SampleData.an, SampleData.binh, SampleData.chi)
        assertEquals(expected = expectedParticipants, actual = rule.participants)
    }

    // 4. Case 2: Chia số tiền chính xác (SplitRule.ExactAmounts - e2)
    @Test
    fun exactAmountsSplitRuleMatchesExpectations() {
        val rule = SampleData.e2.rule as SplitRule.ExactAmounts
        val expectedSummary = "Theo số tiền: An: 400000, Bình: 300000, Chi: 200000"
        assertEquals(expected = expectedSummary, actual = rule.summary())

        val expectedParticipants = setOf(SampleData.an, SampleData.binh, SampleData.chi)
        assertEquals(expected = expectedParticipants, actual = rule.participants)

        // Kiểm tra chi tiết số tiền từng người phải trả
        assertEquals(expected = Money(400_000), actual = rule.amounts[SampleData.an])
        assertEquals(expected = Money(300_000), actual = rule.amounts[SampleData.binh])
        assertEquals(expected = Money(200_000), actual = rule.amounts[SampleData.chi])
        assertEquals(expected = SampleData.e2.amount, actual = rule.amounts.values.total())
    }

    // 5. Case 3: Chia theo phần trăm (SplitRule.Percentages - e3)
    @Test
    fun percentagesSplitRuleMatchesExpectations() {
        val rule = SampleData.e3.rule as SplitRule.Percentages
        val expectedSummary = "Chia t% An: 50%, Bình: 30%, Chi: 20%"
        assertEquals(expected = expectedSummary, actual = rule.summary())

        val expectedParticipants = setOf(SampleData.an, SampleData.binh, SampleData.chi)
        assertEquals(expected = expectedParticipants, actual = rule.participants)

        // Kiểm tra chi tiết tỷ lệ % từng người
        assertEquals(expected = 50, actual = rule.percents[SampleData.an])
        assertEquals(expected = 30, actual = rule.percents[SampleData.binh])
        assertEquals(expected = 20, actual = rule.percents[SampleData.chi])
    }
}
