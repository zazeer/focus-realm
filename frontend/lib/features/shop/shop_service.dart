import 'dart:convert';
import 'dart:developer' as developer;
import 'dart:io';
import 'package:http/http.dart' as http;
import 'shop_models.dart';

class ShopServiceException implements Exception {
  final String message;
  final String? errorCode;
  final dynamic originalError;

  const ShopServiceException({
    required this.message,
    this.errorCode,
    this.originalError,
  });

  @override
  String toString() {
    return 'ShopServiceException: $message${errorCode != null ? ' (Code: $errorCode)' : ''}';
  }
}

class ShopService {
  static const String _baseUrl = 'https://focus-realm-app.azurewebsites.net'; 
  static const Duration _defaultTimeout = Duration(seconds: 30);
  
  final http.Client _httpClient;
  final String baseUrl;
  final Duration timeout;

  ShopService({
    http.Client? httpClient,
    this.baseUrl = _baseUrl,
    this.timeout = _defaultTimeout,
  }) : _httpClient = httpClient ?? http.Client();

  Map<String, String> get _defaultHeaders => {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  };

  /// Fetch shop data from API
  Future<ShopPageModel> fetchShopData({
    required String userId,
    Map<String, String>? additionalHeaders,
  }) async {
    developer.log('Fetching shop data for user: $userId', name: 'ShopService.fetchShopData');
    
    try {
      final headers = {..._defaultHeaders};
      if (additionalHeaders != null) {
        headers.addAll(additionalHeaders);
      }

      final uri = Uri.parse('$baseUrl/api/shop/$userId');
      developer.log('Making GET request to: $uri', name: 'ShopService.fetchShopData');

      final response = await _httpClient
          .get(uri, headers: headers)
          .timeout(timeout);

      developer.log(
        'API Response - Status: ${response.statusCode}, Body length: ${response.body.length}',
        name: 'ShopService.fetchShopData',
      );

      if (response.statusCode == 200) {
        return await _parseShopResponse(response.body);
      } else {
        throw ShopServiceException(
          message: 'Failed to fetch shop data: ${response.statusCode}',
          errorCode: response.statusCode.toString(),
          originalError: response.body,
        );
      }
    } on SocketException catch (e) {
      developer.log(
        'Network error: $e',
        name: 'ShopService.fetchShopData',
        error: e,
      );
      throw ShopServiceException(
        message: 'Network error: Please check your internet connection',
        originalError: e,
      );
    } on FormatException catch (e) {
      developer.log(
        'JSON parsing error: $e',
        name: 'ShopService.fetchShopData',
        error: e,
      );
      throw ShopServiceException(
        message: 'Invalid response format from server',
        originalError: e,
      );
    } catch (e) {
      developer.log(
        'Unexpected error: $e',
        name: 'ShopService.fetchShopData',
        error: e,
      );
      throw ShopServiceException(
        message: 'Failed to fetch shop data: $e',
        originalError: e,
      );
    }
  }

  /// Parse shop response from JSON
  Future<ShopPageModel> _parseShopResponse(String responseBody) async {
    try {
      developer.log('Parsing shop response', name: 'ShopService._parseShopResponse');
      
      final Map<String, dynamic> jsonData = json.decode(responseBody);
      final apiResponse = ShopApiResponse.fromJson(jsonData);
      
      if (!apiResponse.isSuccess) {
        throw ShopServiceException(
          message: apiResponse.errorMessage,
          errorCode: apiResponse.errorCode,
        );
      }

      if (apiResponse.shopPageModel == null) {
        throw ShopServiceException(
          message: 'Shop data is null in API response',
          errorCode: apiResponse.errorCode,
        );
      }

      developer.log(
        'Successfully parsed shop data: ${apiResponse.shopPageModel}',
        name: 'ShopService._parseShopResponse',
      );

      return apiResponse.shopPageModel!;
    } catch (e) {
      if (e is ShopServiceException) rethrow;
      
      developer.log(
        'Error parsing shop response: $e',
        name: 'ShopService._parseShopResponse',
        error: e,
      );
      throw ShopServiceException(
        message: 'Failed to parse shop data: $e',
        originalError: e,
      );
    }
  }

