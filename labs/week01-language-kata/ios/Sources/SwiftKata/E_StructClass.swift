//
//  E_StructClass.swift
//  SwiftKata
//
//  Created by Le Hung on 07/10/2026.
//

struct PointValue {
    var x: Int
    var y : Int
}

final class PointRef {
    var x: Int
    var y: Int
    
    init(x: Int, y: Int) {
        self.x = x
        self.y = y
    }
}

struct StopWatch {
    private(set) var laps: [Int] = []
    
    mutating func lap(_ ms: Int) {
        laps.append(ms)
    }
}

func addLap(_ item: inout StopWatch, lap: Int) {
    item.lap(lap)
}


func runGroupE() {
    print("===========RunGroupE===========")
    var a = PointValue(x: 1, y: 2)
    var b = a
    b.x = 3
    print("E1: \(a.x), \(b.x)")
    // Dự đoán: 1,3
    // Kết quả thực tế: 1,3
    
    let pointStruct = PointValue(x: 21,y: 50)
    let pointClass = PointRef(x: 12, y: 31)
    
//    pointStruct.y = 2
//    Đây là dòng báo lỗi: cannot assign to property: 'pointStruct' is a 'let' constant
    pointClass.y = 99
    
    print("E1: pointClass \(pointClass.y)")
    
    var varStopWatch = StopWatch(laps: [1,2,3])
    let letStopWatch = StopWatch(laps: [4,5,6])
    
    varStopWatch.lap(4)
//    letStopWatch.lap(7)
//    Dòng này bị lỗi: cannot use mutating member on immutable value: 'letStopWatch' is a 'let' constant
    addLap(&varStopWatch, lap: 10)
//    addLap(&letStopWatch, lap: 11)
//    Dòng này bị lỗi: Cannot pass immutable value as inout argument: 'letStopWatch' is a 'let' constant
    
    print("E3: \(varStopWatch)")
    print("===========EndGroupE===========")
}
