package net.kigawa.sample

import net.kigawa.renlin.AutoFill

// テスト用のコンポーネントクラス
class TestComponent {
    fun render(@AutoFill key: String? = null, content: String) {
        println("TestComponent.render called with key: '$key', content: '$content'")
    }

}

// 通常の関数でもアノテーション付きパラメータを使用可能
fun createItem(@AutoFill itemId: String?, @AutoFill title: String = "none") {
    println("createItem called with itemId: '$itemId', title: '$title'")
}
