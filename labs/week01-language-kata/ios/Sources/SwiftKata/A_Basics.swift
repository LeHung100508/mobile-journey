//
//  A_Basics.swift
//  SwiftKata
//
//  Created by Le Hung on 05/10/2026.
//

func runGroupA() {
    let name = "Máy tính"
    var stock = 150
    let price = 15.5
    
    // Tính giá trị kho
    let stockValue = Double(stock) * price
    
    // Tính trung bình [4, 5, 3, 5]
    let list = [4, 5, 3, 5]
    
    let average = (Double(list.reduce(0, +)) / Double(list.count))
//    name = "Tủ lạnh"
//    Lỗi compile: `- error: cannot assign to value: 'name' is a 'let' constant
    
    print("===========RunGroupA===========")
    print("A1: \(stockValue)")
    print("A2: \(average)")
    print("===========EndGroupA===========")
}
