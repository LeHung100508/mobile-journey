//
//  G_CollectionsProperties.swift
//  SwiftKata
//
//  Created by Le Hung on 07/10/2026.
//

func frequency(of text: String) -> [Character: Int] {
    var counts: [Character: Int] = [:]

    for char in text.lowercased() where !char.isWhitespace {
        counts[char, default: 0] += 1
    }

    return counts
}

struct CartItem {
    var name: String
    var price: Double
    var quantity: Int
    var subtotal: Double {
        price * Double(quantity)
    }
}

struct ShoppingCart {
    var items: [CartItem]
    var total: Double {
        items.reduce(0.0) { acc, step in
            acc + step.subtotal
        }
    }

    var isEmpty: Bool {
        items.isEmpty
    }

    var mostExpensive: CartItem? {
        items.max { $0.subtotal < $1.subtotal }
    }
}

func runShoppingCartTests() {
    print("--- BẮT ĐẦU TEST CARTITEM & SHOPPINGCART ---")

    // TEST 1: CartItem.subtotal
    let item1 = CartItem(name: "Chuột", price: 50.0, quantity: 2)
    let item2 = CartItem(name: "Bàn phím", price: 120.0, quantity: 1)
    let item3 = CartItem(name: "Màn hình", price: 300.0, quantity: 2) // subtotal = 600.0
    
    assert(item1.subtotal == 100.0, "❌ Lỗi: item1 subtotal phải là 100.0")
    assert(item2.subtotal == 120.0, "❌ Lỗi: item2 subtotal phải là 120.0")
    print("✅ TEST 1: Tính subtotal của CartItem chính xác!")

    // TEST 2: Giỏ hàng RỖNG (Empty Cart)
    let emptyCart = ShoppingCart(items: [])
    
    assert(emptyCart.isEmpty == true, "❌ Lỗi: emptyCart.isEmpty phải là true")
    assert(emptyCart.total == 0.0, "❌ Lỗi: Tổng tiền giỏ rỗng phải là 0.0")
    assert(emptyCart.mostExpensive == nil, "❌ Lỗi: mostExpensive của giỏ rỗng phải là nil")
    print("✅ TEST 2: Kiểm tra giỏ hàng rỗng chính xác!")

    // TEST 3: Giỏ hàng CÓ HÀNG (Normal Cart)
    let cart = ShoppingCart(items: [item1, item2, item3])
    
    assert(cart.isEmpty == false, "❌ Lỗi: cart.isEmpty phải là false")
    assert(cart.total == 820.0, "❌ Lỗi: Tổng tiền phải là 100 + 120 + 600 = 820.0")
    
    if let mostExpensive = cart.mostExpensive {
        assert(mostExpensive.name == "Màn hình", "❌ Lỗi: Món đắt nhất phải là Màn hình")
        assert(mostExpensive.subtotal == 600.0, "❌ Lỗi: Subtotal đắt nhất phải là 600.0")
        print("✅ TEST 3: Tính total và tìm mostExpensive của giỏ hàng chính xác!")
    } else {
        assertionFailure("❌ Lỗi: mostExpensive không được trả về nil khi giỏ có hàng!")
    }

    print("🎉 TẤT CẢ TEST CASES ĐỀU ĐÃ VƯỢT QUA!")
}

struct Volumn {
    var level: Int {
        didSet {
            level = min(max(level, 0), 100)
        }
    }
    init(level: Int) {
        self.level = min(max(level, 0), 100)
    }
}

final class Player {
    private(set) var highScore = 0
    var score = 0 {
        didSet {
            // TODO: nếu score vượt highScore thì cập nhật highScore
            //       và in "Kỷ lục mới: N"
            if (score > highScore) {
                highScore = score
                print("Kỷ lục mới: \(highScore)")
            }
        }
    }
}


func runGroupG() {
    print("===========RunGroupG===========")
    var dict1 = ["A": 1, "B": 2, "C": 3, "D": 4]
    var dict2 = dict1
    dict2["C"] = 20

    let frequencyDict = frequency(of: "Tadaststqqd").sorted(by: <)

    // Lý do phải sắp là vì dictionary không có thứ tự xác định nên mỗi lần in sẽ là một thứ tự khác nhau
    print(
        "G1: \n\(dict1)\n\(dict2)\nfrequency of Tadaststqqd: \(frequencyDict))"
    )
    //    Dự đoán:
    //    ["A": 1, "C": 3, "B": 2, "D": 4]
    //    ["A": 1, "C": 20, "B": 2, "D": 4]
    //    Kết quả thực tế:
    //    ["A": 1, "C": 3, "B": 2, "D": 4]
    //    ["A": 1, "C": 20, "B": 2, "D": 4]
    print("G2:\n\(runShoppingCartTests())")
    
    let a = Volumn(level: 150)
    var v = Volumn(level: 50)
    v.level = 150
    
    print("G3: \(a.level) \(v.level)")
    // Dự đoán 150, 100
    // Kết quả thực tế: 150, 100
    
    var player = Player()
    player.score = 50
    player.score = 40
    player.score = 100
    // player.highScore = 120
    // Lỗi compile: Cannot assign to property: 'highScore' setter is inaccessible
    
    print("===========EndGroupG===========")
}
