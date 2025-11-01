import 'package:flutter/material.dart';
import 'package:frontend/Pages/profile_page.dart';
import 'package:frontend/Pages/custom_time.dart';
import 'package:frontend/features/gallery/presentations/gallery_page.dart';
import 'package:frontend/features/shop/shop_page.dart';
import 'package:frontend/Pages/home_page.dart';

class CustomNavigationBar extends StatelessWidget {
  final String userId;
  final int selectedIndex; // 0=explore, 1=shop, 2=home, 3=gallery, 4=profile

  const CustomNavigationBar({
    Key? key,
    required this.userId,
    required this.selectedIndex,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 70,
      padding: const EdgeInsets.symmetric(horizontal: 20),
      decoration: BoxDecoration(
        color: const Color(0xFFF9FBFF),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 10,
            offset: const Offset(0, -5),
          ),
        ],
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children: [
          _buildNavItem(context, Icons.explore, 0),
          _buildNavItem(context, Icons.shopping_bag, 1),
          _buildNavItem(context, Icons.home, 2),
          _buildNavItem(context, Icons.assessment, 3),
          _buildNavItem(context, Icons.person, 4),
        ],
      ),
    );
  }

  Widget _buildNavItem(BuildContext context, IconData icon, int index) {
    final bool isSelected = selectedIndex == index;
    
    return GestureDetector(
      onTap: () {
        if (index == selectedIndex) return;

        Widget destination;
        switch (index) {
          case 0:
            return;
          case 1:
            destination = ShopPage(userId: userId);
            break;
          case 2:
            destination = HomePage(userId: userId);
            break;
          case 3:
            destination = GalleryPage(userId: userId);
            break;
          case 4:
            destination = ProfilePage(userId: userId);
            break;
          default:
            return;
        }

        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => destination),
        );
      },
      child: Container(
        padding: const EdgeInsets.all(8),
        decoration: BoxDecoration(
          color: isSelected ? const Color(0xFF4C9EEB) : Colors.transparent,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Icon(
          icon,
          color: isSelected ? const Color(0xFFF9FBFF) : Colors.grey,
          size: 28,
        ),
      ),
    );
  }
}