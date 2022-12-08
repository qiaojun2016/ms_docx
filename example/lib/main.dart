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
                      '序号': '${index + 1}',
                      '样品编号': '${100001 + index}',
                      '名称': '大头菜',
                      '产地': '山东日照五莲县许孟镇',
                      '送检单位': '山东美正生物有限公司',
                      '检测人员': '李四',
                      '结果': '0.43',
                      '定性': '阴性',
                      '检测日期': '2022-08-12'
                    }))
                .toList();
            final contentMap = <String, dynamic>{
              'project': '甲醛',
              'department': '北京美正生物快检服务中心',
              'limit': '40%',
              'createDate': '2022-03-12',
              'sampleType': '蔬菜类',
              'method': '分光度法',
              'list': rowData.map((e) => e.values.toList()).toList()
            };
            _msDocxPlugin.generate(
                "/data/data/com.github.qiaojun2016.ms_docx_example/files/template.docx",
                '/data/data/com.github.qiaojun2016.ms_docx_example/files/generate.docx',
                contentMap);
          },
          child: const Text("生成Word"),
        )),
      ),
    );
  }
}
