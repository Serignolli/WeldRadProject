package com.project.weldrad.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

public class ExternalAPI{

    public String analisysAPI(String imagePath) {
        String result = "";
        try {
            HttpClient client = HttpClient.newHttpClient();
            String json = "{\"image_path\": \"" + imagePath.replace("\\", "/") + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:5000/api/analisys"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            result = response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
