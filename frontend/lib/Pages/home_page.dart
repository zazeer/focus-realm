import 'package:flutter_svg/flutter_svg.dart';
import 'package:flutter/material.dart';
import 'package:frontend/API/userapiauth.dart';

class HomePage extends StatefulWidget {
  final String userId;
  
  const HomePage({Key? key, required this.userId}) : super(key: key);

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  HomePageModel? homePageData;
  bool isLoading = true;
  
  String selectedTimer = 'Pomodoro long';
  int selectedMinutes = 25;
  bool isTimerRunning = false;
  
  final List<String> timerTypes = ['Pomodoro long', 'Pomodoro short', 'Custom'];
  final List<int> minuteOptions = [5, 25, 60];

  @override
  void initState() {
    super.initState();
    _loadHomePageData();
  }

  Future<void> _loadHomePageData() async {
    final data = await UserApiService.fetchHomePageData(widget.userId);
    if (mounted) {
      setState(() {
        homePageData = data;
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    }

    return Scaffold(
      backgroundColor: const Color(0xFFFEF9FF),
      body: SafeArea(
        child: Column(
          children: [
            // Header dengan background gradient dan game scene
            Container(
              width: double.infinity,
              height: MediaQuery.of(context).size.height * 0.6,
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  colors: [Color(0xFF4C9EEB), Color(0xFF446AD4), Color(0xFF7A4AFE)],
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                ),
                borderRadius: BorderRadius.only(
                  bottomLeft: Radius.circular(30),
                  bottomRight: Radius.circular(30),
                ),
              ),
              child: Stack(
                children: [
                  // Header title dan coins
                  Positioned(
                    top: 20,
                    left: 20,
                    right: 20,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          'FOCUSREALM',
                          style: TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                            color: Colors.white,
                            letterSpacing: 2,
                          ),
                        ),
                        const SizedBox(height: 10),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  'Hi, ${homePageData?.username ?? 'User'}!',
                                  style: const TextStyle(
                                    fontSize: 18,
                                    color: Colors.white,
                                  ),
                                ),
                                const SizedBox(height: 5),
                                const Text(
                                  'Ready to study?',
                                  style: TextStyle(
                                    fontSize: 14,
                                    color: Colors.white70,
                                  ),
                                ),
                              ],
                            ),
                            // Coins display
                            Container(
                              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                              decoration: BoxDecoration(
                                color: Colors.orange,
                                borderRadius: BorderRadius.circular(20),
                              ),
                              child: Row(
                                children: [
                                  const Icon(Icons.monetization_on, color: Colors.white, size: 16),
                                  const SizedBox(width: 4),
                                  Text(
                                    '${homePageData?.userCoins ?? 0} Coins',
                                    style: const TextStyle(
                                      color: Colors.white,
                                      fontWeight: FontWeight.bold,
                                      fontSize: 12,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                  
                  // Game Scene Container
                  Positioned(
                    bottom: 20,
                    left: 20,
                    right: 20,
                    child: Container(
                      height: 200,
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: const Color(0xFF87CEEB), // Sky blue background
                        borderRadius: BorderRadius.circular(20),
                        border: Border.all(color: Colors.white.withOpacity(0.3)),
                      ),
                      child: Stack(
                        children: [
                          // Background scenery placeholder
                          _buildSceneryPlaceholder(),
                          
                          // Character placeholder
                          Positioned(
                            bottom: 30,
                            left: MediaQuery.of(context).size.width * 0.4,
                            child: _buildCharacterPlaceholder(),
                          ),
                          
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),

            // Timer Control Section
            Expanded(
              child: Container(
                margin: const EdgeInsets.all(16),
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Color(0xFFF9FBFF),
                  borderRadius: BorderRadius.circular(20),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.1),
                      blurRadius: 10,
                      offset: const Offset(0, 5),
                    ),
                  ],
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Current ${homePageData?.username ?? 'User'}'s timer",
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w600,
                        color: Color(0xFF2E77C3),
                      ),
                    ),
                    const SizedBox(height: 15),
                    
                    // Timer selection container
                    Container(
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: const Color(0xFF4C9EEB),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Row(
                        children: [
                          // Timer type dropdown
                          Expanded(
                            flex: 2,
                            child: Container(
                              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                              decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: DropdownButton<String>(
                                value: selectedTimer,
                                underline: const SizedBox(),
                                isExpanded: true,
                                items: timerTypes.map((String value) {
                                  return DropdownMenuItem<String>(
                                    value: value,
                                    child: Text(value, style: const TextStyle(fontSize: 12)),
                                  );
                                }).toList(),
                                onChanged: (String? newValue) {
                                  if (newValue != null) {
                                    setState(() {
                                      selectedTimer = newValue;
                                    });
                                  }
                                },
                              ),
                            ),
                          ),
                          const SizedBox(width: 12),
                          
                          // Time options
                          ...minuteOptions.map((minutes) => Padding(
                            padding: const EdgeInsets.only(right: 8),
                            child: GestureDetector(
                              onTap: () {
                                setState(() {
                                  selectedMinutes = minutes;
                                });
                              },
                              child: Container(
                                width: 50,
                                padding: const EdgeInsets.all(8),
                                decoration: BoxDecoration(
                                  color: selectedMinutes == minutes 
                                      ? Colors.orange 
                                      : Colors.white,
                                  borderRadius: BorderRadius.circular(8),
                                ),
                                child: Column(
                                  children: [
                                    Icon(
                                      minutes == 25 ? Icons.schedule : 
                                      minutes == 5 ? Icons.timer : Icons.access_time,
                                      size: 16,
                                      color: selectedMinutes == minutes 
                                          ? Colors.white 
                                          : Colors.grey,
                                    ),
                                    Text(
                                      '${minutes}m',
                                      style: TextStyle(
                                        fontSize: 10,
                                        color: selectedMinutes == minutes 
                                            ? Colors.white 
                                            : Colors.grey,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          )).toList(),
                        ],
                      ),
                    ),
                    
                    const SizedBox(height: 20),
                    
                    // Start Timer Button
                    Row(
                      children: [
                        Expanded(
                          child: ElevatedButton(
                            onPressed: () {
                              setState(() {
                                isTimerRunning = !isTimerRunning;
                              });
                            },
                            style: ElevatedButton.styleFrom(
                              backgroundColor: const Color(0xFF4C9EEB),
                              padding: const EdgeInsets.symmetric(vertical: 15),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(12),
                              ),
                            ),
                            child: Text(
                              isTimerRunning ? 'Stop Timer' : 'Start Timer',
                              style: const TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.w600,
                                color: Colors.white,
                              ),
                            ),
                          ),
                        ),
                        const SizedBox(width: 12),
                        Container(
                          width: 50,
                          height: 50,
                          decoration: BoxDecoration(
                            color: Colors.black,
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: const Icon(
                            Icons.settings,
                            color: Color(0xFFF9FBFF),
                            size: 24,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),

            // Bottom Navigation
            Container(
              height: 70,
              padding: const EdgeInsets.symmetric(horizontal: 20),
              decoration: BoxDecoration(
                color: Color(0xFFF9FBFF),
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
                  _buildNavItem(Icons.explore, false),
                  _buildNavItem(Icons.shopping_bag, false),
                  _buildNavItem(Icons.home, true),
                  _buildNavItem(Icons.assessment, false),
                  _buildNavItem(Icons.person, false),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSceneryPlaceholder() {
    bool hasScenery = homePageData?.sceneryFileName != null && 
                      homePageData!.sceneryFileName!.isNotEmpty;
    
    return Container(
      width: double.infinity,
      height: double.infinity,
      decoration: BoxDecoration(
        color: hasScenery ? Colors.greenAccent : Colors.grey[300],
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: hasScenery ? Colors.green : Colors.grey,
          width: 2,
        ),
      ),
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.landscape,
              size: 40,
              color: hasScenery ? Colors.green[800] : Colors.grey[600],
            ),
            const SizedBox(height: 8),
            Text(
              hasScenery 
                  ? homePageData!.sceneryName ?? 'Scenery' 
                  : 'No Scenery',
              style: TextStyle(
                color: hasScenery ? Colors.green[800] : Colors.grey[600],
                fontWeight: FontWeight.bold,
                fontSize: 12,
              ),
            ),
            if (hasScenery)
              Text(
                homePageData!.sceneryFileName!,
                style: TextStyle(
                  color: Colors.green[600],
                  fontSize: 10,
                ),
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildCharacterPlaceholder() {
    bool hasCharacter = homePageData?.characterFileName != null && 
                        homePageData!.characterFileName!.isNotEmpty;
    
    return Container(
      width: 60,
      height: 80,
      decoration: BoxDecoration(
        color: hasCharacter ? Colors.greenAccent : Colors.grey[300],
        borderRadius: BorderRadius.circular(8),
        border: Border.all(
          color: hasCharacter ? Colors.green : Colors.grey,
          width: 2,
        ),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            Icons.person,
            size: 30,
            color: hasCharacter ? Colors.green[800] : Colors.grey[600],
          ),
          const SizedBox(height: 4),
          Text(
            hasCharacter 
                ? homePageData!.characterName ?? 'Character' 
                : 'No Char',
            style: TextStyle(
              color: hasCharacter ? Colors.green[800] : Colors.grey[600],
              fontWeight: FontWeight.bold,
              fontSize: 8,
            ),
            textAlign: TextAlign.center,
          ),
          if (hasCharacter)
            Text(
              homePageData!.characterFileName!,
              style: TextStyle(
                color: Colors.green[600],
                fontSize: 6,
              ),
              textAlign: TextAlign.center,
            ),
        ],
      ),
    );
  }

  Widget _buildCloud() {
    return Container(
      width: 40,
      height: 20,
      decoration: BoxDecoration(
        color: Colors.white.withOpacity(0.8),
        borderRadius: BorderRadius.circular(20),
      ),
    );
  }

  Widget _buildNavItem(IconData icon, bool isSelected) {
    return Container(
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        color: isSelected ? const Color(0xFF4C9EEB) : Colors.transparent,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Icon(
        icon,
        color: isSelected ? Color(0xFFF9FBFF) : Colors.grey,
        size: 28,
      ),
    );
  }
}