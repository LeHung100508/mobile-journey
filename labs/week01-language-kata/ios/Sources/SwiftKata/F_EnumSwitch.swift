//
//  F_EnumSwitch.swift
//  SwiftKata
//
//  Created by Le Hung on 07/10/2026.
//

enum AppNotification {
    case message(from: String, text: String)
    case friendRequest(from: String)
    case systemAlert(level: Int)
}

func title(for notification: AppNotification) -> String {
    return switch notification {
    case .message(_, let text) where text.count > 20:
        String(text.prefix(20)) + "…"
    case .message(_, let text):
        text
    case .friendRequest:
        "example message"
    case .systemAlert(let level) where level >= 3:
        "⚠️ Cảnh báo khẩn (mức \(level))"
    case .systemAlert:
        "Thông báo hệ thống"
    }
}

func runNotificationTests() {
    let testCases:
        [(notification: AppNotification, expected: String, description: String)] =
            [
                // 1. Message > 20 ký tự
                (
                    notification: .message(
                        from: "Hưng",
                        text: "Xin chào bạn, hôm nay trời đẹp quá đi!"
                    ),
                    expected: "Xin chào bạn, hôm na…",
                    description:
                        "Message dài > 20 ký tự (bị cắt ngắn và thêm ...)"
                ),

                // 2. Message <= 20 ký tự
                (
                    notification: .message(from: "Lan", text: "Alo bạn ơi!"),
                    expected: "Alo bạn ơi!",
                    description: "Message ngắn <= 20 ký tự (giữ nguyên)"
                ),

                // 3. Friend request
                (
                    notification: .friendRequest(from: "Nam"),
                    expected: "example message",
                    description: "Lời mời kết bạn"
                ),

                // 4. System alert >= 3
                (
                    notification: .systemAlert(level: 4),
                    expected: "⚠️ Cảnh báo khẩn (mức 4)",
                    description: "Cảnh báo hệ thống cấp độ cao (>= 3)"
                ),

                // 5. System alert < 3
                (
                    notification: .systemAlert(level: 1),
                    expected: "Thông báo hệ thống",
                    description: "Cảnh báo hệ thống cấp độ thấp (< 3)"
                ),
            ]

    print("--- BẮT ĐẦU CHẠY TEST ---")
    for (index, tc) in testCases.enumerated() {
        let result = title(for: tc.notification)
        let isPassed = (result == tc.expected)
        let status = isPassed ? "✅ PASS" : "❌ FAIL"

        print("\(status) [Case \(index + 1)] \(tc.description)")
        print("   -> Output:   \"\(result)\"")
        if !isPassed {
            print("   -> Expected: \"\(tc.expected)\"")
        }
    }
}

enum TrafficLight: String, CaseIterable {
    case red = "Đỏ"
    case green = "Xanh"
    case yellow = "Vàng"

    func next() -> TrafficLight {
        // TODO: switch self, không default
        return switch self {
        case .red: .green
        case .green: .yellow
        case .yellow: .red
        }
    }

    var durationSeconds: Int {
        // TODO: switch self, không default
        return switch self {
        case .red: 30
        case .green: 25
        case .yellow: 3
        }
    }
}

func runGroupF() {
    print("===========RunGroupF===========")
    print("F1")
    runNotificationTests()

    print("F2")
    TrafficLight.allCases.forEach { traffic in
        print(
            "\(traffic.rawValue) → \(traffic.durationSeconds) → Next traffic: \(traffic.next().rawValue)"
        )
    }

    let f3Result = (1...15).map { n in
        switch (n % 3, n % 5) {
        case (0,0): "FizzBuzz"
        case (0, _): "Fizz"
        case (_, 0): "Buzz"
        default: String(n)
        }
    }
    print("F3: \(f3Result)")

    print("===========EndGroupF===========")
}