  /// Mock method untuk testing 
  Future<ShopPageModel> fetchShopDataMock(String userId) async {
    developer.log('Using mock data for user: $userId', name: 'ShopService.fetchShopDataMock');
    
    // Simulate network delay
    await Future.delayed(Duration(milliseconds: 500));

    const mockResponse = '''
    {
      "errorCode": "200",
      "errorMessage": "Success",
      "shopPageModel": {
        "user_id": "U004",
        "unobtainedCharacter": [
          {
            "character_id": "CH004",
            "character_name": "The King",
            "character_rarity": "Rare",
            "character_description": "The Real King",
            "price": 2000,
            "release_date": "2025-08-10",
            "file_name": "The_King.gif"
          },
          {
            "character_id": "CH005",
            "character_name": "Shadow Ninja",
            "character_rarity": "Epic",
            "character_description": "Moves in the shadows",
            "price": 3500,
            "release_date": "2025-09-01",
            "file_name": "Shadow_Ninja.gif"
          },
          {
            "character_id": "CH006",
            "character_name": "Aqua Mage",
            "character_rarity": "Common",
            "character_description": "Controls water",
            "price": 500,
            "release_date": "2025-09-15",
            "file_name": "Aqua_Mage.gif"
          }
        ],
        "unobtainedScenery": [
          {
            "scenery_id": "SC003",
            "scenery_name": "Savana",
            "scenery_rarity": "Legendary",
            "scenery_description": "Huge Savana",
            "price": 1500,
            "release_date": "2025-08-03",
            "file_name": "Savana.gif"
          },
          {
            "scenery_id": "SC004",
            "scenery_name": "Cyber City",
            "scenery_rarity": "Epic",
            "scenery_description": "A futuristic metropolis",
            "price": 4000,
            "release_date": "2025-10-01",
            "file_name": "Cyber_City.gif"
          },
          {
            "scenery_id": "SC005",
            "scenery_name": "Enchanted Forest",
            "scenery_rarity": "Rare",
            "scenery_description": "A forest full of magic",
            "price": 2500,
            "release_date": "2025-10-10",
            "file_name": "Enchanted_Forest.gif"
          }
        ],
        "obtainedCharacter": [
          {
            "character_id": "CH003",
            "character_name": "Labubu_1",
            "character_rarity": "Legendary",
            "character_description": "The Real Machmoud",
            "price": 1500,
            "release_date": "2025-08-03",
            "file_name": "Labubu_1.gif",
            "acquire_date": "2025-08-18",
            "chosen_character": true
          },
          {
            "character_id": "CH001",
            "character_name": "Labubu_2",
            "character_rarity": "Common",
            "character_description": "The default character",
            "price": 0,
            "release_date": "2025-08-01",
            "file_name": "Labubu_2.gif",
            "acquire_date": "2025-08-01",
            "chosen_character": false
          },
          {
            "character_id": "CH002",
            "character_name": "Labubu_3",
            "character_rarity": "Rare",
            "character_description": "A knight with a flaming sword",
            "price": 1000,
            "release_date": "2025-08-02",
            "file_name": "Labubu_3.gif",
            "acquire_date": "2025-08-20",
            "chosen_character": false
          }
        ],
        "obtainedScenery": [
          {
            "scenery_id": "SC002",
            "scenery_name": "Mountains",
            "scenery_rarity": "Rare",
            "scenery_description": "Huge Mountains",
            "price": 1000,
            "release_date": "2025-08-03",
            "file_name": "Mountain.gif",
            "acquire_date": "2025-08-18",
            "chosen_scenery": true
          },
          {
            "scenery_id": "SC001",
            "scenery_name": "Default Room",
            "scenery_rarity": "Common",
            "scenery_description": "The starting room",
            "price": 0,
            "release_date": "2025-08-01",
            "file_name": "Default_Room.gif",
            "acquire_date": "2025-08-01",
            "chosen_scenery": false
          },
          {
            "scenery_id": "SC006",
            "scenery_name": "Beach Sunset",
            "scenery_rarity": "Rare",
            "scenery_description": "A relaxing beach",
            "price": 1000,
            "release_date": "2025-08-05",
            "file_name": "Beach.gif",
            "acquire_date": "2025-09-05",
            "chosen_scenery": false
          }
        ]
      }
    }
    ''';

    return await _parseShopResponse(mockResponse);
  }

  /// Dispose resources
  void dispose() {
    _httpClient.close();
    developer.log('ShopService disposed', name: 'ShopService.dispose');
  }
}