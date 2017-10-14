package photato.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotatoController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}