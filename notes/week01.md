# Tuần 01 — Kotlin ↔ Swift cơ bản

## Concept đã học

| Concept | Kotlin / Android | Swift / iOS | Giống | Khác |
|---|---|---|---|---|
| Hằng / biến | `val` / `var` | `let` / `var` | Không gán lại được | `let` với value type (`struct`, `Array`…) khóa luôn cả các thuộc tính `var` bên trong. `val` chỉ khóa tham chiếu, object bên trong vẫn sửa được. |
| Nullable / Optional | `T?`, `?.`, `?:`, `!!`, `?.let {}` | `T?`, `?.`, `??`, `!`, `if let` / `guard let` | Null-safety kiểm tra lúc compile, cú pháp gần giống nhau | `Optional` trong Swift là một `enum` thật (`.some(T)` / `.none`), nên lồng được `T??` và `switch` được. Nhét Optional vào string interpolation sẽ bị warning và in ra `Optional(...)`. |
| Unwrap sớm | `?: return` | `guard let … else` | Early exit, giữ code phẳng | Khối `else` của `guard` **bắt buộc** phải thoát scope (`return`/`throw`/`break`/`continue`). `guard` gộp được nhiều điều kiện bằng dấu `,`. Kotlin dùng smart cast sau `?: return`. |
| Tham số hàm | named args, default args | argument label, default args | Gọi hàm tự giải thích, có giá trị mặc định | Label trong Swift **bắt buộc** viết khi gọi và **không đổi được thứ tự**. Một tham số có 2 tên (label bên ngoài, tên bên trong); dùng `_` để bỏ label. Named args trong Kotlin là tùy chọn và đổi thứ tự được. |
| Lambda / closure | `{ x -> … }`, `it` | `{ x in … }`, `$0`, `$1` | Trailing lambda/closure, capture được biến bên ngoài | Closure là reference type, có capture list `[weak self]` để tránh retain cycle. Swift có **nhiều trailing closure** (`{ … } onError: { … }`). |
| DSL | lambda with receiver `T.() -> R` | result builder (Tuần 2–3) | Cú pháp khai báo dạng cây (Compose ↔ SwiftUI) | Kotlin đổi `this` bên trong lambda thành receiver. Swift để **compiler biến đổi** các biểu thức rời rạc thành một giá trị qua `buildBlock`. |
| Mở rộng type | extension function / property | `extension` | Thêm hàm vào type có sẵn mà không cần kế thừa | Cả hai đều dispatch tĩnh: chọn hàm theo kiểu khai báo, không theo kiểu thật lúc chạy. `extension` của Swift còn thêm được `init`, cho type conform protocol, và có ràng buộc (`where Element: Equatable`). |
| Model giá trị | `data class` + `copy()` | `struct` | Model dữ liệu gọn, tự sinh constructor | `b = a`: **struct copy** (độc lập), **data class dùng chung tham chiếu**. Sửa struct bằng `mutating func`. Swift không tự sinh `==`/`hash` mà phải conform `Equatable`/`Hashable`. |
| Sum type | `sealed interface` | `enum` + associated value | Exhaustive, compiler bắt thiếu case | `enum` của Swift là value type gọn nhẹ, dữ liệu gắn trực tiếp vào case. `sealed` của Kotlin là một cây class (mỗi case là một class/object). |
| Rẽ nhánh | `when` | `switch` | Là expression, exhaustive | `switch` match được tuple `(a, b)`, `_`, range `7..<9`, có `where` để thêm điều kiện. Bắt buộc exhaustive nên thường cần `default`. Không fallthrough ngầm. Kotlin có `when { }` không đối số. |
| Collection | `List` / `MutableList`, `Map`, `Set` | `[T]`, `[K: V]`, `Set<T>` | `map`/`filter`/`reduce`… gần giống hệt nhau | Swift: collection là value type, **gán là copy** (copy-on-write); mutable hay không là do `let`/`var`. `Dictionary` **không giữ thứ tự** (`mapOf` của Kotlin thì giữ thứ tự thêm vào). |
| Thuộc tính suy ra | `val x get() = …` | computed `var x: T { … }` | Không lưu trữ, tính lại mỗi lần đọc | Swift luôn dùng `var` (kể cả read-only), có thể bỏ `return`, có `get`/`set` với `newValue`. |
| Theo dõi thay đổi | custom setter, `field` | `willSet` / `didSet` | Phản ứng khi giá trị bị gán | Setter của Kotlin chạy **trước** khi lưu (chặn/sửa được qua `field`). `didSet` chạy **sau** khi lưu, có `oldValue`. Gán lại chính nó trong `didSet` không gọi lại `didSet`. **Không chạy trong `init`** (giá trị khởi tạo `= 0` của Kotlin cũng không đi qua setter). |
| Thư viện thuần | Gradle JVM module | Swift Package | Logic độc lập với UI, chạy và test bằng CLI | SPM có sẵn trong toolchain, cấu hình bằng chính Swift (`Package.swift`). Dùng `swift run` / `swift test` thay cho `./gradlew run` / `./gradlew test`. |

