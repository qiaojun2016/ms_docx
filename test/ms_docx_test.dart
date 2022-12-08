import 'package:flutter_test/flutter_test.dart';
import 'package:ms_docx/ms_docx.dart';
import 'package:ms_docx/ms_docx_platform_interface.dart';
import 'package:ms_docx/ms_docx_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMsDocxPlatform 
    with MockPlatformInterfaceMixin
    implements MsDocxPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<bool> generateWord(String input, String output, Map<String, dynamic> data) async {
    return true;
  }
}

void main() {
  final MsDocxPlatform initialPlatform = MsDocxPlatform.instance;

  test('$MethodChannelMsDocx is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMsDocx>());
  });

  test('getPlatformVersion', () async {
    MsDocx msDocxPlugin = MsDocx();
    MockMsDocxPlatform fakePlatform = MockMsDocxPlatform();
    MsDocxPlatform.instance = fakePlatform;
  
    expect(await msDocxPlugin.getPlatformVersion(), '42');
  });
}
