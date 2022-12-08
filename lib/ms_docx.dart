import 'ms_docx_platform_interface.dart';

class MsDocx {
  Future<String?> getPlatformVersion() {
    return MsDocxPlatform.instance.getPlatformVersion();
  }

  Future<bool?> generate(
      String inputPath, String outputPath, Map<String, dynamic> data) async {
    return MsDocxPlatform.instance.generateWord(inputPath, outputPath, data);
  }
}
