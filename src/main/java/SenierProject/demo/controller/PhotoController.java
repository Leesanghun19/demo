package SenierProject.demo.controller;

import SenierProject.demo.Service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;
    @GetMapping("/circles/view/photo/{id}")
    public ResponseEntity<Resource> showImage(@PathVariable("id") Long id){
        String imageRoot = photoService.findPhoto(id);
        Resource resource = new FileSystemResource(imageRoot);
        HttpHeaders headers = new HttpHeaders();
        Path filePath = null;
        try{
            filePath = Paths.get(imageRoot);
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(resource,headers, HttpStatus.OK);
    }
}
