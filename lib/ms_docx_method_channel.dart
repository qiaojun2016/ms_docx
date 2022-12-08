import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'ms_docx_platform_interface.dart';

/// An implementation of [MsDocxPlatform] that uses method channels.
class MethodChannelMsDocx extends MsDocxPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('ms_docx');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<bool?> generateWord(
      String input, String output, Map<String, dynamic> data) async {
    return await methodChannel
        .invokeMethod<bool>('generateWord', <String, dynamic>{
      'input': input,
      'output': output,
      'data': data,
    });
  }
}
