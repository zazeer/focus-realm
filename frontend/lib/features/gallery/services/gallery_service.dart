import 'dart:convert';
import 'dart:developer' as developer;
import 'dart:io';
import 'package:http/http.dart' as http;
import '../models/gallery_models.dart';

/// Exception untuk Gallery Service
class GalleryServiceException implements Exception {
  final String message;
  final String? errorCode;
  final dynamic originalError;

  const GalleryServiceException({
    required this.message,
    this.errorCode,
    this.originalError,
  });

  @override
  String toString() {
    return 'GalleryServiceException: $message${errorCode != null ? ' (Code: $errorCode)' : ''}';
  }
}

/// Service untuk berkomunikasi dengan Gallery API
class GalleryService {
  static const String _baseUrl = 'https://api-base-url.com'; // Ganti dengan URL API beneran nanti
  static const Duration _defaultTimeout = Duration(seconds: 30);
  
  final http.Client _httpClient;
  final String baseUrl;
  final Duration timeout;

  GalleryService({
    http.Client? httpClient,
    this.baseUrl = _baseUrl,
    this.timeout = _defaultTimeout,
  }) : _httpClient = httpClient ?? http.Client();

  /// Headers default untuk request
  Map<String, String> get _defaultHeaders => {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  };

  /// Fetch gallery data from API
  Future<GalleryPageModel> fetchGalleryData({
    required String userId,
    Map<String, String>? additionalHeaders,
  }) async {
    developer.log('Fetching gallery data for user: $userId', name: 'GalleryService.fetchGalleryData');
    
    try {
      final headers = {..._defaultHeaders};
      if (additionalHeaders != null) {
        headers.addAll(additionalHeaders);
      }

      final uri = Uri.parse('$baseUrl/api/gallery/$userId');
      developer.log('Making GET request to: $uri', name: 'GalleryService.fetchGalleryData');

      final response = await _httpClient
          .get(uri, headers: headers)
          .timeout(timeout);

      developer.log(
        'API Response - Status: ${response.statusCode}, Body length: ${response.body.length}',
        name: 'GalleryService.fetchGalleryData',
      );

      if (response.statusCode == 200) {
        return await _parseGalleryResponse(response.body);
      } else {
        throw GalleryServiceException(
          message: 'Failed to fetch gallery data: ${response.statusCode}',
          errorCode: response.statusCode.toString(),
          originalError: response.body,
        );
      }
    } on SocketException catch (e) {
      developer.log(
        'Network error: $e',
        name: 'GalleryService.fetchGalleryData',
        error: e,
      );
      throw GalleryServiceException(
        message: 'Network error: Please check your internet connection',
        originalError: e,
      );
    } on FormatException catch (e) {
      developer.log(
        'JSON parsing error: $e',
        name: 'GalleryService.fetchGalleryData',
        error: e,
      );
      throw GalleryServiceException(
        message: 'Invalid response format from server',
        originalError: e,
      );
    } catch (e) {
      developer.log(
        'Unexpected error: $e',
        name: 'GalleryService.fetchGalleryData',
        error: e,
      );
      throw GalleryServiceException(
        message: 'Failed to fetch gallery data: $e',
        originalError: e,
      );
    }
  }

  /// Parse gallery response from JSON
  Future<GalleryPageModel> _parseGalleryResponse(String responseBody) async {
    try {
      developer.log('Parsing gallery response', name: 'GalleryService._parseGalleryResponse');
      
      final Map<String, dynamic> jsonData = json.decode(responseBody);
      final apiResponse = GalleryApiResponse.fromJson(jsonData);
      
      if (!apiResponse.isSuccess) {
        throw GalleryServiceException(
          message: apiResponse.errorMessage,
          errorCode: apiResponse.errorCode,
        );
      }

      if (apiResponse.galleryPageModel == null) {
        throw GalleryServiceException(
          message: 'Gallery data is null in API response',
          errorCode: apiResponse.errorCode,
        );
      }

      developer.log(
        'Successfully parsed gallery data: ${apiResponse.galleryPageModel}',
        name: 'GalleryService._parseGalleryResponse',
      );

      return apiResponse.galleryPageModel!;
    } catch (e) {
      if (e is GalleryServiceException) rethrow;
      
      developer.log(
        'Error parsing gallery response: $e',
        name: 'GalleryService._parseGalleryResponse',
        error: e,
      );
      throw GalleryServiceException(
        message: 'Failed to parse gallery data: $e',
        originalError: e,
      );
    }
  }

