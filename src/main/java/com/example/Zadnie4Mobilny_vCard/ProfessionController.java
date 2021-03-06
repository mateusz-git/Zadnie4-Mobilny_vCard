package com.example.Zadnie4Mobilny_vCard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfessionController {
    @GetMapping("/searchProfession")
    public String searchProfession() {
        return "search";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "k") String k) throws IOException {
        String url1 = "https://panoramafirm.pl/szukaj?k=+" + k + "&l=";
        List<ProfessionDetails> professionList = getProfessionList(url1);
        model.addAttribute("profession", professionList);
        model.addAttribute("k", k);

        return "result";
    }

    @GetMapping("/getvCard/{k}/{email}")
    public ResponseEntity getvCard(@PathVariable String k, @PathVariable String email) throws IOException {
        String url = "https://panoramafirm.pl/szukaj?k=" + k + "&l=";
        List<ProfessionDetails> professionList = getProfessionList(url);

        ProfessionDetails person = professionList.stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        String vCard = createVCard(person);
        String filename = person.getEmail() + ".vcf";
        File file = new File(filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        if (file.exists()) {
            outputStream.write(vCard.getBytes());
            outputStream.flush();
            outputStream.close();
        }
        Path path = Paths.get(filename);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .body(resource);
    }

    private String createVCard(ProfessionDetails professionDetails) {
        return "BEGIN:VCARD\n" +
                "VERSION:4.0\n" +
                "FN;CHARSET=utf-8:" + professionDetails.getName() + "\n" +
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

        return professionDetailsList;
    }
}