---

## Ví dụ đặt cạnh nhau

### 1. Hằng / biến — `val` vs `let` với value type

```kotlin
data class Point(var x: Int, var y: Int)

val p = Point(1, 2)
p.x = 10          // ✅ OK: val chỉ khóa tham chiếu
// p = Point(0, 0) // ❌ không gán lại được
```

```swift
struct Point { var x: Int; var y: Int }

let p = Point(x: 1, y: 2)
// p.x = 10        // ❌ Cannot assign to property: 'p' is a 'let' constant
var q = Point(x: 1, y: 2)
q.x = 10           // ✅ OK
```

**Nhận xét:** Với value type, `let` khóa cả giá trị bên trong chứ không riêng gì tên biến. Muốn sửa thuộc tính thì biến phải là `var`.

### 2. Nullable / Optional — Optional là một enum

```kotlin
val port: Int? = "8080".toIntOrNull()
val length = name?.length ?: 0
port?.let { println("Port $it") }
```

```swift
let port: Int? = Int("8080")
let length = name?.count ?? 0
if let port { print("Port \(port)") }

// Optional thực chất là enum, switch được:
switch port {
case .some(let p): print("Có port \(p)")
case .none:        print("Không có")
}
```

**Nhận xét:** Cú pháp hai bên gần giống nhau, nhưng `Int?` của Swift chính là `Optional<Int>`, một enum thật. Vì vậy in nó ra sẽ thấy `Optional(8080)` và compiler sẽ cảnh báo.

### 3. Unwrap sớm — `?: return` vs `guard let`

```kotlin
fun checkout(cart: Cart?, customer: Customer?): String {
    val c = cart ?: return "Không có giỏ"
    if (c.items.isEmpty()) return "Giỏ trống"
    val cus = customer ?: return "Thiếu khách hàng"
    return "OK: ${cus.name}, ${c.items.size} món"
}
```

```swift
func checkout(cart: Cart?, customer: Customer?) -> String {
    guard let cart else { return "Không có giỏ" }
    guard !cart.items.isEmpty else { return "Giỏ trống" }
    guard let customer else { return "Thiếu khách hàng" }
    return "OK: \(customer.name), \(cart.items.count) món"
}
```

**Nhận xét:** `guard cart != nil` chỉ kiểm tra mà **không unwrap**, phải dùng `guard let`. Biến unwrap bằng `guard let` dùng được ở toàn bộ phần code bên dưới.

### 4. Tham số hàm — named args vs argument label

```kotlin
fun transfer(amount: Int, from: String, to: String) { /* ... */ }

transfer(100, "Hưng", "Ánh")
transfer(to = "Ánh", from = "Hưng", amount = 100)   // ✅ đổi thứ tự được
```

```swift
func transfer(amount: Int, from sender: String, to recipient: String) { /* ... */ }

transfer(amount: 100, from: "Hưng", to: "Ánh")      // ✅
// transfer(from: "Hưng", to: "Ánh", amount: 100)   // ❌ sai thứ tự
// transfer(100, "Hưng", "Ánh")                      // ❌ thiếu label

func parsePort(_ text: String) -> Int? { Int(text) } // `_` = không cần label
parsePort("8080")
```

