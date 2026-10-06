//
//  B_Optionals.swift
//  SwiftKata
//
//  Created by Le Hung on 05/10/2026.
//

func parsePort(_ text: String) -> Int? {
    let range = 1...65535
    guard let number = Int(text), range.contains(number) else {
        return nil
    }
    return number
}

func displayName(nickname: String?, fullName: String?) -> String {
    return nickname ?? fullName ?? "Khách"
}

struct Customer {
    var name: String
    var email: String?
}
struct Cart { var items: [String] }

func checkout(cart: Cart?, customer: Customer?) -> String {
    guard let cart else {
        return "Không có giỏ"
    }

    guard !cart.items.isEmpty else {
        return "Giỏ trống"
    }

    guard let customer else {
        return "Chưa đăng nhập"
    }

    guard let email = customer.email else {
        return "Thiếu email"
    }

    return "Gửi hóa đơn \(cart.items.count) món tới \(email)"
}

struct Address { var city: String }
struct Person {
    var name: String
    var address: Address?
}
struct Company {
    var name: String
    var ceo: Person?
}

func runGroupB() {
    let p1 = parsePort("8080")?.description ?? "nil"
    let p2 = parsePort("abc")?.description ?? "nil"
    let p3 = parsePort("65536")?.description ?? "nil"

    let casesB2: [(nickname: String?, fullName: String?)] = [
        ("Tí", "Lê Hưng"), (nil, "Lê Hưng"), ("Tí", nil), (nil, nil),
    ]
    let cart = Cart(items: ["Macbook", "Tai nghe"])
    let customer = Customer(
        name: "Lê Hưng",
        email: "hungle@example.com"
    )

    let company = Company(
        name: "VNPT Lỏ",
        ceo: Person(name: "Lê Hưng", address: Address(city: "TP.HCM"))
    )

    let full: Company? = Company(name: "Example", ceo: Person(name: "Lê Hưng", address: Address(city: "TP.HCM"))) // đủ cả chuỗi
    let noCompany: Company? = nil  // company nil
    let noCeo: Company? = Company(name: "A", ceo: nil)  // ceo nil
    let noAddress: Company? = Company(
        name: "B",
        ceo: Person(name: "C", address: nil)
    )  // address
    
    //Dự đoán kiểu: String?

    print("===========RunGroupB===========")
    print("B1: \(p1), \(p2), \(p3)")
    casesB2.enumerated().forEach { index, c in
        print("B2 case\(index): \(displayName(nickname: c.nickname, fullName: c.fullName))")
    }
    print("B3: \(checkout(cart: cart, customer: customer))")
    print(
        "B4: \(full?.ceo?.address?.city ?? "Không rõ"), \(noCompany?.ceo?.address?.city ?? "Không rõ"), \(noCeo?.ceo?.address?.city ?? "Không rõ"), \(noAddress?.ceo?.address?.city ?? "Không rõ")"
    )
    print("===========EndGroupB===========")
    
    //Thực tế: String?
}
