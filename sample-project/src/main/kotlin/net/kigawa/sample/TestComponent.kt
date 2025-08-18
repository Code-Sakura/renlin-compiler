package net.kigawa.sample

// テスト用のコンポーネントクラス
class TestComponent {
    fun render(@AutoFill key: String? = null, content: String) {
        println("TestComponent.render called with key: '$key', content: '$content'")
    }
    
    fun process(@AutoFill id: String?, @AutoFill name: String?, value: Int) {
        println("TestComponent.process called with id: '$id', name: '$name', value: $value")
    }
}

// 通常の関数でもアノテーション付きパラメータを使用可能
fun createItem(@AutoFill itemId: String?, @AutoFill title: String = "none") {
    println("createItem called with itemId: '$itemId', title: '$title'")
}