package com.project.weldrad.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.weldrad.configuration.RadFileProperties;
import com.project.weldrad.domain.EnumRadiographyStatus;
import com.project.weldrad.domain.Radiography;
import com.project.weldrad.dto.RadiographyDTO;
import com.project.weldrad.repository.RadiographyRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class RadiographyService {
    private final Path fileStorageLocation;
    private final RadiographyRepository radiographyRepository;

    //Contructor injecting file properties
    public RadiographyService(RadFileProperties radFileProperties, RadiographyRepository radiographyRepository) {

        this.radiographyRepository = radiographyRepository;

        //Relative to Absolute path
        this.fileStorageLocation = Paths.get(radFileProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            //Create just if not exist
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ioex) {
            throw new RuntimeException("Failed on creating directory: ", ioex);
        }
    }

    public String saveUploadFile(MultipartFile file, RadiographyDTO radiographyDTO) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); //File name

        if (!(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {
            throw new IllegalArgumentException("Apenas arquivos PNG e JPG são permitidos.");
        }

        Path targetLocation = this.fileStorageLocation.resolve(fileName); //Location to save
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); //Saving

        //Save data
        Radiography radiography = new Radiography();
        radiography.setFileName(fileName);
        radiography.setFilePath(targetLocation.toString());
        radiography.setSubmissionDate(LocalDateTime.now());
        radiography.setSubmissionUser("Joãozinho");
        radiography.setStatus(EnumRadiographyStatus.PENDENTE);
        radiography.setDescription(radiographyDTO.getDescription());
        radiography.setMaterial(radiographyDTO.getMaterial());
        radiography.setThickness(radiographyDTO.getThickness());
        radiography.setDiameter(radiographyDTO.getDiameter());
        radiography.setReport(null);
        radiographyRepository.save(radiography);

        return fileName;
    }

    public ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request) throws IOException {
        Path filePath = fileStorageLocation.resolve(fileName).normalize();

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }

    public ResponseEntity<List<String>> listFiles() throws IOException {
        List<String> fileNames = Files.list(fileStorageLocation)
            .map(Path::getFileName)
            .map(Path::toString)
            .collect(Collectors.toList());

        return ResponseEntity.ok(fileNames);
    }

    public ResponseEntity<String> analisys(Long id) throws IOException {
        if (!radiographyRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID não encontrado");
        }
    
        Radiography radiography = radiographyRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Radiografia não encontrada para o ID: " + id));

        Path relativePath = Paths.get(radiography.getFilePath());
        Path absolutePath = relativePath.toAbsolutePath();
        
        ExternalAPI pythonAPI = new ExternalAPI();
        
        String result = pythonAPI.analisysAPI(absolutePath.toString());

        if (result.equals("Com Defeito")) {
            radiography.setStatus(EnumRadiographyStatus.REPROVADA);
            return ResponseEntity.ok("REPROVADA");
        } else if(result.equals("Sem Defeito")) {
            radiography.setStatus(EnumRadiographyStatus.APROVADA);
            return ResponseEntity.ok("APROVADA");
        } else {
            return ResponseEntity.ok("Houve uma falha na análise.");
        }
    }
}
