import 'dart:developer' as developer;
import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutter/material.dart';
import 'shop_models.dart';

/// Widget untuk item card di shop grid
class ShopItemCard extends StatelessWidget {
  final ShopItem item;  
  final bool isCharacter;
  final VoidCallback onTap;
  final String userId;

  const ShopItemCard({
    Key? key,
    required this.item,
    required this.isCharacter,
    required this.onTap,
    required this.userId,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        height: 140,
        decoration: BoxDecoration(
          color: const Color(0xFF343F73),
          borderRadius: BorderRadius.circular(12),
          border: Border.all(
            color: const Color(0xFF343F73),
            width: 1,
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.1), 
              blurRadius: 4,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Stack(
          children: [
            Center(
              child: SizedBox(
                width: 100,
                height: 100,
                child: Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(6),
                    child: _buildItemAnimation(),
                  ),
                ),
              ),
            ),

            Positioned(
              top: 12,
              left: 12,
              child: Text(
                item.name,
                style: const TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                  color: Color(0xFFF9FBFF),
                  shadows: [
                    Shadow(
                      blurRadius: 4.0,
                      color: Colors.black,
                      offset: Offset(1.0, 1.0),
                    ),
                  ],
                ),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ),

            Positioned(
              top: 12,
              right: 12,
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                decoration: BoxDecoration(
                  color: _parseColor(item.rarityColor),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Text(
                  item.rarity,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 10,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),

            Positioned(
              bottom: 12,
              right: 12,
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(
                    Icons.monetization_on,
                    color: Colors.orange[400],
                    size: 16,
                    shadows: const [
                      Shadow(
                        blurRadius: 2.0,
                        color: Colors.black,
                        offset: Offset(1.0, 1.0),
                      ),
                    ],
                  ),
                  const SizedBox(width: 4),
                  Text(
                    item.price.toString(),
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 14,
                      fontWeight: FontWeight.bold,
                      shadows: [
                        Shadow(
                          blurRadius: 4.0,
                          color: Colors.black,
                          offset: Offset(1.0, 1.0),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildItemAnimation() {
    final assetPath = isCharacter
        ? 'assets/character/${item.fileName}'
        : 'assets/scenery/${item.fileName}';

    return Stack(
      alignment: Alignment.center,
      children: [
        Image.asset(
          assetPath,
          fit: BoxFit.cover,
          width: double.infinity,
          height: double.infinity,
          errorBuilder: (context, error, stackTrace) {
            developer.log(
              'Image load error in card: $error',
              name: 'ShopItemCard._buildItemAnimation',
              error: error,
            );
            // Error fallback
            return Container(
              color: Colors.grey[300],
              child: Icon(
                isCharacter ? Icons.person : Icons.landscape,
                size: 48,
                color: Colors.grey[600],
              ),
            );
          },
        ),
      ],
    );
  }

  Color _parseColor(String hexColor) {
    try {
      return Color(int.parse(hexColor.replaceFirst('#', '0xFF')));
    } catch (e) {
      developer.log('Error parsing color: $hexColor', name: 'ShopItemCard._parseColor');
      return Colors.grey;
    }
  }

  
}

/// Widget untuk loading state
class ShopLoadingWidget extends StatelessWidget {
  const ShopLoadingWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
      padding: const EdgeInsets.all(16),
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        childAspectRatio: 0.75,
        crossAxisSpacing: 16,
        mainAxisSpacing: 16,
      ),
      itemCount: 6,
      itemBuilder: (context, index) {
        return _buildShimmerCard();
      },
    );
  }

  Widget _buildShimmerCard() {
    return Container(
      decoration: BoxDecoration(
        color: Colors.grey[300],
        borderRadius: BorderRadius.circular(12),
      ),
      child: Column(
        children: [
          Expanded(
            flex: 3,
            child: Container(
              margin: const EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: Colors.grey[200],
                borderRadius: BorderRadius.circular(8),
              ),
            ),
          ),
          Expanded(
            flex: 1,
            child: Padding(
              padding: const EdgeInsets.all(8),
              child: Column(
                children: [
                  Container(
                    height: 14,
                    decoration: BoxDecoration(
                      color: Colors.grey[200],
                      borderRadius: BorderRadius.circular(4),
                    ),
                  ),
                  const SizedBox(height: 4),
                  Container(
                    height: 10,
                    width: 60,
                    decoration: BoxDecoration(
                      color: Colors.grey[200],
                      borderRadius: BorderRadius.circular(4),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}