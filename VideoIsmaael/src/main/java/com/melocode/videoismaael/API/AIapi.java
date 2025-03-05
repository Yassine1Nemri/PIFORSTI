package com.melocode.videoismaael.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIapi {
    // Free API endpoint (replace with your chosen model)
    private static final String API_URL = "https://api-inference.huggingface.co/models/gpt2";

    // You can get a free API token by signing up at huggingface.co
    private static final String API_TOKEN = "YOUR_HUGGINGFACE_API_TOKEN";

    public static void main(String[] args) {
        try {
            String response = sendQuery("Hello, how are you today?");
            System.out.println("AI Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendQuery(String message) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_TOKEN);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Request body
        String jsonInput = "{\"inputs\": \"" + message + "\"}";

        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
            writer.write(jsonInput);
            writer.flush();
        }

        // Read response
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }
}