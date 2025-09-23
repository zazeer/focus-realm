import 'dart:developer' as developer;

/// Base class untuk semua gallery items
abstract class GalleryItem {
  final String id;
  final String name;
  final String rarity;
  final String description;
  final int price;
  final String releaseDate;
  final String fileName;
  final String? acquireDate;

  const GalleryItem({
    required this.id,
    required this.name,
    required this.rarity,
    required this.description,
    required this.price,
    required this.releaseDate,
    required this.fileName,
    this.acquireDate,
  });

  bool get isObtained => acquireDate != null;
  
  /// Get rarity color based on rarity level
  String get rarityColor {
    switch (rarity.toLowerCase()) {
      case 'legendary':
        return '#FFD700'; // Gold
      case 'rare':
        return '#9932CC'; // Purple
      case 'common':
        return '#C0C0C0'; // Silver
      default:
        return '#FFFFFF'; // White
    }
  }

  Map<String, dynamic> toJson();
  
  @override
  String toString() {
    return 'GalleryItem{id: $id, name: $name, rarity: $rarity, isObtained: $isObtained}';
  }
}

/// Model untuk Character
class Character extends GalleryItem {
  final bool chosenCharacter;

  const Character({
    required super.id,
    required super.name,
    required super.rarity,
    required super.description,
    required super.price,
    required super.releaseDate,
    required super.fileName,
    super.acquireDate,
    this.chosenCharacter = false,
  });

  factory Character.fromJson(Map<String, dynamic> json) {
    try {
      developer.log('Parsing character: ${json.toString()}', name: 'Character.fromJson');
      
      return Character(
        id: json['character_id']?.toString() ?? '',
        name: json['character_name']?.toString() ?? 'Unknown Character',
        rarity: json['character_rarity']?.toString() ?? 'Common',
        description: json['character_description']?.toString() ?? '',
        price: int.tryParse(json['price']?.toString() ?? '0') ?? 0,
        releaseDate: json['release_date']?.toString() ?? '',
        fileName: json['file_name']?.toString() ?? '',
        acquireDate: json['acquire_date']?.toString(),
        chosenCharacter: json['chosen_character'] == true,
      );
    } catch (e, stackTrace) {
      developer.log(
        'Error parsing character from JSON: $e',
        name: 'Character.fromJson',
        error: e,
        stackTrace: stackTrace,
      );
      rethrow;
    }
  }

  @override
  Map<String, dynamic> toJson() {
    return {
      'character_id': id,
      'character_name': name,
      'character_rarity': rarity,
      'character_description': description,
      'price': price,
      'release_date': releaseDate,
      'file_name': fileName,
      if (acquireDate != null) 'acquire_date': acquireDate,
      if (isObtained) 'chosen_character': chosenCharacter,
    };
  }

  Character copyWith({
    String? id,
    String? name,
    String? rarity,
    String? description,
    int? price,
    String? releaseDate,
    String? fileName,
    String? acquireDate,
    bool? chosenCharacter,
  }) {
    return Character(
      id: id ?? this.id,
      name: name ?? this.name,
      rarity: rarity ?? this.rarity,
      description: description ?? this.description,
      price: price ?? this.price,
      releaseDate: releaseDate ?? this.releaseDate,
      fileName: fileName ?? this.fileName,
      acquireDate: acquireDate ?? this.acquireDate,
      chosenCharacter: chosenCharacter ?? this.chosenCharacter,
    );
  }

  @override
  String toString() {
    return 'Character{id: $id, name: $name, rarity: $rarity, chosen: $chosenCharacter, isObtained: $isObtained}';
  }
}

/// Model untuk Scenery
class Scenery extends GalleryItem {
  final bool chosenScenery;

  const Scenery({
    required super.id,
    required super.name,
    required super.rarity,
    required super.description,
    required super.price,
    required super.releaseDate,
    required super.fileName,
    super.acquireDate,
    this.chosenScenery = false,
  });

