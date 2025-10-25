import 'dart:ui';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';

class CharacterModel{
  String? character_id;
  String? character_name;
  String? character_rarity;
  String? character_description;
  int? price;
  String? release_date;
  String? file_name;
  String? acquire_date;
  bool? chosen_character;

  CharacterModel({
    this.character_id,
    this.character_name,
    this.character_rarity,
    this.character_description,
    this.price,
    this.release_date,
    this.file_name,
    this.acquire_date,
    this.chosen_character,
  });

  factory CharacterModel.fromJson(Map<String, dynamic> json){
    return CharacterModel(
      character_id: json['character_id'],
      character_name: json['character_name'],
      character_rarity: json['character_rarity'],
      character_description: json['character_description'],
      price: json['price'],
      release_date: json['release_date'],
      file_name: json['file_name'],
      acquire_date: json['acquire_date'],
      chosen_character: json['chosen_character'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'character_id': character_id,
      'character_name': character_name,
      'character_rarity': character_rarity,
      'character_description': character_description,
      'price': price,
      'release_date': release_date,
      'file_name': file_name,
      'acquire_date': acquire_date,
      'chosen_character': chosen_character,
    };
  }
}

class SceneryModel {
  String? scenery_id;
  String? scenery_name;
  String? scenery_rarity;
  String? scenery_description;
  int? price;
  String? release_date;
  String? file_name;
  String? acquire_date;
  bool? chosen_scenery;

  SceneryModel({
    this.scenery_id,
    this.scenery_name,
    this.scenery_rarity,
    this.scenery_description,
    this.price,
    this.release_date,
    this.file_name,
    this.acquire_date,
    this.chosen_scenery,
  });

  factory SceneryModel.fromJson(Map<String, dynamic> json) {
    return SceneryModel(
      scenery_id: json['scenery_id'],
      scenery_name: json['scenery_name'],
      scenery_rarity: json['scenery_rarity'],
      scenery_description: json['scenery_description'],
      price: json['price'],
      release_date: json['release_date'],
      file_name: json['file_name'],
      acquire_date: json['acquire_date'],
      chosen_scenery: json['chosen_scenery'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'scenery_id': scenery_id,
      'scenery_name': scenery_name,
      'scenery_rarity': scenery_rarity,
      'scenery_description': scenery_description,
      'price': price,
      'release_date': release_date,
      'file_name': file_name,
      'acquire_date': acquire_date,
      'chosen_scenery': chosen_scenery,
    };
  }
}

class CustomizationPageModel {
  String? user_id;
  String? newUsedCharacterId;
  String? newUsedSceneryId;
  CharacterModel? currentlyUsedCharacter;
  SceneryModel? currentlyUsedScenery;
  List<CharacterModel>? unobtainedCharacter;
  List<SceneryModel>? unobtainedScenery;
  List<CharacterModel>? obtainedCharacter;
  List<SceneryModel>? obtainedScenery;

  CustomizationPageModel({
    this.user_id,
    this.newUsedCharacterId,
    this.newUsedSceneryId,
    this.currentlyUsedCharacter,
    this.currentlyUsedScenery,
    this.unobtainedCharacter,
    this.unobtainedScenery,
    this.obtainedCharacter,
    this.obtainedScenery,
  });
  factory CustomizationPageModel.fromJson(Map<String, dynamic> json) {
    List<CharacterModel>? parseCharList(dynamic v) {
      if (v is List) return v.map((e) => CharacterModel.fromJson(Map<String, dynamic>.from(e))).toList();
      return null;
    }

    List<SceneryModel>? parseSceneryList(dynamic v) {
      if (v is List) return v.map((e) => SceneryModel.fromJson(Map<String, dynamic>.from(e))).toList();
      return null;
    }

    return CustomizationPageModel(
      user_id: json['user_id'] as String?,
      newUsedCharacterId: json['newUsedCharacterId'] as String?,
      newUsedSceneryId: json['newUsedSceneryId'] as String?,
      currentlyUsedCharacter: json['currentlyUsedCharacter'] != null ? CharacterModel.fromJson(Map<String, dynamic>.from(json['currentlyUsedCharacter'])) : null,
      currentlyUsedScenery: json['currentlyUsedScenery'] != null ? SceneryModel.fromJson(Map<String, dynamic>.from(json['currentlyUsedScenery'])) : null,
      unobtainedCharacter: parseCharList(json['unobtainedCharacter']),
      unobtainedScenery: parseSceneryList(json['unobtainedScenery']),
      obtainedCharacter: parseCharList(json['obtainedCharacter']),
      obtainedScenery: parseSceneryList(json['obtainedScenery']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'user_id': user_id,
      'newUsedCharacterId': newUsedCharacterId,
      'newUsedSceneryId': newUsedSceneryId,
      'currentlyUsedCharacter': currentlyUsedCharacter?.toJson(),
      'currentlyUsedScenery': currentlyUsedScenery?.toJson(),
      'unobtainedCharacter': unobtainedCharacter?.map((e) => e.toJson()).toList(),
      'unobtainedScenery': unobtainedScenery?.map((e) => e.toJson()).toList(),
      'obtainedCharacter': obtainedCharacter?.map((e) => e.toJson()).toList(),
      'obtainedScenery': obtainedScenery?.map((e) => e.toJson()).toList(),
    };
  }
}

class CustomizationPageResponse {
  String? errorCode;
  String? errorMessage;
  CustomizationPageModel? customizationPageModel;

  CustomizationPageResponse({this.errorCode, this.errorMessage, this.customizationPageModel});

  factory CustomizationPageResponse.fromJson(Map<String, dynamic> json) {
    return CustomizationPageResponse(
      errorCode: json['errorCode']?.toString(),
      errorMessage: json['errorMessage']?.toString(),
      customizationPageModel: json['customizationPageModel'] != null ? CustomizationPageModel.fromJson(Map<String, dynamic>.from(json['customizationPageModel'])) : null,
    );
  }
}

class UserApiService{
  static const String baseUrl = 'http://localhost:8080';

   static Future<CustomizationPageModel?> fetchCustomizationPageData(String userId) async {
    final uri = Uri.parse('$baseUrl/customization_page/fetch_customization_page_by_user_id');
    final resp = await http.post(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'user_id': userId}),
    );

    if (resp.statusCode == 200) {
      final Map<String, dynamic> body = jsonDecode(resp.body);
      final response = CustomizationPageResponse.fromJson(body);
      if (response.errorCode == '200' || response.errorCode == '0') {
        return response.customizationPageModel;
      } else {
        throw Exception(response.errorMessage ?? 'Unknown error from server');
      }
    } else {
      throw Exception('HTTP ${resp.statusCode}');
    }
  }

  static Future<bool> updateUserSettings(String userId, String newCharacterId, String newSceneryId) async {
    final uri = Uri.parse('$baseUrl/customization_page/update_user_settings');
    final resp = await http.post(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'user_id': userId,
        'newUsedCharacterId': newCharacterId,
        'newUsedSceneryId': newSceneryId,
      }),
    );

    if (resp.statusCode == 200) {
      final Map<String, dynamic> body = jsonDecode(resp.body);
      final response = CustomizationPageResponse.fromJson(body);
      return response.errorCode == '200' || response.errorCode == '0';
    } else {
      throw Exception('HTTP ${resp.statusCode}');
    }
  }

}