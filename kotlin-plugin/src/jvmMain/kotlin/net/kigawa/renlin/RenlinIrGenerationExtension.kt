package net.kigawa.renlin

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

class RenlinIrGenerationExtension(
    private val annotations: List<String> = listOf("com.example.HogeAnnotation"),
    private val messageCollector: MessageCollector? = null,
): IrGenerationExtension {

    private fun log(message: String) {
        messageCollector?.report(CompilerMessageSeverity.WARNING, "IR_PLUGIN: $message")
        System.err.println("IR_PLUGIN: $message") // fallback
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        log("RenlinIrGenerationExtension is running!")
        log("Looking for annotations: $annotations")

        moduleFragment.transformChildren(
            object: IrElementTransformerVoidWithContext() {

                override fun visitCall(expression: IrCall): IrExpression {
                    val result = super.visitCall(expression) as IrCall


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
                    return parameter?.hasAnnotation(FqName("net.kigawa.renlin.AutoFill")) == true
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
