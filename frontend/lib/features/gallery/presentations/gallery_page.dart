import 'dart:developer' as developer;
import 'package:flutter/material.dart';
import 'package:rive/rive.dart'as rive;
import '../models/gallery_models.dart';
import '../services/gallery_service.dart';
import '../widgets/gallery_widgets.dart';

/// Enum untuk gallery tab
enum GalleryTab { character, scenery }

/// Gallery Page - Main presentation layer
class GalleryPage extends StatefulWidget {
  final String userId;
  final GalleryService? galleryService;

  const GalleryPage({
    Key? key,
    required this.userId,
    this.galleryService,
  }) : super(key: key);

  @override
  State<GalleryPage> createState() => _GalleryPageState();
}

class _GalleryPageState extends State<GalleryPage>
    with SingleTickerProviderStateMixin {
  
  // Services and Controllers
  late final GalleryService _galleryService;
  late final TabController _tabController;
  
  // State variables
  GalleryPageModel? _galleryData;
  bool _isLoading = true;
  String? _errorMessage;
  GalleryTab _currentTab = GalleryTab.character;
  String? _selectedCharacterId;
  String? _selectedSceneryId;
  
  // Animation controllers for smooth transitions
  late final AnimationController _fadeController;
  late final Animation<double> _fadeAnimation;

  @override
  void initState() {
    super.initState();
    _initializeControllers();
    _galleryService = widget.galleryService ?? GalleryService();
    _loadGalleryData();
  }

  void _initializeControllers() {
    _tabController = TabController(length: 2, vsync: this);
    _tabController.addListener(_handleTabChange);
    
    _fadeController = AnimationController(
      duration: const Duration(milliseconds: 300),
      vsync: this,
    );
    _fadeAnimation = Tween<double>(begin: 0.0, end: 1.0).animate(
      CurvedAnimation(parent: _fadeController, curve: Curves.easeInOut),
    );
  }

  void _handleTabChange() {
    if (_tabController.indexIsChanging) return;
    
    setState(() {
      _currentTab = _tabController.index == 0 
          ? GalleryTab.character 
          : GalleryTab.scenery;
    });
    developer.log('Tab changed to: ${_currentTab.name}', name: 'GalleryPage._handleTabChange');
  }

  Future<void> _loadGalleryData() async {
    try {
      developer.log('Loading gallery data for user: ${widget.userId}', name: 'GalleryPage._loadGalleryData');
      
      setState(() {
        _isLoading = true;
        _errorMessage = null;
      });

      // Untuk demo, gunakan mock data. Ganti dengan fetchGalleryData untuk production
      final galleryData = await _galleryService.fetchGalleryDataMock(widget.userId);
      // final galleryData = await _galleryService.fetchGalleryData(widget.userId);
      
      setState(() {
        _galleryData = galleryData;
        _selectedCharacterId = galleryData.chosenCharacter?.id;
        _selectedSceneryId = galleryData.chosenScenery?.id;
        _isLoading = false;
      });

      _fadeController.forward();
      developer.log('Gallery data loaded successfully', name: 'GalleryPage._loadGalleryData');
      
    } catch (e) {
      developer.log('Error loading gallery data: $e', name: 'GalleryPage._loadGalleryData', error: e);
      
      setState(() {
        _errorMessage = e is GalleryServiceException 
            ? e.message 
            : 'Failed to load gallery data. Please try again.';
        _isLoading = false;
      });
    }
  }

  Future<void> _selectCharacter(Character character) async {
    if (!character.isObtained) {
      _showLockedItemDialog(character);
      return;
    }

    try {
      developer.log('Selecting character: ${character.name}', name: 'GalleryPage._selectCharacter');
      
      setState(() {
        _selectedCharacterId = character.id;
      });

      // Update selection on server (uncomment for production)
      // final success = await _galleryService.updateCharacterSelection(
      //   userId: widget.userId,
      //   characterId: character.id,
      // );
      // 
      // if (!success) {
      //   setState(() {
      //     _selectedCharacterId = _galleryData?.chosenCharacter?.id;
      //   });
      //   _showErrorSnackBar('Failed to update character selection');
      // }
      
    } catch (e) {
      developer.log('Error selecting character: $e', name: 'GalleryPage._selectCharacter', error: e);
      setState(() {
        _selectedCharacterId = _galleryData?.chosenCharacter?.id;
      });
      _showErrorSnackBar('Failed to select character');
    }
  }

  Future<void> _selectScenery(Scenery scenery) async {
    if (!scenery.isObtained) {
      _showLockedItemDialog(scenery);
      return;
    }

    try {
      developer.log('Selecting scenery: ${scenery.name}', name: 'GalleryPage._selectScenery');
      
      setState(() {
        _selectedSceneryId = scenery.id;
      });

      // Update selection on server (uncomment for production)
      // final success = await _galleryService.updateScenerySelection(
      //   userId: widget.userId,
      //   sceneryId: scenery.id,
      // );
      // 
      // if (!success) {
      //   setState(() {
      //     _selectedSceneryId = _galleryData?.chosenScenery?.id;
      //   });
      //   _showErrorSnackBar('Failed to update scenery selection');
      // }
      
    } catch (e) {
      developer.log('Error selecting scenery: $e', name: 'GalleryPage._selectScenery', error: e);
      setState(() {
        _selectedSceneryId = _galleryData?.chosenScenery?.id;
      });
      _showErrorSnackBar('Failed to select scenery');
    }
  }

  void _showLockedItemDialog(GalleryItem item) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('${item is Character ? 'Character' : 'Scenery'} Locked'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('${item.name} is not yet available.'),
            const SizedBox(height: 8),
            Text('Price: ${item.price} coins'),
            Text('Release Date: ${item.releaseDate}'),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('OK'),
          ),
        ],
      ),
    );
  }

  void _showErrorSnackBar(String message) {
    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(message),
          backgroundColor: Colors.red[600],
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
  }

  Widget _buildPreviewArea() {
    // Always show both character and scenery in preview (combined scene)
    final selectedCharacter = _galleryData?.allCharacters.firstWhere(
      (char) => char.id == _selectedCharacterId,
      orElse: () => _galleryData!.chosenCharacter ?? _galleryData!.allCharacters.first,
    );
    
    final selectedScenery = _galleryData?.allSceneries.firstWhere(
      (scenery) => scenery.id == _selectedSceneryId,
      orElse: () => _galleryData!.chosenScenery ?? _galleryData!.allSceneries.first,
    );

    return Container(
      height: 300,
      width: double.infinity,
      margin: const EdgeInsets.symmetric(horizontal: 16),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(20),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.15),
            blurRadius: 15,
            offset: const Offset(0, 8),
          ),
        ],
      ),
      child: ClipRRect(
        borderRadius: BorderRadius.circular(20),
        child: Stack(
          children: [
            // Scenery background
            if (selectedScenery != null)
              Positioned.fill(
                child: Builder(
                  builder: (context) {
                    try {
                      return rive.RiveAnimation.asset(
                        'assets/scenery/${selectedScenery.fileName}',
                        fit: BoxFit.cover,
                      );
                    } catch (e) {
                      developer.log('Scenery animation error: $e');
                      return const SizedBox(); 
                    }
                  },
                ),
              ),
            
            // Character foreground
            if (selectedCharacter != null)
              Positioned(
                bottom: 20,
                left: 0,
                right: 0,
                child: Center(
                  child: Container(
                    width: 120,
                    height: 120,
                    child: Builder(
                      builder: (context) {
                        try {
                          return rive.RiveAnimation.asset(
                            'assets/character/${selectedCharacter.fileName}',
                            fit: BoxFit.contain,
                          );
                        } catch (e) {
                          developer.log('Character animation error: $e');
                          return const SizedBox(); // fallback kosong
                        }
                      },
                    ),
                  ),
                ),
              ),
            
            // Fallback if no items selected
            if (selectedCharacter == null && selectedScenery == null)
              Container(
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topCenter,
                    end: Alignment.bottomCenter,
                    colors: [
                      Colors.blue[300]!,
                      Colors.blue[600]!,
                    ],
                  ),
                ),
                child: const Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(Icons.landscape, size: 64, color: Colors.white),
                      SizedBox(height: 16),
                      Text(
                        'Select character and scenery',
                        style: TextStyle(
                          fontSize: 18,
                          color: Colors.white,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildTabBar() {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 40),
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(25),
      ),
      child: TabBar(
        controller: _tabController,
        indicator: BoxDecoration(
          color: Colors.blue[600],
          borderRadius: BorderRadius.circular(25),
        ),
        indicatorSize: TabBarIndicatorSize.tab,
        dividerColor: Colors.transparent,
        labelColor: Colors.white,
        unselectedLabelColor: Colors.grey[600],
        labelStyle: const TextStyle(
          fontWeight: FontWeight.bold,
          fontSize: 16,
        ),
        unselectedLabelStyle: const TextStyle(
          fontWeight: FontWeight.w500,
          fontSize: 16,
        ),
        tabs: const [
          Tab(text: 'Character'),
          Tab(text: 'Scenery'),
        ],
      ),
    );
  }

  Widget _buildGalleryGrid() {
    final items = _currentTab == GalleryTab.character
        ? _galleryData?.allCharacters.cast<GalleryItem>() ?? []
        : _galleryData?.allSceneries.cast<GalleryItem>() ?? [];

    final selectedId = _currentTab == GalleryTab.character
        ? _selectedCharacterId
        : _selectedSceneryId;

    return Container(
      decoration: BoxDecoration(
        color: Colors.blue[600],
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(30),
          topRight: Radius.circular(30),
        ),
      ),
      child: GridView.builder(
        padding: const EdgeInsets.all(20),
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          childAspectRatio: 0.8,
          crossAxisSpacing: 16,
          mainAxisSpacing: 16,
        ),
        itemCount: items.length,
        itemBuilder: (context, index) {
          final item = items[index];
          final isSelected = item.id == selectedId;

          return GalleryItemCard(
            item: item,
            isSelected: isSelected,
            isCharacter: _currentTab == GalleryTab.character,
            onTap: () {
              if (_currentTab == GalleryTab.character) {
                _selectCharacter(item as Character);
              } else {
                _selectScenery(item as Scenery);
              }
            },
          );
        },
      ),
    );
  }

  Widget _buildLoadingState() {
    return const Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          CircularProgressIndicator(),
          SizedBox(height: 16),
          Text(
            'Loading Gallery...',
            style: TextStyle(
              fontSize: 16,
              color: Colors.grey,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildErrorState() {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            Icons.error_outline,
            size: 64,
            color: Colors.red[400],
          ),
          const SizedBox(height: 16),
          Text(
            _errorMessage ?? 'Something went wrong',
            style: TextStyle(
              fontSize: 16,
              color: Colors.red[600],
            ),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 16),
          ElevatedButton.icon(
            onPressed: _loadGalleryData,
            icon: const Icon(Icons.refresh),
            label: const Text('Retry'),
            style: ElevatedButton.styleFrom(
              backgroundColor: Theme.of(context).primaryColor,
              foregroundColor: Colors.white,
            ),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Gallery',
          style: TextStyle(
            fontWeight: FontWeight.bold,
            fontSize: 24,
          ),
        ),
        centerTitle: true,
        backgroundColor: Theme.of(context).primaryColor,
        foregroundColor: Colors.white,
        elevation: 0,
        actions: [
          IconButton(
            onPressed: _loadGalleryData,
            icon: const Icon(Icons.refresh),
            tooltip: 'Refresh Gallery',
          ),
        ],
      ),
      body: _isLoading
          ? _buildLoadingState()
          : _errorMessage != null
              ? _buildErrorState()
              : FadeTransition(
                  opacity: _fadeAnimation,
                  child: Column(
                    children: [
                      // Hero Preview Area
                      Padding(
                        padding: const EdgeInsets.only(top: 20, bottom: 24),
                        child: _buildPreviewArea(),
                      ),
                      
                      // Tab Bar
                      _buildTabBar(),
                      
                      const SizedBox(height: 20),
                      
                      // Gallery Grid
                      Expanded(
                        child: _buildGalleryGrid(),
                      ),
                    ],
                  ),
                ),
    );
  }

  @override
  void dispose() {
    _tabController.dispose();
    _fadeController.dispose();
    developer.log('GalleryPage disposed', name: 'GalleryPage.dispose');
    super.dispose();
  }
}