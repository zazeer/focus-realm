
import 'package:flutter/material.dart';
import 'package:frontend/themes/app_colors.dart';

class ProfilePage extends StatefulWidget {
  final String userId;

  const ProfilePage({Key? key, required this.userId}) : super(key: key);

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}
class _ProfilePageState extends State<ProfilePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Profile Page'),
        backgroundColor: AppColors.primary,
      ),
      body: Center(
        child: Text('User ID: ${widget.userId}'),
      ),
    );
  }
}