**Nhận xét:** Label là một phần của tên hàm trong Swift (`transfer(amount:from:to:)`), nên bắt buộc viết và phải đúng thứ tự. Tên bên trong (`sender`) có thể khác label bên ngoài (`from`).

### 5. Lambda / closure — `it` vs `$0`, nhiều trailing closure

```kotlin
val names = listOf("An", "Bình", "Chi")
val upper = names.map { it.uppercase() }
val long = names.filter { name -> name.length > 2 }

var count = 0
val inc = { count++ }   // capture biến bên ngoài
inc(); inc()
println(count)          // 2
```

```swift
let names = ["An", "Bình", "Chi"]
let upper = names.map { $0.uppercased() }
let long = names.filter { name in name.count > 2 }

var count = 0
let inc = { count += 1 } // capture biến bên ngoài (theo tham chiếu)
inc(); inc()
print(count)             // 2

// Nhiều trailing closure:
func load(onSuccess: (String) -> Void, onError: (String) -> Void) { onSuccess("data") }
load { data in print(data) } onError: { err in print(err) }
```

**Nhận xét:** Tham số trong Kotlin viết `x ->`, trong Swift viết `x in`. Swift cho phép viết liền nhiều trailing closure có label, điều mà Kotlin không có.

### 6. DSL — lambda with receiver vs result builder

```kotlin
fun shoppingList(block: MutableList<String>.() -> Unit): List<String> =
    mutableListOf<String>().apply(block)

val list = shoppingList {
    add("Táo")       // `this` là MutableList<String>
    add("Cam")
}
```

```swift
@resultBuilder
struct ListBuilder {
    static func buildBlock(_ items: String...) -> [String] { items }
}

func shoppingList(@ListBuilder _ content: () -> [String]) -> [String] { content() }

let list = shoppingList {
    "Táo"            // compiler gom các biểu thức lại qua buildBlock
    "Cam"
}
```

**Nhận xét:** Kotlin dựng DSL bằng cách đổi `this` bên trong lambda. Swift thì để compiler viết lại các dòng biểu thức thành một lời gọi `buildBlock(...)`. Đây chính là nền tảng của SwiftUI (`@ViewBuilder`).

### 7. Mở rộng type — extension và dispatch tĩnh

```kotlin
fun String.isValidPort(): Boolean = toIntOrNull()?.let { it in 1..65535 } ?: false

open class Animal
class Dog : Animal()
fun Animal.sound() = "..."
fun Dog.sound() = "Gâu"

val a: Animal = Dog()
println(a.sound())   // "..." — chọn theo kiểu khai báo
```

```swift
extension String {
    var isValidPort: Bool { Int(self).map { (1...65535).contains($0) } ?? false }
}

protocol Animal {}
extension Animal { func sound() -> String { "..." } }
struct Dog: Animal { func sound() -> String { "Gâu" } }

let a: any Animal = Dog()
print(a.sound())     // "..." — vì sound() không được khai báo trong protocol
```

**Nhận xét:** Cả hai đều dispatch tĩnh. Ở Swift, nếu khai báo `sound()` trong `protocol Animal` thì nó thành requirement và dispatch động (in ra "Gâu").

### 8. Model giá trị — `b = a` thì sao?

```kotlin
data class User(val name: String, var age: Int)

val u1 = User("Hưng", 25)
val u2 = u1          // cùng một object
u2.age = 26
println(u1.age)      // 26 ❗

val u3 = u1.copy(age = 30)  // muốn độc lập thì copy()
```

```swift
struct User { var name: String; var age: Int }

var u1 = User(name: "Hưng", age: 25)
var u2 = u1          // bản sao độc lập
u2.age = 26
print(u1.age)        // 25 ✅

extension User {
    mutating func birthday() { age += 1 }  // sửa chính nó phải là mutating
}
```

**Nhận xét:** Struct được copy khi gán nên không có chuyện "sửa chỗ này, chỗ khác cũng đổi theo". Data class muốn an toàn thì nên để mọi thuộc tính là `val` và dùng `copy()`.

