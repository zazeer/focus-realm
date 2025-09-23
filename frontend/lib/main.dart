import 'package:flutter/material.dart';
import 'package:frontend/Pages/login_page.dart';
import 'package:frontend/Pages/register_page.dart';
import 'package:frontend/Pages/home_page.dart';
import 'package:frontend/features/gallery/presentations/gallery_page.dart';
// Import pages yang sudah dibuat
// import 'login_page.dart';
// import 'register_page.dart';
// import 'user_api_service.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Focus Realm App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
        fontFamily: 'Poppins',
      ),
      home: const GalleryPage(userId: '1'),
      debugShowCheckedModeBanner: false,
    );
  }
}