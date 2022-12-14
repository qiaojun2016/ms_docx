package com.github.qiaojun2016.ms_docx

import android.content.Context
import android.os.Handler
import android.os.Looper

import com.github.qiaojun2016.ms_docx.DocxUtil.generateWord

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.FileNotFoundException
import java.io.IOException


const val GENERATE_WORD = "generateWord"

/** MsDocxPlugin */
class MsDocxPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var applicationContext: Context;

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "ms_docx")
        channel.setMethodCallHandler(this)
        applicationContext = flutterPluginBinding.applicationContext;
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            GENERATE_WORD -> {
                val input = call.argument<String>("input");
                val output = call.argument<String>("output");
                val data = call.argument<Map<String, Any>>("data");
                println(data)
                println(input)
                println(output)
                Thread {
                    try {
                        generateWord(applicationContext, input!!, output!!, LinkedHashMap(data!!))
                        Handler(Looper.getMainLooper()).post {
                            result.success(true)
                        }
                    } catch (e: FileNotFoundException) {
                        Handler(Looper.getMainLooper()).post {
                            result.error("104", "template not found path = $input", null)
                        }
                    } catch (e2: UnsupportedOperationException) {
                        Handler(Looper.getMainLooper()).post {
                            result.error("105", e2.message, e2.message);
                        }
                    } catch (e3: IOException) {
                        Handler(Looper.getMainLooper()).post {
                            result.error("102", e3.message, e3.message);
                        }
                    }
                }.start()
            }
            else -> result.notImplemented()
        }
    }


    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
