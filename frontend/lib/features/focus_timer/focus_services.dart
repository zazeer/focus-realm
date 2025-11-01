import 'dart:convert';
import 'dart:developer' as developer;
import 'dart:io';
import 'package:http/http.dart' as http;
import 'focus_model.dart';

class FocusServiceException implements Exception {
  final String message;
  final String? errorCode;
  final dynamic originalError;

  const FocusServiceException({
    required this.message,
    this.errorCode,
    this.originalError,
  });

  @override
  String toString() {
    return 'FocusServiceException: $message${errorCode != null ? ' (Code: $errorCode)' : ''}';
  }
}

class FocusTimerService{
  static const String baseUrl = 'https://focus-realm-app.azurewebsites.net';
  static const Duration timeoutDuration = Duration(seconds: 10);

  static const Map<String, String> headers = {
    'Content-Type': 'application/json',
  };

  static Future<FocusTimerModel> fetchFocusTimerModel(String userId) async {
  
    final uri = Uri.parse('$baseUrl/focus_timer/fetch_timer_page_data_by_user_id');
    try {
      final resp = await http.post(
        uri,
        headers: headers,
        body: jsonEncode({'user_id': userId}),
      ).timeout(timeoutDuration);

      if (resp.statusCode == 200) {
        final Map<String, dynamic> body = jsonDecode(resp.body);
        if (body['errorCode'] == '200' || body['errorCode'] == '0') {
          return FocusTimerModel.fromJson(Map<String, dynamic>.from(body['focus_timer']));
        } else {
          throw FocusServiceException(
            message: body['errorMessage'] ?? 'Unknown error from server',
            errorCode: body['errorCode']?.toString(),
          );
        }
      } else {
        throw FocusServiceException(
          message: 'HTTP ${resp.statusCode}',
        );
      }
    } on SocketException catch (e) {
      throw FocusServiceException(
        message: 'Network error: ${e.message}',
        originalError: e,
      );
    } on TimeoutException catch (e) {
      throw FocusServiceException(
        message: 'Request timed out after ${timeoutDuration.inSeconds} seconds',
        originalError: e,
      );
    } catch (e) {
      throw FocusServiceException(
        message: 'Unexpected error: $e',
        originalError: e,
      );
    }
  }

}