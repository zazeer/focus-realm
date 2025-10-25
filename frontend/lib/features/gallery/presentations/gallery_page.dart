import 'dart:developer' as developer;
import 'package:flutter/material.dart';
import '../models/gallery_models.dart';
import '../services/gallery_service.dart';
import '../widgets/gallery_widgets.dart';

enum GalleryTab { character, scenery }

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
    with TickerProviderStateMixin {
  
  late final GalleryService _galleryService;
  late final TabController _tabController;
  
  GalleryPageModel? _galleryData;
  bool _isLoading = true;
  String? _errorMessage;
  GalleryTab _currentTab = GalleryTab.character;
  String? _selectedCharacterId;
  String? _selectedSceneryId;
  
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

      // For demo purposes, using mock data fetch
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
      return;
    }

    try {
      developer.log('Selecting character: ${character.name}', name: 'GalleryPage._selectCharacter');
      
      setState(() {
        _selectedCharacterId = character.id;
      });
      
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
      return;
    }

    try {
      developer.log('Selecting scenery: ${scenery.name}', name: 'GalleryPage._selectScenery');
      
      setState(() {
        _selectedSceneryId = scenery.id;
      });
      
    } catch (e) {
      developer.log('Error selecting scenery: $e', name: 'GalleryPage._selectScenery', error: e);
      setState(() {
        _selectedSceneryId = _galleryData?.chosenScenery?.id;
      });
      _showErrorSnackBar('Failed to select scenery');
    }
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
                child: Image.asset(  
                  'assets/scenery/${selectedScenery.fileName}',
                  fit: BoxFit.cover,
                  errorBuilder: (context, error, stackTrace) {
                    developer.log('Scenery image error: $error');
                    return Container(
                      color: Colors.grey[300],
                      child: const Icon(
                        Icons.landscape,
                        size: 64,
                        color: Colors.grey,
                      ),
                    );
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
                  child: SizedBox(  
                    width: 120,
                    height: 120,
                    child: Image.asset(
                      'assets/character/${selectedCharacter.fileName}',
                      fit: BoxFit.contain,
                      errorBuilder: (context, error, stackTrace) {
                        developer.log('Character image error: $error');
                        return const Icon(
                          Icons.person,
                          size: 64,
                          color: Colors.grey,
                        );
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
                      const Color(0xFF2E77C3),
                      const Color(0xFF2E77C3),
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

  Widget _buildTabButtons() {
    final Color activeColor = const Color(0xFF2E77C3);
    final Color inactiveColor = const Color(0xFFB0BEC5);

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 40),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Expanded(
            child: ElevatedButton(
              onPressed: () {
                _tabController.animateTo(0);
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: _currentTab == GalleryTab.character
                    ? activeColor
                    : inactiveColor,
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                padding: const EdgeInsets.symmetric(vertical: 12),
              ),
              child: const Text(
                'Character',
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
              ),
            ),
          ),
          const SizedBox(width: 16),
          Expanded(
            child: ElevatedButton(
              onPressed: () {
                _tabController.animateTo(1);
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: _currentTab == GalleryTab.scenery
                    ? activeColor
                    : inactiveColor,
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                padding: const EdgeInsets.symmetric(vertical: 12),
              ),
              child: const Text(
                'Scenery',
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
              ),
            ),
          ),
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
        color: const Color(0xFF2E77C3),
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
      body: _isLoading
        ? _buildLoadingState()
        : _errorMessage != null
            ? _buildErrorState()
            : FadeTransition(
                opacity: _fadeAnimation,
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(top: 20, bottom: 24),
                      child: _buildPreviewArea(),
                    ),
                    
                    _buildTabButtons(),
                    
                    const SizedBox(height: 20),
                    
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