import 'package:flutter/material.dart';
import 'package:frontend/API/customizationapiauth.dart';
import 'package:frontend/Pages/home_page.dart';
import 'package:frontend/themes/app_colors.dart';

class CustomizationPage extends StatefulWidget {
  final String userId;

  const CustomizationPage({Key? key, required this.userId}) : super(key: key);

  @override
  State<CustomizationPage> createState() => _CustomizationPageState();
}

class _CustomizationPageState extends State<CustomizationPage> {
  CustomizationPageModel? customizationData;
  bool isLoading = true;
  
  // Timer settings
  String selectedTimerType = 'Long';
  int studyMinutes = 25;
  int breakMinutes = 5;
  int sessionCount = 1;
  
  // Selected items
  String? selectedCharacterId;
  String? selectedSceneryId;
  
  final List<String> timerTypes = ['Long', 'Short', 'Custom'];

  @override
  void initState() {
    super.initState();
    _loadCustomizationData();
  }

  Future<void> _loadCustomizationData() async {
    try {
      final data = await UserApiService.fetchCustomizationPageData(widget.userId);
      if (mounted) {
        setState(() {
          customizationData = data;
          // Set currently used items as selected
          selectedCharacterId = data?.currentlyUsedCharacter?.character_id;
          selectedSceneryId = data?.currentlyUsedScenery?.scenery_id;
          isLoading = false;
        });
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          isLoading = false;
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error loading customization data: $e')),
        );
      }
    }
  }

  void _updateTimerSettings(String timerType) {
    setState(() {
      selectedTimerType = timerType;
      switch (timerType) {
        case 'Long':
          studyMinutes = 25;
          breakMinutes = 5;
          break;
        case 'Short':
          studyMinutes = 12;
          breakMinutes = 3;
          break;
        case 'Custom':
          // Keep current values for custom
          break;
      }
    });
  }

  Future<void> _applySettings() async {
    if (selectedCharacterId == null || selectedSceneryId == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select both character and scenery')),
      );
      return;
    }

    try {
      // Update user settings (character and scenery)
      await UserApiService.updateUserSettings(
        widget.userId,
        selectedCharacterId!,
        selectedSceneryId!,
      );

      // Navigate back to home with timer settings
      if (mounted) {
        Navigator.pushAndRemoveUntil(
          context,
          MaterialPageRoute(
            builder: (context) => HomePage(
              userId: widget.userId,
              timerSettings: {
                'type': selectedTimerType,
                'studyMinutes': studyMinutes,
                'breakMinutes': breakMinutes,
                'sessionCount': sessionCount,
              },
            ),
          ),
          (route) => false,
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error applying settings: $e')),
        );
      }
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
      appBar: AppBar(
        title: const Text('Timer Customization'),
        backgroundColor: AppColors.primary,
        foregroundColor: AppColors.background,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Character selection
            _buildSectionTitle('Character'),
            const SizedBox(height: 12),
            _buildCharacterSelection(),
            
            const SizedBox(height: 24),
            
            // Scenery selection
            _buildSectionTitle('Scenery'),
            const SizedBox(height: 12),
            _buildScenerySelection(),
            
            const SizedBox(height: 24),
            
            // Timer Settings
            _buildSectionTitle('Timer Settings:'),
            const SizedBox(height: 12),
            _buildTimerTypeDropdown(),
            
            const SizedBox(height: 16),
            _buildTimerControls(),
            
            const SizedBox(height: 24),
            _buildSessionControl(),
            
            const SizedBox(height: 24),
            _buildTotalTimeDisplay(),
            
            const SizedBox(height: 32),
            _buildApplyButton(),
          ],
        ),
      ),
    );
  }

  Widget _buildSectionTitle(String title) {
    return Text(
      title,
      style: const TextStyle(
        fontSize: 18,
        fontWeight: FontWeight.bold,
        color: AppColors.primary,
      ),
    );
  }

  Widget _buildCharacterSelection() {
    List<CharacterModel> allCharacters = [];
    final seen = <String>{};

    // Add currently used character (if exists)
    if (customizationData?.currentlyUsedCharacter != null) {
      final c = customizationData!.currentlyUsedCharacter!;
      if (c.character_id != null) {
        seen.add(c.character_id!);
      }
      allCharacters.add(c);
    }

    // Add obtained characters, avoid duplicates
    if (customizationData?.obtainedCharacter != null) {
      for (final c in customizationData!.obtainedCharacter!) {
        if (c.character_id == null) continue;
        if (seen.contains(c.character_id)) continue;
        seen.add(c.character_id!);
        allCharacters.add(c);
      }
    }

    return Container(
      height: 100,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        itemCount: allCharacters.length,
        itemBuilder: (context, index) {
          final character = allCharacters[index];
          final isSelected = selectedCharacterId == character.character_id;
          
          return GestureDetector(
            onTap: () {
              setState(() {
                selectedCharacterId = character.character_id;
              });
            },
            child: Container(
              width: 80,
              margin: const EdgeInsets.only(right: 12),
              decoration: BoxDecoration(
                color: isSelected ? AppColors.primary : AppColors.background,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(
                  color: isSelected ? AppColors.primary : AppColors.light,
                  width: 2,
                ),
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.person,
                    size: 40,
                    color: isSelected ? AppColors.background : AppColors.primary,
                  ),
                  const SizedBox(height: 4),
                  Text(
                    character.character_name ?? 'Character',
                    style: TextStyle(
                      fontSize: 10,
                      color: isSelected ? AppColors.background : AppColors.primary,
                      fontWeight: FontWeight.bold,
                    ),
                    textAlign: TextAlign.center,
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildScenerySelection() {
    List<SceneryModel> allScenery = [];
    final seen = <String>{};

    // Add currently used scenery
    if (customizationData?.currentlyUsedScenery != null) {
      final s = customizationData!.currentlyUsedScenery!;
      if (s.scenery_id != null) seen.add(s.scenery_id!);
      allScenery.add(s);
    }

    // Add obtained scenery, avoid duplicates
    if (customizationData?.obtainedScenery != null) {
      for (final s in customizationData!.obtainedScenery!) {
        if (s.scenery_id == null) continue;
        if (seen.contains(s.scenery_id)) continue;
        seen.add(s.scenery_id!);
        allScenery.add(s);
      }
    }

    return Container(
      height: 100,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        itemCount: allScenery.length,
        itemBuilder: (context, index) {
          final scenery = allScenery[index];
          final isSelected = selectedSceneryId == scenery.scenery_id;
          
          return GestureDetector(
            onTap: () {
              setState(() {
                selectedSceneryId = scenery.scenery_id;
              });
            },
            child: Container(
              width: 80,
              margin: const EdgeInsets.only(right: 12),
              decoration: BoxDecoration(
                color: isSelected ? AppColors.primary : AppColors.background,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(
                  color: isSelected ? AppColors.primary : AppColors.light,
                  width: 2,
                ),
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.landscape,
                    size: 40,
                    color: isSelected ? AppColors.background : AppColors.primary,
                  ),
                  const SizedBox(height: 4),
                  Text(
                    scenery.scenery_name ?? 'Scenery',
                    style: TextStyle(
                      fontSize: 10,
                      color: isSelected ? AppColors.background : AppColors.primary,
                      fontWeight: FontWeight.bold,
                    ),
                    textAlign: TextAlign.center,
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildTimerTypeDropdown() {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      decoration: BoxDecoration(
        color: AppColors.primary,
        borderRadius: BorderRadius.circular(12),
      ),
      child: DropdownButton<String>(
        value: selectedTimerType,
        dropdownColor: AppColors.background,
        underline: const SizedBox(),
        isExpanded: true,
        style: const TextStyle(color: AppColors.background),
        items: timerTypes.map((String type) {
          return DropdownMenuItem<String>(
            value: type,
            child: Text(
              'Pomodoro $type',
              style: const TextStyle(color: AppColors.primary),
            ),
          );
        }).toList(),
        onChanged: (String? newValue) {
          if (newValue != null) {
            _updateTimerSettings(newValue);
          }
        },
      ),
    );
  }

  Widget _buildTimerControls() {
    return Column(
      children: [
        _buildTimeControl(
          'Study time:',
          Icons.school,
          studyMinutes,
          (value) {
            if (selectedTimerType == 'Custom') {
              setState(() {
                studyMinutes = value;
              });
            }
          },
          enabled: selectedTimerType == 'Custom',
        ),
        const SizedBox(height: 16),
        _buildTimeControl(
          'Break time:',
          Icons.hourglass_empty,
          breakMinutes,
          (value) {
            if (selectedTimerType == 'Custom') {
              setState(() {
                breakMinutes = value;
              });
            }
          },
          enabled: selectedTimerType == 'Custom',
        ),
      ],
    );
  }

  Widget _buildTimeControl(String label, IconData icon, int value, Function(int) onChanged, {bool enabled = true}) {
    return Row(
      children: [
        Icon(icon, color: AppColors.primary),
        const SizedBox(width: 12),
        Text(label, style: const TextStyle(fontSize: 16)),
        const Spacer(),
        if (enabled) ...[
          IconButton(
            onPressed: () {
              if (value > 1) onChanged(value - 1);
            },
            icon: const Icon(Icons.remove_circle_outline),
          ),
          Text('$value Minutes', style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          IconButton(
            onPressed: () {
              if (value < 120) onChanged(value + 1);
            },
            icon: const Icon(Icons.add_circle_outline),
          ),
        ] else ...[
          Text('$value Minutes', style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
        ],
      ],
    );
  }

  Widget _buildSessionControl() {
    return Row(
      children: [
        const Icon(Icons.repeat, color: AppColors.primary),
        const SizedBox(width: 12),
        const Text('Session amount:', style: TextStyle(fontSize: 16)),
        const Spacer(),
        IconButton(
          onPressed: () {
            if (sessionCount > 1) {
              setState(() {
                sessionCount--;
              });
            }
          },
          icon: const Icon(Icons.remove_circle_outline),
        ),
        Text('$sessionCount Sessions', style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
        IconButton(
          onPressed: () {
            if (sessionCount < 10) {
              setState(() {
                sessionCount++;
              });
            }
          },
          icon: const Icon(Icons.add_circle_outline),
        ),
      ],
    );
  }

  Widget _buildTotalTimeDisplay() {
    int totalMinutes = (studyMinutes + breakMinutes) * sessionCount;
    int hours = totalMinutes ~/ 60;
    int minutes = totalMinutes % 60;
    
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: AppColors.light.withOpacity(0.1),
        borderRadius: BorderRadius.circular(12),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          const Text(
            'Total time:',
            style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
          ),
          Text(
            hours > 0 ? '$hours Hour $minutes Minutes' : '$minutes Minutes',
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: AppColors.primary),
          ),
        ],
      ),
    );
  }

  Widget _buildApplyButton() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          colors: [AppColors.primary, AppColors.secondary],
        ),
        borderRadius: BorderRadius.circular(12),
      ),
      child: ElevatedButton(
        onPressed: _applySettings,
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.transparent,
          shadowColor: Colors.transparent,
          padding: const EdgeInsets.symmetric(vertical: 16),
        ),
        child: const Text(
          'Apply Setting',
          style: TextStyle(
            color: AppColors.background,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}