  /// Update character selection
  Future<bool> updateCharacterSelection({
    required String userId,
    required String characterId,
    Map<String, String>? additionalHeaders,
  }) async {
    developer.log(
      'Updating character selection - User: $userId, Character: $characterId',
      name: 'GalleryService.updateCharacterSelection',
    );
    
    try {
      final headers = {..._defaultHeaders};
      if (additionalHeaders != null) {
        headers.addAll(additionalHeaders);
      }

      final uri = Uri.parse('$baseUrl/api/gallery/$userId/character/select');
      final body = json.encode({'character_id': characterId});

      developer.log('Making PUT request to: $uri', name: 'GalleryService.updateCharacterSelection');

      final response = await _httpClient
          .put(uri, headers: headers, body: body)
          .timeout(timeout);

      developer.log(
        'Character selection API Response - Status: ${response.statusCode}',
        name: 'GalleryService.updateCharacterSelection',
      );

      if (response.statusCode == 200) {
        final responseData = json.decode(response.body);
        return responseData['errorCode'] == '200';
      } else {
        throw GalleryServiceException(
          message: 'Failed to update character selection: ${response.statusCode}',
          errorCode: response.statusCode.toString(),
        );
      }
    } catch (e) {
      if (e is GalleryServiceException) rethrow;
      
      developer.log(
        'Error updating character selection: $e',
        name: 'GalleryService.updateCharacterSelection',
        error: e,
      );
      throw GalleryServiceException(
        message: 'Failed to update character selection: $e',
        originalError: e,
      );
    }
  }

  /// Update scenery selection
  Future<bool> updateScenerySelection({
    required String userId,
    required String sceneryId,
    Map<String, String>? additionalHeaders,
  }) async {
    developer.log(
      'Updating scenery selection - User: $userId, Scenery: $sceneryId',
      name: 'GalleryService.updateScenerySelection',
    );
    
    try {
      final headers = {..._defaultHeaders};
      if (additionalHeaders != null) {
        headers.addAll(additionalHeaders);
      }

      final uri = Uri.parse('$baseUrl/api/gallery/$userId/scenery/select');
      final body = json.encode({'scenery_id': sceneryId});

      developer.log('Making PUT request to: $uri', name: 'GalleryService.updateScenerySelection');

      final response = await _httpClient
          .put(uri, headers: headers, body: body)
          .timeout(timeout);

      developer.log(
        'Scenery selection API Response - Status: ${response.statusCode}',
        name: 'GalleryService.updateScenerySelection',
      );

      if (response.statusCode == 200) {
        final responseData = json.decode(response.body);
        return responseData['errorCode'] == '200';
      } else {
        throw GalleryServiceException(
          message: 'Failed to update scenery selection: ${response.statusCode}',
          errorCode: response.statusCode.toString(),
        );
      }
    } catch (e) {
      if (e is GalleryServiceException) rethrow;
      
      developer.log(
        'Error updating scenery selection: $e',
        name: 'GalleryService.updateScenerySelection',
        error: e,
      );
      throw GalleryServiceException(
        message: 'Failed to update scenery selection: $e',
        originalError: e,
      );
    }
  }

  /// Mock method untuk testing (menggunakan data dari spesifikasi)
  Future<GalleryPageModel> fetchGalleryDataMock(String userId) async {
    developer.log('Using mock data for user: $userId', name: 'GalleryService.fetchGalleryDataMock');
    
    // Simulate network delay
    await Future.delayed(Duration(milliseconds: 500));

    const mockResponse = '''
    {
      "errorCode": "200",
      "errorMessage": "Success",
      "galleryPageModel": {
        "user_id": "U004",
        "unobtainedCharacter": [
          {
            "character_id": "CH004",
            "character_name": "The King",
            "character_rarity": "Rare",
            "character_description": "The Real King",
            "price": 2000,
            "release_date": "2025-08-10",
            "file_name": "The_King.riv"
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
            "file_name": "Savana.riv"
          }
        ],
        "obtainedCharacter": [
          {
            "character_id": "CH003",
            "character_name": "Machmoud Slayer",
            "character_rarity": "Legendary",
            "character_description": "The Real Machmoud",
            "price": 1500,
            "release_date": "2025-08-03",
            "file_name": "Machmoud_slayer.riv",
            "acquire_date": "2025-08-18",
            "chosen_character": true
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
            "file_name": "Mountain.riv",
            "acquire_date": "2025-08-18",
            "chosen_scenery": true
          }
        ]
      }
    }
    ''';

    return await _parseGalleryResponse(mockResponse);
  }

  /// Dispose resources
  void dispose() {
    _httpClient.close();
    developer.log('GalleryService disposed', name: 'GalleryService.dispose');
  }
}