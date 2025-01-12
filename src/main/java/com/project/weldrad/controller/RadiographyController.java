package com.project.weldrad.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.weldrad.dto.RadiographyDTO;
import com.project.weldrad.dto.RadiographyListDTO;
import com.project.weldrad.service.RadiographyService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/api/radiography")
public class RadiographyController {
    private final RadiographyService radiographyService;

    @Autowired
    public RadiographyController(RadiographyService radiographyService) {
        this.radiographyService = radiographyService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("description") String description,
        @RequestParam("material") String material,
        @RequestParam("thickness") int thickness,
        @RequestParam("diameter") int diameter
    ) {
        RadiographyDTO radiographyDTO = new RadiographyDTO();
        radiographyDTO.setDescription(description);
        radiographyDTO.setMaterial(material);
        radiographyDTO.setThickness(thickness);
        radiographyDTO.setDiameter(diameter);

        try {
            String fileName = radiographyService.saveUploadFile(file, radiographyDTO);
            return ResponseEntity.ok("Upload completed: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("There was an error: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        try {
            return radiographyService.downloadFile(fileName, request);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<RadiographyListDTO>> listFile() throws IOException{
        try {
            return radiographyService.listFiles();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/analisys")
    public ResponseEntity<String> imageAnalisys(@RequestParam Long id) {
        try {
            return radiographyService.analisys(id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}