### 9. Sum type — `sealed interface` vs `enum` + associated value

```kotlin
sealed interface AppNotification {
    data class Message(val from: String, val text: String) : AppNotification
    data class FriendRequest(val from: String) : AppNotification
    data class SystemAlert(val level: Int) : AppNotification
}

fun title(n: AppNotification): String = when (n) {
    is AppNotification.Message ->
        if (n.text.length > 20) n.text.take(20) + "…" else n.text
    is AppNotification.FriendRequest -> "${n.from} muốn kết bạn"
    is AppNotification.SystemAlert ->
        if (n.level >= 3) "⚠️ Khẩn (mức ${n.level})" else "Thông báo hệ thống"
}
```

```swift
enum AppNotification {
    case message(from: String, text: String)
    case friendRequest(from: String)
    case systemAlert(level: Int)
}

func title(for n: AppNotification) -> String {
    switch n {
    case .message(_, let text) where text.count > 20: String(text.prefix(20)) + "…"
    case .message(_, let text):                        text
    case .friendRequest(let from):                     "\(from) muốn kết bạn"
    case .systemAlert(let level) where level >= 3:     "⚠️ Khẩn (mức \(level))"
    case .systemAlert:                                 "Thông báo hệ thống"
    }
}
```

**Nhận xét:** Swift khai báo cả 3 case chỉ trong 3 dòng, và `where` giúp tách điều kiện ra thành case riêng. Kotlin cần một class cho mỗi case và phải `if` bên trong nhánh.

### 10. Rẽ nhánh — `when` vs `switch` với tuple

```kotlin
val result = (1..15).map { n ->
    when {
        n % 15 == 0 -> "FizzBuzz"
        n % 3 == 0  -> "Fizz"
        n % 5 == 0  -> "Buzz"
        else        -> "$n"
    }
}
```

```swift
let result = (1...15).map { n -> String in
    switch (n % 3, n % 5) {
    case (0, 0): "FizzBuzz"
    case (0, _): "Fizz"
    case (_, 0): "Buzz"
    default:     "\(n)"
    }
}
```

**Nhận xét:** `switch` của Swift match theo **mẫu** (tuple, `_`, range, `where`) chứ không phải theo biểu thức boolean. Lưu ý range dùng ngoặc tròn `(1...15)`, còn `[1...15]` là một mảng chứa đúng 1 range.

### 11. Collection — gán là copy, Dictionary không có thứ tự

```kotlin
val a = mutableListOf(1, 2)
val b = a
b.add(3)
println(a)            // [1, 2, 3] ❗ cùng tham chiếu

val scores = mapOf("Bình" to 7, "An" to 9)
println(scores)       // {Bình=7, An=9} — giữ thứ tự thêm vào
```

```swift
var a = [1, 2]
var b = a
b.append(3)
print(a)              // [1, 2] ✅ copy-on-write

let scores = ["Bình": 7, "An": 9]
print(scores)         // thứ tự không xác định
for (name, score) in scores.sorted(by: { $0.key < $1.key }) {
    print(name, score)
}
print(scores["Dũng", default: 0])   // 0 — subscript trả về Optional nếu không có default
```

**Nhận xét:** Collection của Swift là value type nên gán là copy (chỉ copy thật khi có thay đổi). Muốn in `Dictionary` theo thứ tự thì phải tự `sorted`.

### 12. Thuộc tính suy ra — getter vs computed property

```kotlin
data class CartItem(val name: String, val price: Double, val quantity: Int) {
    val subtotal: Double get() = price * quantity
}

class ShoppingCart(val items: List<CartItem>) {
    val total get() = items.sumOf { it.subtotal }
    val mostExpensive get() = items.maxByOrNull { it.subtotal }
}
```

```swift
struct CartItem {
    var name: String
    var price: Double
    var quantity: Int
    var subtotal: Double { price * Double(quantity) }
}

struct ShoppingCart {
    var items: [CartItem]
    var total: Double { items.reduce(0) { $0 + $1.subtotal } }
    var mostExpensive: CartItem? { items.max { $0.subtotal < $1.subtotal } }
}
```

