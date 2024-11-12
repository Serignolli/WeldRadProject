package com.project.weldrad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/report")
public class ReportController {

    /*@PostMapping("/create")
    public ResponseEntity<String> createPDF(Long id) {

        
        //Criar lógica para chamar a service para gerar o PDF
        //Criar service intermediar o controller e a geração da service, bem como gravar os dados
        //Criar mais uma service que gera o PDF
        try {
            return ResponseEntity.ok("Report created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("There was an error: " + e.getMessage());
        }
        
    }*/
}
