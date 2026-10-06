//
//  C_Functions.swift
//  SwiftKata
//
//  Created by Le Hung on 05/10/2026.
//

func transfer(amount: Int, from: String, to: String) {
    print("C1: Chuyển \(amount) đồng từ \(from) đến \(to)")
}

func insert(_ element: String, at index: Int, into array: inout [String]) {
    array.insert(element, at: index)
}

func distance(from: Int, to: Int) -> Int {
    return abs(from - to)
}

func joined(_ words: String..., separator: String = " ") -> String {
    return words.joined(separator: separator)
}

func sortPair(_ a: inout Int, _ b: inout Int) {
    guard a > b else {
        return
    }
    let tempA = a
    let tempB = b
    a = tempB
    b = tempA
    
    // Không có cách nào để làm như vậy trong kotlin bởi vì mọi tham số truyền vào đều là bất biến, cách khả thi là trả về một Pair mới (một cặp giá trị mới đã được thay đổi vị trí)
}

func runGroupC() {
    print("===========RunGroupC===========")
    transfer(amount: 100000, from: "Hưng", to: "Ánh")
    var array = ["Táo", "Cam", "Quýt"]
    insert("Dưa hấu", at: 2, into: &array)
    let distance = distance(from: 12912, to: 3421)
    let joinedString = joined("Starting", "to", "learn", "Swift")
    print("C1: \(array) \(distance)")
    print("C2: \(joinedString)")
    var a = 20
    var b = 10
    sortPair(&a, &b)
    print("C3: \(a), \(b)")
    print("===========EndGroupC===========")
}