**Nhận xét:** Computed property trong Swift luôn khai báo bằng `var`, không dùng `= …` (vì như vậy sẽ thành stored property chỉ tính một lần). Swift phải tự ép `Double(quantity)` vì không có chuyển kiểu ngầm định.

### 13. Theo dõi thay đổi — custom setter vs `didSet`

```kotlin
class Volume(initial: Int) {
    var level: Int = initial          // ❗ không đi qua setter
        set(value) {
            field = value.coerceIn(0, 100)   // chạy TRƯỚC khi lưu
        }
}
```

```swift
struct Volume {
    var level: Int {                  // init không gọi didSet
        didSet {                      // chạy SAU khi lưu, có oldValue
            level = min(max(level, 0), 100)  // không gọi lại didSet
        }
    }
}

var v = Volume(level: 150)
print(v.level)   // 150 ❗ vì didSet không chạy trong init
v.level = 150
print(v.level)   // 100
```

**Nhận xét:** Muốn clamp cả lúc khởi tạo thì phải tự viết `init` hoặc dùng property wrapper. Kotlin cũng có cái bẫy tương tự: giá trị khởi tạo được gán thẳng vào `field`.

### 14. Thư viện thuần — Gradle JVM module vs Swift Package

```kotlin
// build.gradle.kts
plugins {
    kotlin("jvm") version "2.0.21"
    application
}
dependencies { testImplementation(kotlin("test")) }
application { mainClass.set("MainKt") }
// Chạy: ./gradlew run   |   Test: ./gradlew test
```

```swift
// Package.swift
// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "SwiftKata",
    targets: [
        .executableTarget(name: "SwiftKata"),
        .testTarget(name: "SwiftKataTests", dependencies: ["SwiftKata"]),
    ]
)
// Chạy: swift run   |   Test: swift test
```

**Nhận xét:** SPM được cấu hình bằng chính Swift và có sẵn trong toolchain, không cần cài thêm build tool. Quy ước thư mục `Sources/<Target>` và `Tests/<Target>` thay cho `src/main` và `src/test`.

---

## Điều bất ngờ

1. **`let` với struct khóa luôn cả thuộc tính bên trong.** Quen với Kotlin nên tưởng `let` chỉ giống `val` (khóa tham chiếu). Giờ hiểu là struct là một giá trị nguyên khối: sửa một thuộc tính nghĩa là thay cả giá trị, nên `let` chặn luôn.
2. **`return number` trong hàm `-> Int?` vẫn ra `Optional(8080)`.** Tưởng trả về `Int` thì nhận được `Int`. Thực ra kiểu trả về là cố định lúc compile: compiler tự bọc `number` thành `.some(number)`, và bên gọi phải tự unwrap.
3. **`didSet` không chạy trong `init`, và gán lại chính nó trong `didSet` không bị đệ quy.** Hai luật này giúp clamp giá trị trong `didSet` an toàn, nhưng cũng có nghĩa là giá trị truyền vào lúc khởi tạo không được kiểm tra.
4. **`Dictionary` in ra mỗi lần một thứ tự.** Kotlin `mapOf` giữ thứ tự thêm vào nên không để ý. Swift `Dictionary` là hash table thuần túy, cần thứ tự thì phải `sorted`.
5. **`split` trả về `[Substring]` chứ không phải `[String]`.** Swift tối ưu bằng cách dùng chung bộ nhớ với chuỗi gốc. Cần `String(...)` hoặc `.map(String.init)` khi muốn lưu lâu hoặc trả về.

---

## Lỗi đã gặp và cách sửa

### Lỗi 1: Còn sót placeholder của IDE
- **Thông báo:** `editor placeholder in source file`
- **Nguyên nhân:** Autocomplete sinh ra `<#T##Int#>` trong `distance(from: 12912, to: <#T##Int#>)` mà chưa điền giá trị.
- **Cách sửa:** Thay placeholder bằng giá trị thật: `distance(from: 12912, to: 20000)`.

