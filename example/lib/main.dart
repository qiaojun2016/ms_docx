import 'dart:collection';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:ms_docx/ms_docx.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _msDocxPlugin = MsDocx();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {}

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: ElevatedButton(
          onPressed: () {
            final rowData = List.generate(7, (index) => index)
                .map((index) =>
                    LinkedHashMap<String, dynamic>.from(<String, dynamic>{
                      'id': '${index + 1}',
                      'number': '${100001 + index}',
                      'name': '大头菜',
                      'place': '山东日照五莲县许孟镇',
                      'booth': '山东美正生物有限公司',
                      'person': '李四',
                      'value': '0.43',
                      'result': '阴性',
                      'date': '2022-08-12'
                    }))
                .toList();
            final contentMap = <String, dynamic>{
              'project': '二氧化硫',
              'department': '山东美正生物快检服务中心',
              'limit': '50%',
              'sendDate': '2022-09-19',
              'sampleType': '蔬菜类，豆类',
              'method': '分光度法',
              'list': rowData.map((e) => e.values.toList()).toList()
            };
            _msDocxPlugin.generate(
                "assets://template_table.docx",
                '/mnt/sdcard/Download/generate.docx',
                contentMap);
          },
          child: const Text("生成Word"),
        )),
      ),
    );
  }
}
