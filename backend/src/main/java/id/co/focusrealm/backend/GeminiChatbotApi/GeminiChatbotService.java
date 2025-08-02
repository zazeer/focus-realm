package id.co.focusrealm.backend.GeminiChatbotApi;

import com.google.api.client.util.Value;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@Service
@Slf4j
public class GeminiChatbotService {

    private static final String API_KEY = "AIzaSyC3tNSpmONaf-UJQOuCGcEYKk6cfWK_T6Q";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    private String messageHolder = "Mulai Percakapan dengan Halo Saya Study Buddy Kamu!, Kamu berperan sebagai Study Buddy professional yang menguasai semua mata pelajaran. Kamu hanya boleh membahas materi pelajaran sekolah (SD, SMP, atau SMA) dan tidak diizinkan menjawab topik di luar itu. Jawaban kamu harus singkat, maksimal 100 kata (IMPORTANT JANGAN MELEBIHI), dan fokus pada inti materi.";

    public GeminiChatbotResponse userChat(GeminiChatbotModel model) throws IOException {
        OkHttpClient client = new OkHttpClient();
        GeminiChatbotResponse geminiChatbotResponse = new GeminiChatbotResponse();

        String requestBody = """
        {
            "contents": [{
                "parts": [{
                    "text": "%s"
                }]
            }]
        }
        """.formatted(messageHolder + model.getUserMessage());

        log.info(messageHolder);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(
                        requestBody,
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                model.setUserResponse(extractText(response.body().string()));
                geminiChatbotResponse.setErrorCode("200");
                geminiChatbotResponse.setErrorMessage("Success");
                geminiChatbotResponse.setGeminiChatbotModel(model);
                return geminiChatbotResponse;
            } else {
                geminiChatbotResponse.setErrorCode("400");
                geminiChatbotResponse.setErrorMessage("Failed");
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    public String extractText(String responseJson) {
        JSONObject jsonObject = new JSONObject(responseJson);
        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");
        JSONObject firstCandidateObject = candidatesArray.getJSONObject(0);
        JSONObject content = firstCandidateObject.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        JSONObject firstPart = parts.getJSONObject(0);
        return firstPart.getString("text");
    }


}
