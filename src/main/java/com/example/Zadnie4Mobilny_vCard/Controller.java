package com.example.Zadnie4Mobilny_vCard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.jsoup.nodes.Document;
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
        String url1 = "https://panoramafirm.pl/szukaj?k=hydraulik&l=";
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
        Document document = Jsoup.parse(htmlContent);
        Elements element = document.select("li");
        Elements name = element.select("h2");
        Elements address = element.select("div.address");
        Elements emailAndPhone = element.select("div.item");
        Elements phone = emailAndPhone.select("a.icon-telephone");
        Elements email = emailAndPhone.select("a.ajax-modal-link");
        for (Element element2 : phone) {

           System.out.println(element2.attr("title"));
        }
        for (Element element2 : email) {

           System.out.println(element2.attr("data-company-email"));
        }

        System.out.println("END");
        return null;
    }
}
