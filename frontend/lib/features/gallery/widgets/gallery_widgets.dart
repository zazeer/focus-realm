import 'dart:developer' as developer;
import 'package:flutter/material.dart';
import 'package:rive/rive.dart' as rive;
import '../models/gallery_models.dart';

/// Widget untuk menampilkan preview hero area
class HeroPreviewWidget extends StatefulWidget {
  final GalleryItem item;
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
  bool _isRiveLoaded = false;
  String? _riveError;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          // Rive Animation Area
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
                child: _buildRiveAnimation(),
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

  Widget _buildRiveAnimation() {
    final assetPath = widget.isCharacter
        ? 'assets/character/${widget.item.fileName}'
        : 'assets/scenery/${widget.item.fileName}';

    return Stack(
      children: [
        // Rive Animation
        if (_riveError == null)
          Builder(
            builder: (context) {
              try {
                return rive.RiveAnimation.asset(
                  assetPath,
                  fit: BoxFit.contain,
                  onInit: (artboard) {
                    setState(() {
                      _isRiveLoaded = true;
                    });
                    developer.log(
                      'Rive animation loaded: ${widget.item.fileName}',
                      name: 'HeroPreviewWidget._buildRiveAnimation',
                    );
                  },
                );
              } catch (error) {
                setState(() {
                  _riveError = error.toString();
                });
                developer.log(
                  'Rive animation error: $error',
                  name: 'HeroPreviewWidget._buildRiveAnimation',
                  error: error,
                );
                return const SizedBox(); // fallback kosong
              }
            },
          ),

        // Fallback/Loading State
        if (!_isRiveLoaded || _riveError != null)
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
                  _riveError != null ? 'Animation Error' : 'Loading...',
                  style: TextStyle(
                    color: Colors.grey[600],
                    fontSize: 14,
                  ),
                ),
              ],
            ),
          ),

        // Loading overlay
        if (!_isRiveLoaded && _riveError == null)
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

/// Widget untuk item card di gallery grid
class GalleryItemCard extends StatelessWidget {
  final GalleryItem item;
  final bool isSelected;
  final bool isCharacter;
  final VoidCallback onTap;

  const GalleryItemCard({
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
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(
            color: isSelected
                ? Theme.of(context).primaryColor
                : Colors.grey[300]!,
            width: isSelected ? 3 : 1,
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(isSelected ? 0.2 : 0.1),
              blurRadius: isSelected ? 8 : 4,
              offset: Offset(0, isSelected ? 4 : 2),
            ),
          ],
        ),
        child: Column(
          children: [
            // Animation Area
            Expanded(
              flex: 3,
              child: Container(
                width: double.infinity,
                margin: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(8),
                  border: Border.all(
                    color: _parseColor(item.rarityColor).withOpacity(0.5),
                    width: 2,
                  ),
                ),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(6),
                  child: _buildItemAnimation(),
                ),
              ),
            ),
            
            // Item Info
            Expanded(
              flex: 1,
              child: Padding(
                padding: const EdgeInsets.all(8),
                child: Column(
                  children: [
                    // Item Name
                    Text(
                      item.name,
                      style: const TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.bold,
                        color: Colors.black87,
                      ),
                      textAlign: TextAlign.center,
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    
                    const SizedBox(height: 4),
                    
                    // Rarity or Status
                    _buildStatusIndicator(),
                  ],
                ),
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
      children: [
        // Rive Animation
        Builder(
          builder: (context) {
            try {
              return rive.RiveAnimation.asset(
                assetPath,
                fit: BoxFit.cover,
              );
            } catch (error) {
              developer.log(
                'Rive animation error in card: $error',
                name: 'GalleryItemCard._buildItemAnimation',
                error: error,
              );
              return const SizedBox(); // fallback kosong
            }
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

        // Selection indicator
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
                child: const Icon(
                  Icons.check,
                  color: Colors.white,
                  size: 16,
                ),
              ),
            ),
          ),
      ],
    );
  }

  Widget _buildStatusIndicator() {
    if (!item.isObtained) {
      return Container(
        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
        decoration: BoxDecoration(
          color: Colors.red[100],
          borderRadius: BorderRadius.circular(8),
        ),
        child: Text(
          '${item.price} coins',
          style: TextStyle(
            fontSize: 10,
            color: Colors.red[700],
            fontWeight: FontWeight.w500,
          ),
        ),
      );
    } else {
      return Container(
        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
        decoration: BoxDecoration(
          color: _parseColor(item.rarityColor).withOpacity(0.2),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Text(
          item.rarity,
          style: TextStyle(
            fontSize: 10,
            color: _parseColor(item.rarityColor),
            fontWeight: FontWeight.bold,
          ),
        ),
      );
    }
  }

  Color _parseColor(String hexColor) {
    try {
      return Color(int.parse(hexColor.replaceFirst('#', '0xFF')));
    } catch (e) {
      developer.log('Error parsing color: $hexColor', name: 'GalleryItemCard._parseColor');
      return Colors.grey;
    }
  }
}

/// Widget untuk loading state
class GalleryLoadingWidget extends StatelessWidget {
  const GalleryLoadingWidget({Key? key}) : super(key: key);

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
      itemCount: 6, // Show 6 shimmer cards
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