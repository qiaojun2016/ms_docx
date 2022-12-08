import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:ms_docx/ms_docx_method_channel.dart';

void main() {
  MethodChannelMsDocx platform = MethodChannelMsDocx();
  const MethodChannel channel = MethodChannel('ms_docx');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
