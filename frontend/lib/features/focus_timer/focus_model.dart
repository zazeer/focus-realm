import 'dart:developer' as developer;
import 'dart:math';

import 'package:flutter/widgets.dart';

class FocusMusic{
  final String music_id;
  final String music_title;
  final String music_file_name;
  final int music_duration;

  FocusMusic({
    required this.music_id,
    required this.music_title,
    required this.music_file_name,
    required this.music_duration
  });

  Map<String, dynamic> toJson() {
    return {
      'music_id': music_id,
      'music_title': music_title,
      'music_file_name': music_file_name,
      'music_duration': music_duration,
    };
  }
  factory FocusMusic.fromJson(Map<String, dynamic> json) {
    return FocusMusic(
      music_id: json['music_id'],
      music_title: json['music_title'],
      music_file_name: json['music_file_name'],
      music_duration: json['music_duration'],
    );
  }
}

class FocusAmbient{
  final String ambient_id;
  final String ambient_title;
  final String ambient_file_name;

  FocusAmbient({
    required this.ambient_id,
    required this.ambient_title,
    required this.ambient_file_name,
  });

  Map<String, dynamic> toJson() {
    return {
      'ambient_id': ambient_id,
      'ambient_title': ambient_title,
      'ambient_file_name': ambient_file_name,
    };
  }
  factory FocusAmbient.fromJson(Map<String, dynamic> json) {
    return FocusAmbient(
      ambient_id: json['ambient_id'],
      ambient_title: json['ambient_title'],
      ambient_file_name: json['ambient_file_name'],
    );
  }
}


class FocusTimerModel {
  String? user_id;
  String? scenery_id;
  String? scenery_name;
  String? scenery_file_name;
  String? character_id;
  String? character_name;
  String? character_file_name;
  FocusMusic? currentMusicModel;
  FocusAmbient? currentAmbientModel;
  List<FocusMusic> allMusicList;
  List<FocusAmbient> allAmbientList;

  FocusTimerModel({
    required this.user_id,
    required this.scenery_id,
    required this.scenery_name,
    required this.scenery_file_name,
    required this.character_id,
    required this.character_name,
    required this.character_file_name,
    this.currentMusicModel,
    this.currentAmbientModel,
    this.allMusicList,
    this.allAmbientList,
  });

  Map<String, dynamic> toJson() {
    return {
      'user_id': user_id,
      'scenery_id': scenery_id,
      'scenery_name': scenery_name,
      'scenery_file_name': scenery_file_name,
      'character_id': character_id,
      'character_name': character_name,
      'character_file_name': character_file_name,
      'currentMusicModel': currentMusicModel?.toJson(),
      'currentAmbientModel': currentAmbientModel?.toJson(),
      'allMusicList': allMusicList.map((e) => e.toJson()).toList(),
      'allAmbientList': allAmbientList.map((e) => e.toJson()).toList(),
    };
  }

  factory FocusTimerModel.fromJson(Map<String, dynamic> json) {
    List<FocusMusic>? musicList(dynamic v){
      if (v is List) return v.map((e) => FocusMusic.fromJson(e)).toList();
      return null;
    }
    List<FocusAmbient>? ambientList(dynamic v){
      if (v is List) return v.map((e) => FocusAmbient.fromJson(e)).toList();
      return null;
    }
    return FocusTimerModel(
      user_id: json['user_id'],
      scenery_id: json['scenery_id'],
      scenery_name: json['scenery_name'],
      scenery_file_name: json['scenery_file_name'],
      character_id: json['character_id'],
      character_name: json['character_name'],
      character_file_name: json['character_file_name'],
      currentMusicModel: json['currentMusicModel'] != null? FocusMusic.fromJson(json['currentMusicModel']) : null,
      currentAmbientModel: json['currentAmbientModel'] != null? FocusAmbient.fromJson(json['currentAmbientModel']) : null,
      allMusicList: musicList(json['allMusicList']) ?? [],
      allAmbientList: ambientList(json['allAmbientList']) ?? [],
    );
  }
}



// API Response Model
class FocusPageResponseModel {
  final String message;
  final int statusCode;
  final FocusTimerModel? focusTimerModel;
  FocusPageResponseModel({
    required this.message,
    required this.statusCode,
    this.focusTimerModel,
  });
  factory FocusPageResponseModel.fromJson(Map<String, dynamic> json) {
    return FocusPageResponseModel(
      message: json['message'],
      statusCode: json['statusCode'],
      focusTimerModel: json['focusTimerModel'] != null ? FocusTimerModel.fromJson(Map<String, dynamic>.from(json['focusTimerModel'])) : null,
    );
  }
  
}