  factory Scenery.fromJson(Map<String, dynamic> json) {
    try {
      developer.log('Parsing scenery: ${json.toString()}', name: 'Scenery.fromJson');
      
      return Scenery(
        id: json['scenery_id']?.toString() ?? '',
        name: json['scenery_name']?.toString() ?? 'Unknown Scenery',
        rarity: json['scenery_rarity']?.toString() ?? 'Common',
        description: json['scenery_description']?.toString() ?? '',
        price: int.tryParse(json['price']?.toString() ?? '0') ?? 0,
        releaseDate: json['release_date']?.toString() ?? '',
        fileName: json['file_name']?.toString() ?? '',
        acquireDate: json['acquire_date']?.toString(),
        chosenScenery: json['chosen_scenery'] == true,
      );
    } catch (e, stackTrace) {
      developer.log(
        'Error parsing scenery from JSON: $e',
        name: 'Scenery.fromJson',
        error: e,
        stackTrace: stackTrace,
      );
      rethrow;
    }
  }

  @override
  Map<String, dynamic> toJson() {
    return {
      'scenery_id': id,
      'scenery_name': name,
      'scenery_rarity': rarity,
      'scenery_description': description,
      'price': price,
      'release_date': releaseDate,
      'file_name': fileName,
      if (acquireDate != null) 'acquire_date': acquireDate,
      if (isObtained) 'chosen_scenery': chosenScenery,
    };
  }

  Scenery copyWith({
    String? id,
    String? name,
    String? rarity,
    String? description,
    int? price,
    String? releaseDate,
    String? fileName,
    String? acquireDate,
    bool? chosenScenery,
  }) {
    return Scenery(
      id: id ?? this.id,
      name: name ?? this.name,
      rarity: rarity ?? this.rarity,
      description: description ?? this.description,
      price: price ?? this.price,
      releaseDate: releaseDate ?? this.releaseDate,
      fileName: fileName ?? this.fileName,
      acquireDate: acquireDate ?? this.acquireDate,
      chosenScenery: chosenScenery ?? this.chosenScenery,
    );
  }

  @override
  String toString() {
    return 'Scenery{id: $id, name: $name, rarity: $rarity, chosen: $chosenScenery, isObtained: $isObtained}';
  }
}

/// Main Gallery Page Model
class GalleryPageModel {
  final String userId;
  final List<Character> unobtainedCharacters;
  final List<Scenery> unobtainedSceneries;
  final List<Character> obtainedCharacters;
  final List<Scenery> obtainedSceneries;

  const GalleryPageModel({
    required this.userId,
    required this.unobtainedCharacters,
    required this.unobtainedSceneries,
    required this.obtainedCharacters,
    required this.obtainedSceneries,
  });

  factory GalleryPageModel.fromJson(Map<String, dynamic> json) {
    try {
      developer.log('Parsing GalleryPageModel: ${json.toString()}', name: 'GalleryPageModel.fromJson');
      
      final galleryData = json['galleryPageModel'] as Map<String, dynamic>? ?? {};
      
      return GalleryPageModel(
        userId: galleryData['user_id']?.toString() ?? '',
        unobtainedCharacters: _parseCharacterList(galleryData['unobtainedCharacter']),
        unobtainedSceneries: _parseSceneryList(galleryData['unobtainedScenery']),
        obtainedCharacters: _parseCharacterList(galleryData['obtainedCharacter']),
        obtainedSceneries: _parseSceneryList(galleryData['obtainedScenery']),
      );
    } catch (e, stackTrace) {
      developer.log(
        'Error parsing GalleryPageModel from JSON: $e',
        name: 'GalleryPageModel.fromJson',
        error: e,
        stackTrace: stackTrace,
      );
      rethrow;
    }
  }

  static List<Character> _parseCharacterList(dynamic charactersData) {
    if (charactersData is! List) return [];
    
    return charactersData.map((json) {
      try {
        return Character.fromJson(json as Map<String, dynamic>);
      } catch (e) {
        developer.log('Failed to parse character: $e', name: 'GalleryPageModel._parseCharacterList');
        return null;
      }
    }).where((character) => character != null).cast<Character>().toList();
  }

