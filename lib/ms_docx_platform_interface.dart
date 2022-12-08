import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'ms_docx_method_channel.dart';

abstract class MsDocxPlatform extends PlatformInterface {
  /// Constructs a MsDocxPlatform.
  MsDocxPlatform() : super(token: _token);

  static final Object _token = Object();

  static MsDocxPlatform _instance = MethodChannelMsDocx();

  /// The default instance of [MsDocxPlatform] to use.
  ///
  /// Defaults to [MethodChannelMsDocx].
  static MsDocxPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MsDocxPlatform] when
  /// they register themselves.
  static set instance(MsDocxPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<bool?> generateWord(
      String input, String output, Map<String, dynamic> data) async {
    throw UnimplementedError('generateWord() has not been implemented.');
  }
}
