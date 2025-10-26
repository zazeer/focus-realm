import 'dart:developer' as developer;
import 'package:flutter/material.dart';
import 'shop_models.dart';

/// Widget untuk menampilkan preview hero area
class HeroPreviewWidget extends StatefulWidget {
  final ShopItem item;
  final bool isCharacter;

  const HeroPreviewWidget({
    Key? key,
    required this.item,
    required this.isCharacter,
  }) : super(key: key);

  @override
  State<HeroPreviewWidget> createState() => _HeroPreviewWidgetState();
}

class _HeroPreviewWidgetState extends State<HeroPreviewWidget> {
  bool _isImageLoaded = false;
  String? _imageError;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          Expanded(
            flex: 3,
            child: Container(
              width: double.infinity,
              decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.8),
                borderRadius: BorderRadius.circular(12),
                border: Border.all(
                  color: _parseColor(widget.item.rarityColor),
                  width: 3,
                ),
              ),
              child: ClipRRect(
                borderRadius: BorderRadius.circular(9),
                child: _buildImageAnimation(),
              ),
            ),
          ),
          
          const SizedBox(height: 16),
          
          // Item Details
          Expanded(
            flex: 1,
            child: _buildItemDetails(),
          ),
        ],
      ),
    );
  }

  Widget _buildImageAnimation() {
    final assetPath = widget.isCharacter
        ? 'assets/character/${widget.item.fileName}'
        : 'assets/scenery/${widget.item.fileName}';

    return Stack(
    children: [
      if (_imageError == null)
        Image.asset(  
          assetPath,
          fit: BoxFit.contain,
          frameBuilder: (context, child, frame, wasSynchronouslyLoaded) {
            if (wasSynchronouslyLoaded || frame != null) {
              // Image loaded
              if (!_isImageLoaded) {
                WidgetsBinding.instance.addPostFrameCallback((_) {
                  if (mounted) {
                    setState(() {
                      _isImageLoaded = true;
                    });
                  }
                });
              }
              return child;
            }
            return const SizedBox();
          },
          errorBuilder: (context, error, stackTrace) {
            developer.log('Image load error: $error');
            WidgetsBinding.instance.addPostFrameCallback((_) {
              if (mounted) {
                setState(() {
                  _imageError = error.toString();
                });
              }
            });
            return const SizedBox();
          },
        ),

        // Fallback/Loading State
        if (!_isImageLoaded || _imageError != null)
          Container(
            width: double.infinity,
            height: double.infinity,
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                colors: [
                  _parseColor(widget.item.rarityColor).withOpacity(0.3),
                  _parseColor(widget.item.rarityColor).withOpacity(0.1),
                ],
              ),
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  widget.isCharacter ? Icons.person : Icons.landscape,
                  size: 64,
                  color: _parseColor(widget.item.rarityColor),
                ),
                const SizedBox(height: 8),
                Text(
                  _imageError != null ? 'Animation Error' : 'Loading...',
                  style: TextStyle(
                    color: Colors.grey[600],
                    fontSize: 14,
                  ),
                ),
              ],
            ),
          ),

        // Loading overlay
        if (!_isImageLoaded && _imageError == null)
          const Center(
            child: CircularProgressIndicator(),
          ),
      ],
    );
  }

  Widget _buildItemDetails() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        // Item Name
        Text(
          widget.item.name,
          style: const TextStyle(
            fontSize: 20,
            fontWeight: FontWeight.bold,
            color: Colors.black87,
          ),
          textAlign: TextAlign.center,
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
        ),
        
        const SizedBox(height: 4),
        
        // Rarity Badge
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
          decoration: BoxDecoration(
            color: _parseColor(widget.item.rarityColor),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Text(
            widget.item.rarity,
            style: const TextStyle(
              color: Colors.white,
              fontSize: 12,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        
        const SizedBox(height: 8),
        
        // Description
        Expanded(
          child: Text(
            widget.item.description,
            style: TextStyle(
              fontSize: 14,
              color: Colors.grey[600],
            ),
            textAlign: TextAlign.center,
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    );
  }

  Color _parseColor(String hexColor) {
    try {
      return Color(int.parse(hexColor.replaceFirst('#', '0xFF')));
    } catch (e) {
      developer.log('Error parsing color: $hexColor', name: 'HeroPreviewWidget._parseColor');
      return Colors.grey;
    }
  }
}

/// Widget untuk item card di shop grid
class ShopItemCard extends StatelessWidget {
  final ShopItem item;
  final bool isSelected;
  final bool isCharacter;
  final VoidCallback onTap;

  const ShopItemCard({
    Key? key,
    required this.item,
    required this.isSelected,
    required this.isCharacter,
    required this.onTap,
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

        // Overlay for unobtained items
        if (!item.isObtained)
          Container(
            width: double.infinity,
            height: double.infinity,
            decoration: BoxDecoration(
              color: Colors.black.withOpacity(0.7),
              borderRadius: BorderRadius.circular(6),
            ),
            child: const Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  Icons.lock,
                  color: Colors.white,
                  size: 32,
                ),
                SizedBox(height: 8),
                Text(
                  'LOCKED',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
          ),

        if (isSelected && item.isObtained)
          Builder(  
            builder: (context) => Positioned(  
              top: 8,
              right: 8,
              child: Container(
                padding: const EdgeInsets.all(4),
                decoration: BoxDecoration(
                  color: Theme.of(context).primaryColor,
                  shape: BoxShape.circle,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.3),
                      blurRadius: 4,
                      offset: const Offset(0, 2),
                    ),
                  ],
                ),
              ),
            ),
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