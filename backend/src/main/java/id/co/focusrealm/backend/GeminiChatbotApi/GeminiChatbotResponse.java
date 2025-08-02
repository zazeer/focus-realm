package id.co.focusrealm.backend.GeminiChatbotApi;

import id.co.focusrealm.backend.Common.BaseResponse;
import lombok.Data;

@Data
public class GeminiChatbotResponse extends BaseResponse {
    GeminiChatbotModel geminiChatbotModel;
}
