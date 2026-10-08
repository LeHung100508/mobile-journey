package dev.mobilejourney.expense

sealed class ExpenseError(message: String) : Exception(message) {
    class NegativeAmount(val amount: Money) : ExpenseError("Số tiền không được âm: $amount")
    class EmptyParticipants : ExpenseError("Danh sách người tham gia không được rỗng")
    class PercentagesNotHundred(val actual: Int) :
        ExpenseError("Tổng tỉ lệ phần trăm phải bằng 100%, thực tế: $actual%")

    class ExactAmountsMismatch(val expected: Money, val actual: Money) :
        ExpenseError("Tổng tiền không khớp: mong đợi $expected, thực tế $actual")

    class PayerNotInGroup(val payer: Person) :
        ExpenseError("Người trả tiền ${payer.name} không nằm trong nhóm")
}