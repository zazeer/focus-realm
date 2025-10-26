import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';

class UserModel {
  final String? user_Id;
  final String? music_Id;
  final String? ambient_Id;
  final String? timer_Id;
  final String? username;
  final String? email;
  final String? password;
  final int? coins;
  final int? pity;
  final DateTime? created_at;

  UserModel({
    this.user_Id,
    this.music_Id,
    this.ambient_Id,
    this.timer_Id,
    this.username,
    this.email,
    this.password,
    this.coins,
    this.pity,
    this.created_at,
  });

  Map<String, dynamic> toJson() {
    return {
      'user_Id': user_Id,
      'music_Id': music_Id,
      'ambient_Id': ambient_Id,
      'timer_Id': timer_Id,
      'username': username,
      'email': email,
      'password': password,
      'coins': coins,
      'pity': pity,
      'created_at': created_at?.toIso8601String(),
    };
  }

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      user_Id: json['user_Id'],
      music_Id: json['music_Id'],
      ambient_Id: json['ambient_Id'],
      timer_Id: json['timer_Id'],
      username: json['username'],
      email: json['email'],
      password: json['password'],
      coins: json['coins'],
      pity: json['pity'],
      created_at: json['created_at'] != null ? DateTime.tryParse(json['created_at']) : null,
    );
  }
}

class HomePageModel {
  final String userId;
  final String username;
  final int userCoins;
  final String sceneryId;
  final String sceneryName;
  final String sceneryFileName;
  final String characterId;
  final String characterName;
  final String characterFileName;

  HomePageModel({
    required this.userId,
    required this.username,
    required this.userCoins,
    required this.sceneryId,
    required this.sceneryName,
    required this.sceneryFileName,
    required this.characterId,
    required this.characterName,
    required this.characterFileName,
  });

  factory HomePageModel.fromJson(Map<String, dynamic> json) {
    return HomePageModel(
      userId: json['user_id'] ?? '',
      username: json['username'] ?? '',
      userCoins: json['user_coins'] ?? 0,
      sceneryId: json['scenery_id'] ?? '',
      sceneryName: json['scenery_name'] ?? '',
      sceneryFileName: json['scenery_file_name'] ?? '',
      characterId: json['character_id'] ?? '',
      characterName: json['character_name'] ?? '',
      characterFileName: json['character_file_name'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'user_id': userId,
      'username': username,
      'user_coins': userCoins,
      'scenery_id': sceneryId,
      'scenery_name': sceneryName,
      'scenery_file_name': sceneryFileName,
      'character_id': characterId,
      'character_name': characterName,
      'character_file_name': characterFileName,
    };
  }
}

// Response model
class UserResponse {
  final String? errorCode;
  final String? errorMessage;
  final UserModel? user;

  UserResponse({
    this.errorCode,
    this.errorMessage,
    this.user,
  });

  factory UserResponse.fromJson(Map<String, dynamic> json) {
    return UserResponse(
      errorCode: json['errorCode'],
      errorMessage: json['errorMessage'],
      user: json['user'] != null ? UserModel.fromJson(json['user']) : null,
    );
  }
}

class UserApiService {
  static final logger = Logger();
  static const String baseUrl = 'https://focus-realm-app.azurewebsites.net'; 
  
  static const Map<String, String> headers = {
    'Content-Type': 'application/json',
  };

  static Future<UserResponse> registerUser(UserModel user) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/user/insert_user'),
        headers: headers,
        body: jsonEncode(user.toJson()),
      );

      if (response.statusCode == 200) {
        final responseData = jsonDecode(response.body);
        return UserResponse.fromJson(responseData);
      } else {
        return UserResponse(
          errorCode: response.statusCode.toString(),
          errorMessage: 'Server error: ${response.statusCode}',
        );
      }
    } catch (e) {
      return UserResponse(
        errorCode: 'CONNECTION_ERROR',
        errorMessage: 'Connection error: $e',
      );
    }
  }

  // Login user
  static Future<UserResponse> loginUser(UserModel user) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/user/fetch_user'),
        headers: headers,
        body: jsonEncode(user.toJson()),
      );

      if (response.statusCode == 200) {
        final responseData = jsonDecode(response.body);
        return UserResponse.fromJson(responseData);
      } else {
        return UserResponse(
          errorCode: response.statusCode.toString(),
          errorMessage: 'Server error: ${response.statusCode}',
        );
      }
    } catch (e) {
      return UserResponse(
        errorCode: 'CONNECTION_ERROR',
        errorMessage: 'Connection error: $e',
      );
    }
  }

  static Future<HomePageModel?> fetchHomePageData(String userId) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/home_page/fetch_home_page_data_by_user_id'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'user_id': userId}),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        logger.i('Home page data: $data');
        return HomePageModel.fromJson(data);
      }
      return null;
    } catch (e) {
      print('Error fetching home page data: $e');
      return null;
    }
  }
}