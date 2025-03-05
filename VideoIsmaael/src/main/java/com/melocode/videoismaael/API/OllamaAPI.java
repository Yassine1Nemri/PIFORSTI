package com.melocode.videoismaael.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class OllamaAPI {
    private static final String API_URL = "http://localhost:11434/api/generate";

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
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Request body for Ollama
        String jsonInput = "{\"model\": \"llama2\", \"prompt\": \"" + message + "\"}";

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