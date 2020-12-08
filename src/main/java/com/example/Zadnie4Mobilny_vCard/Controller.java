package com.example.Zadnie4Mobilny_vCard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@RestController
public class Controller {
    @GetMapping("/getAllProfession")
    public void getAllProfession() throws IOException {
        String url1 = "https://panoramafirm.pl/hydraulik/firmy,1.html";
        getProfessionList(url1);
    }

    private List<ProfessionDetails> getProfessionList(String url1) throws IOException {
        URL url = new URL(url1);
        URLConnection connection = url.openConnection();
        StringBuilder fromWebsite = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            fromWebsite.append(line);
        }
        String htmlContent = fromWebsite.toString();
        System.out.println(htmlContent);
        return null;
    }
}
