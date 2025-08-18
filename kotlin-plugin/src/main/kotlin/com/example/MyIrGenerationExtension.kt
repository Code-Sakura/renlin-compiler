package com.example

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.name.FqName
import java.util.*

class MyIrGenerationExtension(
    private val annotations: List<String> = listOf("com.example.HogeAnnotation"),
    private val messageCollector: MessageCollector? = null,
): IrGenerationExtension {

    private fun log(message: String) {
        messageCollector?.report(CompilerMessageSeverity.WARNING, "IR_PLUGIN: $message")
        System.err.println("IR_PLUGIN: $message") // fallback
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        log("MyIrGenerationExtension is running!")
        log("Looking for annotations: $annotations")

        moduleFragment.transformChildren(
            object: IrElementTransformerVoidWithContext() {
                private var insideAnnotatedFunction = false

                override fun visitFunctionNew(declaration: IrFunction): IrStatement {
                    val prev = insideAnnotatedFunction

                    // アノテーションの検出をデバッグ
                    val hasTargetAnnotation = if (annotations.isEmpty()) {
                        true
                    } else {
                        annotations.any { annotationFqName ->
                            val hasAnno = declaration.hasAnnotation(FqName(annotationFqName))
                            if (hasAnno) {
                                log("Found annotated function: ${declaration.name} with annotation: $annotationFqName")
                            }
                            hasAnno
                        }
                    }

                    insideAnnotatedFunction = insideAnnotatedFunction || hasTargetAnnotation

                    val result = super.visitFunctionNew(declaration)
                    insideAnnotatedFunction = prev
                    return result
                }

                override fun visitCall(expression: IrCall): IrExpression {
                    val result = super.visitCall(expression) as IrCall

                    // 詳細なデバッグ情報を出力
                    IrDebugHelper.printIrCall(result)

                    // アノテーションが指定されている場合、アノテーション付き関数内でのみ処理
                    if (!insideAnnotatedFunction && annotations.isNotEmpty()) {
                        return result
                    }

                    // アノテーション付きパラメータで値がnullの場合に自動値を生成
                    for (i in 0 until result.valueArgumentsCount) {
                        if (result.getValueArgument(i) == null && hasAutoFillAnnotation(result, i)) {
                            val paramName = result.symbol.owner.valueParameters.getOrNull(i)?.name?.asString()
                                ?: "param$i"
                            val autoValue = generateAutoValue(paramName)

                            log("Injecting auto-generated value '$autoValue' for annotated parameter $i in function: ${result.symbol.owner.fqNameWhenAvailable}")

                            result.putValueArgument(
                                i,
                                IrConstImpl.string(
                                    startOffset = expression.startOffset,
                                    endOffset = expression.endOffset,
                                    type = pluginContext.irBuiltIns.stringType,
                                    value = autoValue
                                )
                            )
                        }
                    }
                    return result
                }

                private fun hasAutoFillAnnotation(call: IrCall, paramIndex: Int): Boolean {
                    val parameter = call.symbol.owner.valueParameters.getOrNull(paramIndex)
                    return parameter?.hasAnnotation(FqName("net.kigawa.sample.AutoFill")) == true ||
                        parameter?.hasAnnotation(FqName("AutoFill")) == true
                }

                private fun generateAutoValue(paramName: String): String {
                    val uuid = UUID.randomUUID().toString()
                    return "auto-$paramName-$uuid"
                }
            },
            null
        )
    }
}

// デバッグ用のヘルパークラス
object IrDebugHelper {

    fun printIrCall(call: IrCall) {
        System.err.println("=== IR Call Debug ===")
        System.err.println("Symbol: ${call.symbol.owner.fqNameWhenAvailable}")
        System.err.println("Function Name: ${call.symbol.owner.name}")
        System.err.println("Parameters: ${call.symbol.owner.valueParameters.map { "${it.name}: ${it.type}" }}")
        System.err.println("Arguments: ${(0 until call.valueArgumentsCount).map { call.getValueArgument(it) }}")
        System.err.println("Dispatch Receiver: ${call.dispatchReceiver?.type}")
        System.err.println("Extension Receiver: ${call.extensionReceiver?.type}")
        System.err.println("====================")
    }
}