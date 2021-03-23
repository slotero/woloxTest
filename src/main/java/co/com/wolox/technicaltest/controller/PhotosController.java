package co.com.wolox.technicaltest.controller;

import co.com.wolox.technicaltest.model.photo.Photo;
import co.com.wolox.technicaltest.service.InterfacePhotoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotosController {

    final
    InterfacePhotoService photoService;

    public PhotosController(InterfacePhotoService photoService) {
        this.photoService = photoService;
    }

    @Operation(summary = "Operation to search all photos")
    @GetMapping("/")
    public ResponseEntity<List<Photo>> photos() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(photoService.findAll());
    }

    @Operation(summary = "Operation to search by id a photo")
    @GetMapping("/{id}")
    public ResponseEntity<Photo> photo(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(photoService.findById(id));
    }


    //Plus
    @Operation(summary = "Operation to search all photos by userId")
    @GetMapping("/photosbyuser/{userId}")
    public ResponseEntity<List<Photo>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(photoService.findByUserId(userId));
    }
}
