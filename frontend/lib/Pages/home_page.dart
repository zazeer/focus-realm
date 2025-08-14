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
      backgroundColor: const Color(0xFF2E3440),
      body: SafeArea(
        child: Column(
          children: [
            // Header dengan FOCUSREALM title
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(20),
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  colors: [Color(0xFF4C9EEB), Color(0xFF5DADE2)],
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                ),
              ),
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
                      Text(
                        'Hi, ${homePageData?.username ?? 'User'}!',
                        style: const TextStyle(
                          fontSize: 18,
                          color: Colors.white,
                        ),
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
                  const SizedBox(height: 10),
                  const Text(
                    'Ready to study?',
                    style: TextStyle(
                      fontSize: 14,
                      color: Colors.white70,
                    ),
                  ),
                ],
              ),
            ),

            // Game Scene Area
            Expanded(
              flex: 2,
              child: Container(
                width: double.infinity,
                margin: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  gradient: const LinearGradient(
                    colors: [Color(0xFF87CEEB), Color(0xFF98D8E8)],
                    begin: Alignment.topCenter,
                    end: Alignment.bottomCenter,
                  ),
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Stack(
                  children: [
                    // Sky and clouds
                    Positioned.fill(
                      child: CustomPaint(
                        painter: SkyPainter(),
                      ),
                    ),
                    // Mountains background
                    Positioned(
                      bottom: 80,
                      left: 0,
                      right: 0,
                      child: CustomPaint(
                        painter: MountainPainter(),
                        size: const Size(double.infinity, 100),
                      ),
                    ),
                    // Ground
                    Positioned(
                      bottom: 0,
                      left: 0,
                      right: 0,
                      height: 80,
                      child: Container(
                        decoration: const BoxDecoration(
                          color: Color(0xFF8B4513),
                          borderRadius: BorderRadius.only(
                            bottomLeft: Radius.circular(20),
                            bottomRight: Radius.circular(20),
                          ),
                        ),
                      ),
                    ),
                    // Trees
                    Positioned(
                      bottom: 40,
                      left: 30,
                      child: CustomPaint(painter: TreePainter(), size: const Size(30, 40)),
                    ),
                    Positioned(
                      bottom: 40,
                      left: 80,
                      child: CustomPaint(painter: TreePainter(), size: const Size(25, 35)),
                    ),
                    Positioned(
                      bottom: 40,
                      right: 30,
                      child: CustomPaint(painter: TreePainter(), size: const Size(30, 40)),
                    ),
                    Positioned(
                      bottom: 40,
                      right: 80,
                      child: CustomPaint(painter: TreePainter(), size: const Size(25, 35)),
                    ),
                    // Character (pixel art style)
                    Positioned(
                      bottom: 80,
                      left: MediaQuery.of(context).size.width * 0.4,
                      child: Container(
                        width: 32,
                        height: 32,
                        child: CustomPaint(
                          painter: PixelCharacterPainter(),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),

            // Timer Control Section
            Expanded(
              flex: 1,
              child: Container(
                margin: const EdgeInsets.all(16),
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Current ${homePageData?.username ?? 'User'}'s timer",
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w600,
                        color: Colors.black87,
                      ),
                    ),
                    const SizedBox(height: 15),
                    
                    // Timer selection row
                    Container(
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: const Color(0xFF4C9EEB),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Row(
                        children: [
                          // Timer type dropdown
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: DropdownButton<String>(
                              value: selectedTimer,
                              underline: const SizedBox(),
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
                              // Handle timer start
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
                        const SizedBox(width: 10),
                        Container(
                          width: 50,
                          height: 50,
                          decoration: BoxDecoration(
                            color: Colors.black,
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: const Icon(
                            Icons.settings,
                            color: Colors.white,
                            size: 24,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),

            // Bottom Navigation (placeholder)
            Container(
              height: 70,
              padding: const EdgeInsets.symmetric(horizontal: 20),
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

  Widget _buildNavItem(IconData icon, bool isSelected) {
    return Container(
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        color: isSelected ? const Color(0xFF4C9EEB) : Colors.transparent,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Icon(
        icon,
        color: isSelected ? Colors.white : Colors.grey,
        size: 28,
      ),
    );
  }
}

// Custom painters untuk elemen visual
class SkyPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint();
    
    // Draw clouds
    paint.color = Colors.white.withOpacity(0.8);
    
    // Cloud 1
    canvas.drawCircle(const Offset(60, 40), 15, paint);
    canvas.drawCircle(const Offset(80, 40), 20, paint);
    canvas.drawCircle(const Offset(100, 40), 15, paint);
    
    // Cloud 2
    canvas.drawCircle(const Offset(200, 60), 12, paint);
    canvas.drawCircle(const Offset(215, 60), 15, paint);
    canvas.drawCircle(const Offset(230, 60), 12, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

class MountainPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint()..color = const Color(0xFF708090);
    
    final path = Path();
    path.moveTo(0, size.height);
    path.lineTo(size.width * 0.3, 20);
    path.lineTo(size.width * 0.6, 40);
    path.lineTo(size.width * 0.8, 10);
    path.lineTo(size.width, size.height);
    path.close();
    
    canvas.drawPath(path, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

class TreePainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final trunkPaint = Paint()..color = const Color(0xFF8B4513);
    final leafPaint = Paint()..color = const Color(0xFF228B22);
    
    // Draw trunk
    canvas.drawRect(
      Rect.fromLTWH(size.width * 0.4, size.height * 0.6, size.width * 0.2, size.height * 0.4),
      trunkPaint,
    );
    
    // Draw leaves (simple triangle)
    final leafPath = Path();
    leafPath.moveTo(size.width * 0.5, 0);
    leafPath.lineTo(0, size.height * 0.7);
    leafPath.lineTo(size.width, size.height * 0.7);
    leafPath.close();
    
    canvas.drawPath(leafPath, leafPaint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}

class PixelCharacterPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final paint = Paint();
    final pixelSize = size.width / 16; // 16x16 pixel character
    
    // Simple pixel character (link-like)
    // Head (beige/skin color)
    paint.color = const Color(0xFFFFDBAC);
    canvas.drawRect(Rect.fromLTWH(6 * pixelSize, 2 * pixelSize, 4 * pixelSize, 4 * pixelSize), paint);
    
    // Hat (green)
    paint.color = const Color(0xFF228B22);
    canvas.drawRect(Rect.fromLTWH(5 * pixelSize, 1 * pixelSize, 6 * pixelSize, 2 * pixelSize), paint);
    
    // Body (green tunic)
    paint.color = const Color(0xFF228B22);
    canvas.drawRect(Rect.fromLTWH(6 * pixelSize, 6 * pixelSize, 4 * pixelSize, 6 * pixelSize), paint);
    
    // Arms
    canvas.drawRect(Rect.fromLTWH(4 * pixelSize, 7 * pixelSize, 2 * pixelSize, 4 * pixelSize), paint);
    canvas.drawRect(Rect.fromLTWH(10 * pixelSize, 7 * pixelSize, 2 * pixelSize, 4 * pixelSize), paint);
    
    // Legs (brown)
    paint.color = const Color(0xFF8B4513);
    canvas.drawRect(Rect.fromLTWH(6 * pixelSize, 12 * pixelSize, 2 * pixelSize, 4 * pixelSize), paint);
    canvas.drawRect(Rect.fromLTWH(8 * pixelSize, 12 * pixelSize, 2 * pixelSize, 4 * pixelSize), paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => false;
}
  
