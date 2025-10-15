package com.yamferreira.screemmatch.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class ConsultaGemini {
    private static final String CHAVE = "AIzaSyAH9v_q1M1uSMzZxDmiiqCJDcg8Y-2LUhU";
    public static String obterTraducao(String texto) {
        Client client = Client.builder().apiKey(CHAVE).build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.0-flash",
                        "Apenas traduza, sem me dizer nada alem disso, o seguinte trecho: " + texto,
                        null);

        return response.text();
    }
}