### Lỗi 2: In Optional trong string interpolation
- **Thông báo:** `warning: string interpolation produces a debug description for an optional value; did you mean to make this explicit?`
- **Nguyên nhân:** `print("B1: \(parsePort("8080"))")` truyền thẳng một `Int?` vào chuỗi.
- **Cách sửa:** Unwrap bằng `?? -1`, hoặc `?.description ?? "nil"`, hoặc ghi rõ ý định bằng `String(describing:)`.

### Lỗi 3: Dùng range trong ngoặc vuông
- **Thông báo:** `Cannot convert value of type 'ClosedRange<Int>' to expected argument type 'Int'`
- **Nguyên nhân:** `[1...15].map { n in … }` tạo ra một **mảng có 1 phần tử là range**, nên `n` có kiểu `ClosedRange<Int>`.
- **Cách sửa:** Dùng ngoặc tròn `(1...15).map { … }`, và match bằng tuple `switch (n % 3, n % 5)`.

### Lỗi 4: Compiler không suy ra được kiểu trả về của closure
- **Thông báo:** `Generic parameter 'ElementOfResult' could not be inferred`
- **Nguyên nhân:** Closure của `compactMap` có nhiều câu lệnh, vừa có `return nil` vừa trả về `Substring`, nên compiler không suy ra được kiểu kết quả.
- **Cách sửa:** Ghi rõ kiểu: `compactMap { item -> (name: String, score: Double)? in … }` và chuyển `String(parts[0])`.

### Lỗi 5: Dùng từ khóa làm tên tham số closure
- **Thông báo:** `Cannot find 'index' in scope` (lỗi cú pháp quanh `case in`)
- **Nguyên nhân:** `forEach { index, case in … }`, mà `case` là từ khóa của Swift.
- **Cách sửa:** Đổi tên (`item`, `testCase`) hoặc bọc trong backtick `` `case` ``.

### Lỗi 6: Kiểm tra `!= nil` nhưng không unwrap
- **Thông báo:** `Value of optional type 'Cart?' must be unwrapped to refer to member 'items'`
- **Nguyên nhân:** `guard cart != nil` chỉ kiểm tra điều kiện, sau đó `cart` vẫn là `Cart?`.
- **Cách sửa:** `guard let cart else { return "Không có giỏ" }`.

### Lỗi 7 (logic): `guard` bị ngược điều kiện
- **Triệu chứng:** `mostExpensive` luôn trả về `nil`.
- **Nguyên nhân:** `guard isEmpty else { return nil }` sẽ thoát khi giỏ **có** hàng.
- **Cách sửa:** Bỏ `guard`, vì `items.max(by:)` đã tự trả về `nil` khi mảng rỗng.

---

## Trả lời câu hỏi tự kiểm tra

### Câu 1. `struct` và `class` khác nhau thế nào? Khi nào dùng cái nào?

**Giống:** đều có stored/computed property, method, `init`, `extension`, và conform được protocol.

**Khác:**

| | `struct` | `class` |
|---|---|---|
| Kiểu | Value type: gán/truyền là copy | Reference type: gán/truyền là chia sẻ |
| Kế thừa | Không | Có (kế thừa đơn) |
| `init` | Tự sinh memberwise init | Phải tự viết nếu thuộc tính chưa có giá trị mặc định |
| Sửa chính nó trong method | Phải đánh dấu `mutating` | Không cần |
| `let` | Khóa toàn bộ giá trị | Chỉ khóa tham chiếu, thuộc tính `var` vẫn sửa được |
| Khác | — | Có `deinit`, so sánh danh tính bằng `===` |

```swift
struct PointS { var x = 0 }
class  PointC { var x = 0 }

var s1 = PointS(); var s2 = s1; s2.x = 5   // s1.x == 0
let c1 = PointC(); let c2 = c1; c2.x = 5   // c1.x == 5
```

**Khi nào dùng:** mặc định dùng `struct` cho model và dữ liệu. Dùng `class` khi cần **danh tính chung** (nhiều nơi cùng thấy một trạng thái), cần kế thừa (ví dụ `UIViewController`), cần `deinit`, hoặc bị framework yêu cầu (`ObservableObject`).

