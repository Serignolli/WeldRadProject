package com.project.weldrad.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.weldrad.configuration.RadFileProperties;
import com.project.weldrad.dto.RadiographyDTO;

import jakarta.servlet.http.HttpServletRequest;



@Controller
@RequestMapping("/api/radiography")
public class RadiographyController {
    private final Path fileStorageLocation;

    //Constructor injecting file properties
    public RadiographyController(RadFileProperties radFileProperties){

        //Relative path to absolute path
        this.fileStorageLocation = Paths.get(radFileProperties.getUploadDir())
        .toAbsolutePath()
        .normalize();
    }

    //DTO adicionado nos parâmetros, fazer a lógica de passar para a service os valores
    //+
    //Fazer as services
    //+
    //Controle de Transação???
    //Continuar com controller de reports
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam RadiographyDTO radiographyDTO, @RequestParam("file") MultipartFile file) {
        //Get archive name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            //Move to the radUpload folder
            Path targetLocation = fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation);

            return ResponseEntity.ok("Upload completed");

        } catch (IOException e) {
            String eString = e.toString();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("There was an error: " + eString);
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Path filePath = fileStorageLocation.resolve(fileName).normalize();

        try {
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

            if (contentType == null){
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "")
            .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFile() throws IOException{
        List<String> fileNames = Files.list(fileStorageLocation)
        .map(Path::getFileName)
        .map(Path::toString)
        .collect(Collectors.toList());

        return ResponseEntity.ok(fileNames);
    }
}
