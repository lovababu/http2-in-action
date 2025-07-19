package org.avol.springboot_http2.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class FileController {

    @GetMapping(value = "/index")
    public String index(){
        return "index";
    }

    @GetMapping(value = "/headerCheck")
    public ResponseEntity<String> hPackCheck() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("CustomerHeader1", generateLargeHeader());
        headers.add("CustomerHeader2", generateLargeHeader());
        headers.add("CustomerHeader3", generateLargeHeader());
        headers.add("CustomerHeader4", generateLargeHeader());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        return ResponseEntity.ok()
                .headers(headers)
                .body("Fake Body.");
    }

    private String generateLargeHeader() {
        StringBuffer str = new StringBuffer();
        IntStream.range(0, 500).forEach(value -> str.append("Header").append(value));
        return str.toString();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable(value = "fileName") String fileName,
                                                 @RequestHeader(value = "X-Priority", defaultValue = "medium") String priority) {
        try {
            Resource resource = new ClassPathResource("static/pdfs/" + fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            switch (priority) {
                case "high":
                    headers.add("X-Stream-Priority", "1");
                    break;
                case "medium":
                    headers.add("X-Stream-Priority", "2");
                    break;
                case "low":
                    headers.add("X-Stream-Priority", "3");
                    break;
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
