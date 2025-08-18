package net.kigawa.sample

fun main() {
    println("=== Sample Project for KCP Demo ===")

    // アノテーション付きの関数を呼び出し
    testAutoFillParameters()

    // 通常の関数（アノテーションなし）
    testWithoutAnnotation()
}

fun testAutoFillParameters() {
    println("\n--- testAutoFillParameters() function ---")
    println("この関数内の@AutoFillアノテーション付きパラメータには自動的に値が注入されます")

    val component = TestComponent()

    // @AutoFillアノテーション付きパラメータに自動値が注入される
    component.render(content = "Hello World")
    createItem(null)
}

fun testWithoutAnnotation() {
    println("\n--- testWithoutAnnotation() function ---")
    println("この関数はアノテーションがないため、プラグインの対象外です")

    val component = TestComponent()

    // この呼び出しはプラグインの対象外なので、nullのまま
    component.render(null, "No auto-fill")

    // 手動で値を指定
    component.render("manual-key", "Manual key specified")
}