  static List<Scenery> _parseSceneryList(dynamic sceneriesData) {
    if (sceneriesData is! List) return [];
    
    return sceneriesData.map((json) {
      try {
        return Scenery.fromJson(json as Map<String, dynamic>);
      } catch (e) {
        developer.log('Failed to parse scenery: $e', name: 'GalleryPageModel._parseSceneryList');
        return null;
      }
    }).where((scenery) => scenery != null).cast<Scenery>().toList();
  }

  /// Get all characters (obtained and unobtained)
  List<Character> get allCharacters => [...obtainedCharacters, ...unobtainedCharacters];
  
  /// Get all sceneries (obtained and unobtained)
  List<Scenery> get allSceneries => [...obtainedSceneries, ...unobtainedSceneries];
  
  /// Get currently chosen character
  Character? get chosenCharacter {
    try {
      return obtainedCharacters.firstWhere((char) => char.chosenCharacter);
    } catch (e) {
      developer.log('No chosen character found', name: 'GalleryPageModel.chosenCharacter');
      return obtainedCharacters.isNotEmpty ? obtainedCharacters.first : null;
    }
  }
  
  /// Get currently chosen scenery
  Scenery? get chosenScenery {
    try {
      return obtainedSceneries.firstWhere((scenery) => scenery.chosenScenery);
    } catch (e) {
      developer.log('No chosen scenery found', name: 'GalleryPageModel.chosenScenery');
      return obtainedSceneries.isNotEmpty ? obtainedSceneries.first : null;
    }
  }

  Map<String, dynamic> toJson() {
    return {
      'galleryPageModel': {
        'user_id': userId,
        'unobtainedCharacter': unobtainedCharacters.map((c) => c.toJson()).toList(),
        'unobtainedScenery': unobtainedSceneries.map((s) => s.toJson()).toList(),
        'obtainedCharacter': obtainedCharacters.map((c) => c.toJson()).toList(),
        'obtainedScenery': obtainedSceneries.map((s) => s.toJson()).toList(),
      }
    };
  }

  @override
  String toString() {
    return 'GalleryPageModel{userId: $userId, obtainedCharacters: ${obtainedCharacters.length}, obtainedSceneries: ${obtainedSceneries.length}, unobtainedCharacters: ${unobtainedCharacters.length}, unobtainedSceneries: ${unobtainedSceneries.length}}';
  }
}

/// API Response wrapper
class GalleryApiResponse {
  final String errorCode;
  final String errorMessage;
  final GalleryPageModel? galleryPageModel;

  const GalleryApiResponse({
    required this.errorCode,
    required this.errorMessage,
    this.galleryPageModel,
  });

  factory GalleryApiResponse.fromJson(Map<String, dynamic> json) {
    try {
      developer.log('Parsing GalleryApiResponse: ${json.toString()}', name: 'GalleryApiResponse.fromJson');
      
      return GalleryApiResponse(
        errorCode: json['errorCode']?.toString() ?? '500',
        errorMessage: json['errorMessage']?.toString() ?? 'Unknown error',
        galleryPageModel: json['galleryPageModel'] != null 
            ? GalleryPageModel.fromJson(json)
            : null,
      );
    } catch (e, stackTrace) {
      developer.log(
        'Error parsing GalleryApiResponse from JSON: $e',
        name: 'GalleryApiResponse.fromJson',
        error: e,
        stackTrace: stackTrace,
      );
      rethrow;
    }
  }

  bool get isSuccess => errorCode == '200';
  
  Map<String, dynamic> toJson() {
    return {
      'errorCode': errorCode,
      'errorMessage': errorMessage,
      if (galleryPageModel != null) ...galleryPageModel!.toJson(),
    };
  }

  @override
  String toString() {
    return 'GalleryApiResponse{errorCode: $errorCode, errorMessage: $errorMessage, hasData: ${galleryPageModel != null}}';
  }
}