**So với Kotlin:** `data class` giống `struct` về mục đích nhưng vẫn là reference type. Trên Android, `struct` tương đương với một `data class` mà mọi thuộc tính đều là `val` và luôn sửa qua `copy()`.

### Câu 2. `if let` và `guard let` khác nhau thế nào? Vì sao `guard` dễ đọc hơn?

- `if let x = opt { … }`: `x` chỉ dùng được **bên trong** khối `if`. Hợp khi có hai nhánh logic ngang hàng.
- `guard let x = opt else { return }`: `x` dùng được **ở toàn bộ phần bên dưới**, còn khối `else` bắt buộc phải thoát scope.

```swift
// if let: lồng nhiều tầng
if let cart {
    if !cart.items.isEmpty {
        if let customer { /* xử lý chính */ }
    }
}

// guard: phẳng, xử lý lỗi ở đầu, luồng chính ở cuối
guard let cart else { return "Không có giỏ" }
guard !cart.items.isEmpty else { return "Giỏ trống" }
guard let customer else { return "Thiếu khách hàng" }
// xử lý chính
```

`guard` dễ đọc hơn vì các điều kiện tiên quyết nằm hết ở đầu hàm, luồng chính không bị thụt lề, và compiler đảm bảo không thể "lọt" qua khi điều kiện sai. Nó tương đương với `val c = cart ?: return …` trong Kotlin.

### Câu 3. Lambda with receiver là gì? Vì sao `Row { }` gọi được `Modifier.weight()` còn bên ngoài thì không?

Lambda with receiver có kiểu `T.() -> R`: bên trong lambda, `this` chính là một đối tượng `T`, nên gọi được các member của `T` (kể cả member extension) mà không cần tiền tố.

```kotlin
inline fun Row(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit   // receiver là RowScope
)

interface RowScope {
    fun Modifier.weight(weight: Float, fill: Boolean = true): Modifier  // member extension
}
```

`weight` là một **extension được khai báo bên trong interface `RowScope`**, nên chỉ gọi được khi đang có một `RowScope` làm `this`. Trong `Row { … }`, `this` là `RowScope`, vì vậy `Modifier.weight(1f)` resolve được. Ra ngoài thì không có receiver đó nên compiler báo lỗi. Cách làm này giới hạn API theo đúng ngữ cảnh: `weight` chỉ có nghĩa với con của `Row`/`Column`. SwiftUI không có receiver như vậy mà dùng result builder và modifier tổng quát (`.frame(maxWidth: .infinity)`).

### Câu 4. Argument label dùng để làm gì?

Mỗi tham số có hai tên: **argument label** (dùng khi gọi) và **parameter name** (dùng trong thân hàm).

```swift
func insert(_ element: String, at index: Int, into array: inout [String]) {
    array.insert(element, at: index)
}

var fruits = ["Táo", "Cam"]
insert("Dưa", at: 1, into: &fruits)   // đọc như một câu: "insert Dưa at 1 into fruits"
```

- Làm cho lời gọi hàm đọc như câu tiếng Anh, tự giải thích mà không cần xem khai báo.
- Bên ngoài dùng giới từ ngắn (`from:`, `to:`, `at:`), bên trong vẫn có tên biến rõ nghĩa (`sender`, `index`).
- Label là một phần của tên hàm (`insert(_:at:into:)`), nên có thể overload theo label: `distance(from:to:)` và `distance(to:)` là hai hàm khác nhau.
- `_` bỏ label khi tên hàm đã đủ rõ: `parsePort("8080")`, `abs(-5)`.

Khác với Kotlin: label **bắt buộc** viết và **không đổi được thứ tự**, còn named args của Kotlin là tùy chọn.

## Link commit
- T1 Kotlin kata: https://github.com/LeHung100508/mobile-journey/commit/2b7b83f7cfa092f8dcc486b5d37bf3d0c8c6f321
- T2 Swift kata: https://github.com/LeHung100508/mobile-journey/commit/770c277f0721201f3fa1dd4052c6a74e6474c245
- T4 P0 Kotlin: (cuối tuần)
- T5 P0 Swift: (cuối tuần)
