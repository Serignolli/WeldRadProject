package com.project.weldrad.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExternalAPI {

    public String analisysAPI(String imagePath) {
        String result = "";
        try {
            HttpClient client = HttpClient.newHttpClient();

            String json = "{\"image_path\": \"" + imagePath.replace("\\", "/") + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8000/api/analisys"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> responseMap = objectMapper.readValue(response.body(), Map.class);
                result = responseMap.get("resultado");
            } else {
                System.err.println("Erro na API: Status " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
