//
//  D_Closures.swift
//  SwiftKata
//
//  Created by Le Hung on 05/10/2026.
//

func makeAccumulator(start: Int) -> (Int) -> Int {
    var acc = start
    return {
        acc = acc + $0
        return acc
    }
}

func runGroupD() {
    let d1 = ["Chi", "An", "Bình"]

    print("===========RunGroupD===========")
    print("D1:")
    print(d1.sorted(by: { (a: String, b: String) -> Bool in return a > b }))  // đầy đủ
    print(d1.sorted(by: { a, b in a > b }))  // bỏ kiểu và return
    print(d1.sorted(by: { $0 > $1 }))  // $0, $1
    print(d1.sorted(by: >))  // truyền thẳng toán tử

    let d2 = ["An:9", "Bình:7", "Chi:8.5", "lỗi", "Dũng:10"]
    let d2Result = d2.compactMap { item -> (name: String, score: Double)? in
        let parts = item.split(separator: ":")

        guard parts.count == 2,
            let score = Double(parts[1]),
            score >= 8
        else {
            return nil
        }

        return (name: String(parts[0]), score: score)
    }.sorted { $0.score > $1.score }.map { $0.name }

    print("D2: \(d2Result)")

    let a = makeAccumulator(start: 0)
    let b = a
    print("D3:", a(10), b(5), a(1))
    // Dự đoán: 10, 15, 16
    // Thực tế: 10, 15, 16
    print("===========EndGroupD===========")
    
}
