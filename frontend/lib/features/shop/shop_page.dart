import 'dart:developer' as developer;
import 'package:flutter/material.dart';
import 'shop_models.dart';
import 'shop_service.dart';
import 'shop_widgets.dart';
import 'package:frontend/features/widgets/navigation_bar.dart'; 

enum ShopTab { character, scenery }

class ShopPage extends StatefulWidget {
  final String userId;
  final ShopService? shopService;

  const ShopPage({
    Key? key,
    required this.userId,
    this.shopService,
  }) : super(key: key);

  @override
  State<ShopPage> createState() => _ShopPageState();
}

class _ShopPageState extends State<ShopPage>
    with TickerProviderStateMixin {
  
  late final ShopService _shopService;
  late final TabController _tabController;
  
  ShopPageModel? _shopData;
  bool _isLoading = true;
  String? _errorMessage;
  ShopTab _currentTab = ShopTab.character;
  
  late final AnimationController _fadeController;
  late final Animation<double> _fadeAnimation;

  @override
  void initState() {
    super.initState();
    _initializeControllers();
    _shopService = widget.shopService ?? ShopService();
    _loadShopData();
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

  void _showItemPreviewPopup(ShopItem item, bool isCharacter) {
    bool isBuying = false; 

    showDialog(
      context: context,
      barrierDismissible: false, 
      builder: (BuildContext dialogContext) {
        return StatefulBuilder(
          builder: (context, setDialogState) {
            return Dialog(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
              backgroundColor: const Color(0xFFECECEC),
              child: Container(
                padding: const EdgeInsets.all(20),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    _buildPopupInfo(item, isCharacter),
                    
                    const SizedBox(height: 24),
                    
                    _buildPopupButtons(
                      dialogContext, 
                      item,
                      isBuying,
                      setDialogState, 
                    ),
                  ],
                ),
              ),
            );
          },
        );
      },
    );
  }
  
  Widget _buildPopupInfo(ShopItem item, bool isCharacter) {
    final assetPath = isCharacter
        ? 'assets/character/${item.fileName}'
        : 'assets/scenery/${item.fileName}';

    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          width: 80,
          height: 80,
          child: Image.asset(
            assetPath,
            fit: BoxFit.contain,
            errorBuilder: (context, error, stackTrace) {
              return Icon(
                isCharacter ? Icons.person : Icons.landscape,
                size: 60,
                color: Colors.grey[700],
              );
            },
          ),
        ),
        
        const SizedBox(width: 16),

        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                item.name,
                style: const TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                  color: Colors.black,
                ),
              ),
              const SizedBox(height: 4),
              
              Container(
                height: 2,
                color: Colors.black,
              ),
              const SizedBox(height: 8),
              
              Text(
                item.description,
                style: TextStyle(
                  fontSize: 14,
                  color: Colors.grey[800],
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildPopupButtons(
    BuildContext dialogContext, 
    ShopItem item,
    bool isBuying, 
    StateSetter setDialogState, 
  ) {
    final buttonColor = const Color(0xFF4A5A9D);
    final buttonShape = RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(30),
    );

    final bool isAlreadyObtained = item.isObtained;

    return Row(
      children: [
        Expanded(
          child: ElevatedButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: buttonColor,
              shape: buttonShape,
              padding: const EdgeInsets.symmetric(vertical: 12),
              disabledBackgroundColor: Colors.grey[400],
            ),
            onPressed: (isBuying || isAlreadyObtained)
                ? null
                : () async {
                    // 1. Set state loading
                    setDialogState(() {
                      isBuying = true;
                    });

                    try {
                      // 2. Panggil API service
                      if (item is Character) {
                        // Untuk TESTING (Mock):
                        await _shopService.purchaseCharacterMock(
                          userId: widget.userId,
                          characterId: item.id,
                        );
                        
                        // Untuk PRODUCTION (Real API):
                        // await _shopService.purchaseCharacter(
                        //   userId: widget.userId,
                        //   characterId: item.id,
                        // );
                        
                      } else if (item is Scenery) {
                        // Untuk TESTING (Mock):
                        await _shopService.purchaseSceneryMock(
                          userId: widget.userId,
                          sceneryId: item.id,
                        );
                        
                        // Untuk PRODUCTION (Real API):
                        // await _shopService.purchaseScenery(
                        //   userId: widget.userId,
                        //   sceneryId: item.id,
                        // );
                      }

                      // 3. Sukses
                      Navigator.of(dialogContext).pop(); 
                      _showSuccessSnackBar('${item.name} purchased!');
                      _loadShopData(); 
                    
                    } catch (e) {
                      // 4. Gagal
                      Navigator.of(dialogContext).pop(); 
                      _showErrorSnackBar(
                        e is ShopServiceException
                            ? e.message
                            : 'Purchase failed. Please try again.',
                      );
                    }
                  },
            child: isBuying
                ? const SizedBox( 
                    height: 24,
                    width: 24,
                    child: CircularProgressIndicator(
                      strokeWidth: 2,
                      color: Colors.white,
                    ),
                  )
                : isAlreadyObtained
                    ? const Text( 
                        'Owned',
                        style: TextStyle(
                          color: Colors.black54,
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      )
                    : Row( 
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(
                            Icons.monetization_on,
                            color: Colors.yellow[600],
                            size: 24,
                          ),
                          const SizedBox(width: 8),
                          Text(
                            item.price.toString(),
                            style: TextStyle(
                              color: Colors.yellow[600],
                              fontWeight: FontWeight.bold,
                              fontSize: 18,
                            ),
                          ),
                        ],
                      ),
          ),
        ),
        
        const SizedBox(width: 16),

        Expanded(
          child: ElevatedButton(
            style: ElevatedButton.styleFrom(
              backgroundColor: buttonColor,
              shape: buttonShape,
              padding: const EdgeInsets.symmetric(vertical: 12),
            ),
            onPressed: isBuying
                ? null
                : () {
                    Navigator.of(dialogContext).pop(); 
                  },
            child: const Text(
              'Cancel',
              style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.bold,
                fontSize: 18,
              ),
            ),
          ),
        ),
      ],
    );
  }  void _handleTabChange() {
    if (_tabController.indexIsChanging) return;
    
    setState(() {
      _currentTab = _tabController.index == 0 
          ? ShopTab.character 
          : ShopTab.scenery;
    });
    developer.log('Tab changed to: ${_currentTab.name}', name: 'ShopPage._handleTabChange');
  }

  Future<void> _loadShopData() async {
    try {
      developer.log('Loading shop data for user: ${widget.userId}', name: 'ShopPage._loadShopData');
      
      setState(() {
        _isLoading = true;
        _errorMessage = null;
      });

      // For demo purposes, using mock data fetch
      final shopData = await _shopService.fetchShopDataMock(widget.userId);
      // final shopData = await _shopService.fetchShopData(widget.userId);
      
      setState(() {
        _shopData = shopData;
        _isLoading = false;
      });

      _fadeController.forward();
      developer.log('Shop data loaded successfully', name: 'ShopPage._loadShopData');
      
    } catch (e) {
      developer.log('Error loading shop data: $e', name: 'ShopPage._loadShopData', error: e);
      
      setState(() {
        _errorMessage = e is ShopServiceException 
            ? e.message 
            : 'Failed to load shop data. Please try again.';
        _isLoading = false;
      });
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

  void _showSuccessSnackBar(String message) {
    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(message),
          backgroundColor: Colors.green[600], 
          behavior: SnackBarBehavior.floating,
        ),
      );
    }
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
                backgroundColor: _currentTab == ShopTab.character
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
                backgroundColor: _currentTab == ShopTab.scenery
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

  Widget _buildShopGrid() {
    final items = _currentTab == ShopTab.character
        ? _shopData?.unobtainedCharacters
            .where((item) => item.rarity.toLowerCase() != 'legendary')
            .cast<ShopItem>()
            .toList() ?? []
        : _shopData?.unobtainedSceneries
            .where((item) => item.rarity.toLowerCase() != 'legendary')
            .cast<ShopItem>()
            .toList() ?? [];

    return Container(
      decoration: BoxDecoration(
        color: const Color(0xFF2E77C3),
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(30),
          topRight: Radius.circular(30),
        ),
      ),
      child: ListView.builder(
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 20),
        itemCount: items.length,
        itemBuilder: (context, index) {
          final item = items[index];
          return Padding(
            padding: const EdgeInsets.only(bottom: 16), 
            child: ShopItemCard(
              item: item,
              isCharacter: _currentTab == ShopTab.character,
              onTap: () {
                _showItemPreviewPopup(item, _currentTab == ShopTab.character);
              },
              userId: widget.userId,
            ),
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
            'Loading Shop...',
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
            onPressed: _loadShopData,
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
                child: SafeArea(
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(top: 20),
                        child: _buildTabButtons(),
                      ),

                    const SizedBox(height: 20),
                    
                    Expanded(
                      child: _buildShopGrid(),
                    ),
                    CustomNavigationBar(
                      userId: widget.userId,
                      selectedIndex: 1, // 1 = shop
                    ),
                    ],
                  ),
                ),
              ),
    );
  }

  @override
  void dispose() {
    _tabController.dispose();
    _fadeController.dispose();
    developer.log('ShopPage disposed', name: 'ShopPage.dispose');
    super.dispose();
  }
}