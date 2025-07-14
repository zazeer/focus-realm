import 'dart:convert';
import 'package:http/http.dart' as http;

// Model untuk User
class UserModel {
  final String? username;
  final String? email;
  final String? password;
  final String? fullName;

  UserModel({
    this.username,
    this.email,
    this.password,
    this.fullName,
  });

  Map<String, dynamic> toJson() {
    return {
      'username': username,
      'email': email,
      'password': password,
      'fullName': fullName,
    };
  }

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      username: json['username'],
      email: json['email'],
      password: json['password'],
      fullName: json['fullName'],
    );
  }
}

// Model untuk Response
class UserResponse {
  final bool success;
  final String message;
  final UserModel? user;

  UserResponse({
    required this.success,
    required this.message,
    this.user,
  });

  factory UserResponse.fromJson(Map<String, dynamic> json) {
    return UserResponse(
      success: json['success'] ?? false,
      message: json['message'] ?? '',
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
          success: false,
          message: 'Server error: ${response.statusCode}',
        );
      }
    } catch (e) {
      return UserResponse(
        success: false,
        message: 'Connection error: $e',
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
          success: false,
          message: 'Server error: ${response.statusCode}',
        );
      }
    } catch (e) {
      return UserResponse(
        success: false,
        message: 'Connection error: $e',
      );
    }
  }
}