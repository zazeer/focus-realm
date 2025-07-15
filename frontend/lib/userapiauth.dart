import 'dart:convert';
import 'package:http/http.dart' as http;

// Model untuk User
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

// Model untuk Response
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

// Service untuk API calls
class UserApiService {
  // Ganti dengan URL backend kamu
  static const String baseUrl = 'http://localhost:8000'; 
  
  // Headers untuk request
  static const Map<String, String> headers = {
    'Content-Type': 'application/json',
  };

  // Fungsi untuk Register
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

  // Fungsi untuk Login
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
}