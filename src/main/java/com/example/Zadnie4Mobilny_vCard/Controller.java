package com.example.Zadnie4Mobilny_vCard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    @GetMapping("/getAllProfession")
    public void getAllProfession() throws IOException {
        String url1 = "https://panoramafirm.pl/szukaj?k=hydraulik&l=";
        List<ProfessionDetails> professionList = getProfessionList(url1);
        String vCard = createVCard(professionList.get(0));
        System.out.println(vCard);
    }

    private String createVCard(ProfessionDetails professionDetails) {
        return "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N;CHARSET=utf-8:" + professionDetails.getName() + "\n" +
                "TEL;WORK;VOICE:" + professionDetails.getPhone() + "\n" +
                "ADR;CHARSET=utf-8;TYPE=WORK,PREF:;;" + professionDetails.getAddress().replace(',', ';') + "\n" +
                "EMAIL:" + professionDetails.getEmail() + "\n" +
                "END:VCARD";
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

        List<ProfessionDetails> professionDetailsList = new ArrayList<>();
        for (int i = 0; i < name.size(); i++) {
            professionDetailsList.add(new ProfessionDetails(name.get(i).text(),
                    address.get(i).text(),
                    phone.get(i).attr("title"),
                    email.get(i).attr("data-company-email")));
        }

        for (ProfessionDetails professionDetails : professionDetailsList) {
            System.out.println(professionDetails);
        }
        return professionDetailsList;
    }
}
