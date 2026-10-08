package dev.mobilejourney.expense

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
            paidBy = SampleData.p1,
            rule = SplitRule.Equal(
                participants = setOf(SampleData.p1, SampleData.p2)
            )
        )

        val updatedGroup = SampleData.group.adding(extraExpense)

        assertEquals(expected = 3, actual = SampleData.group.expenses.size)
        assertEquals(expected = 4, actual = updatedGroup.expenses.size)
    }

    // 3. Case 1: Chia đều (SplitRule.Equal - e1)
    @Test
    fun equalSplitRuleMatchesExpectations() {
        val expectedSummary = "Chia đều cho 3 người: An, Bình, Cường"
        assertEquals(expected = expectedSummary, actual = SampleData.e1.rule.summary())

        val expectedParticipants = setOf(SampleData.p1, SampleData.p2, SampleData.p3)
        assertEquals(expected = expectedParticipants, actual = SampleData.e1.rule.participants)
    }

    // 4. Case 2: Chia theo phần trăm (SplitRule.Percentages - e2)
    @Test
    fun percentagesSplitRuleMatchesExpectations() {
        val expectedSummary = "Chia theo phần trăm An: 50%, Bình: 30%, Cường: 20%"
        assertEquals(expected = expectedSummary, actual = SampleData.e2.rule.summary())

        val expectedParticipants = setOf(SampleData.p1, SampleData.p2, SampleData.p3)
        assertEquals(expected = expectedParticipants, actual = SampleData.e2.rule.participants)
    }

    // 5. Case 3: Chia số tiền chính xác (SplitRule.ExactAmounts - e3)
    @Test
    fun exactAmountsSplitRuleMatchesExpectations() {
        val expectedSummary = "Chia chính xác An: 200000, Bình: 100000, Cường: 100000 - Tổng: 400000"
        assertEquals(expected = expectedSummary, actual = SampleData.e3.rule.summary())

        val expectedParticipants = setOf(SampleData.p1, SampleData.p2, SampleData.p3)
        assertEquals(expected = expectedParticipants, actual = SampleData.e3.rule.participants)
    }

    // 6. Kiểm tra các ràng buộc validation của từng SplitRule khi dữ liệu không hợp lệ
    @Test
    fun splitRulesValidateInvalidInputs() {
        // Equal không cho phép rỗng
        assertFailsWith<IllegalArgumentException> {
            SplitRule.Equal(participants = emptySet())
        }

        // Percentages tổng không bằng 100%
        assertFailsWith<IllegalArgumentException> {
            SplitRule.Percentages(percents = mapOf(SampleData.p1 to 50, SampleData.p2 to 40))
        }

        // ExactAmounts có số tiền âm
        assertFailsWith<IllegalArgumentException> {
            SplitRule.ExactAmounts(
                amounts = mapOf(
                    SampleData.p1 to Money(100_000),
                    SampleData.p2 to Money(-50_000)
                )
            )
        }
    }
}
