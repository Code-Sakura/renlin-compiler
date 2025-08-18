# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

これは特定のアノテーションがついた引数の値を自動生成するKotlin Compiler Plugin (KCP)プロジェクトです。GradleプラグインとKotlinコンパイラプラグインが連携して、指定されたアノテーションが付いた関数内で、`@AutoFill`アノテーションが付いたパラメータの値を自動的に生成・注入します。

## アーキテクチャ

プロジェクトは3つのメインモジュールから構成されています：

1. **ルートモジュール** (`/src/main/kotlin/Main.kt`): シンプルなアプリケーションエントリーポイント
2. **Gradleプラグイン** (`/gradle-plugin/`): Kotlinビルドプロセスと統合するGradleプラグインインターフェース
3. **Kotlinプラグイン** (`/kotlin-plugin/`): 実際のコンパイラIR変換ロジック

### 主要コンポーネント

- **MyPlugin.kt**: コンパイラプラグインを登録し設定を処理するメインGradleプラグイン
- **MyPluginExtension.kt**: Gradleプラグインの設定DSL（enabledフラグとアノテーションリスト）
- **MyCompilerRegistrar.kt**: IR生成拡張をKotlinコンパイラに登録
- **MyCommandLineProcessor.kt**: Gradleからコンパイラへのコマンドラインオプションを処理
- **MyIrGenerationExtension.kt**: 指定されたアノテーション内で`@AutoFill`アノテーション付きパラメータの値を自動生成するコアIR変換ロジック

### プラグインフロー

1. Gradleプラグインが適用され、拡張設定を作成
2. 設定がコマンドラインオプション経由でKotlinコンパイラに渡される
3. コンパイラレジストラーがIR生成拡張を有効化
4. IR拡張が指定されたアノテーションが付いた関数内で`@AutoFill`アノテーション付きパラメータを検出し、自動値を生成・注入

## 開発コマンド

### ビルド
```bash
./gradlew build
```

### クリーンビルド
```bash
./gradlew clean build
```

### ローカルMavenに公開
```bash
./gradlew publishToMavenLocal
```

### メインアプリケーション実行
```bash
./gradlew run
```

### テスト実行
```bash
./gradlew test
```

### 特定モジュールのビルド
```bash
./gradlew :gradle-plugin:build
./gradlew :kotlin-plugin:build
```

## 設定

プラグインはbuild.gradle.ktsで以下の設定オプションをサポートします：

```kotlin
myPlugin {
    enabled = true
    annotations = listOf("com.example.HogeAnnotation")
}
```

- `enabled`: プラグインの有効/無効を切り替えるブールフラグ
- `annotations`: 対象とするアノテーションのFQNリスト（指定されたアノテーションが付いた関数内でのみ変換が実行される）

## 変換対象

プラグインは指定されたアノテーションが付いた関数内で、`@AutoFill`アノテーションが付いたパラメータでnull値が渡された場合に自動的に値を生成・注入します。自動生成される値は`"auto-parameterName-count"`形式です。

### 使用例

```kotlin
// パラメータにアノテーションを付ける
fun render(@AutoFill key: String?, content: String) {
    println("render called with key: '$key', content: '$content'")
}

@UseAutoKey  // 関数レベルのアノテーション（プラグインが有効になる条件）
fun example() {
    render(null, "Hello")  // keyパラメータに自動的に "auto-key-1" が注入される
    render(null, "World")  // keyパラメータに自動的に "auto-key-2" が注入される
}
```

## 依存関係

- Kotlin 1.9.0
- Kotlin Compiler Embeddable
- アノテーション処理用のAuto Service
- Gradle Plugin API

## 注意事項

- Java 8ツールチェーンを使用
- グループID `net.kigawa` でアーティファクトを公開
- プラグインIDは `net.kigawa.renlin-compiler`
- Kotlinコードスタイルは公式規約に従う
- アノテーションが指定されていない場合、すべての`@AutoFill`アノテーション付きパラメータが変換対象となる

## サンプルプロジェクト

`/sample-project/` ディレクトリにプラグインの使用例があります。

### サンプルプロジェクトの実行方法

1. まずプラグインをローカルMavenに公開：
```bash
./gradlew :gradle-plugin:publishToMavenLocal :kotlin-plugin:publishToMavenLocal
```

2. サンプルプロジェクトをビルド・実行：
```bash
cd sample-project
../gradlew run
```

注意: 現在sample-projectはマルチプロジェクトから独立したプロジェクトとして設定されています。

### サンプルプロジェクトの構成

- `UseAutoKey.kt`: プラグインが対象とする関数レベルのアノテーション定義
- `AutoFill.kt`: パラメータレベルのアノテーション定義（自動値注入の対象を指定）
- `TestComponent.kt`: `@AutoFill`アノテーション付きパラメータを持つサンプルクラス
- `Main.kt`: アノテーション付きの関数とそうでない関数のサンプル
- アノテーション付きの関数内で`@AutoFill`パラメータに自動値が注入される動作を確認可能