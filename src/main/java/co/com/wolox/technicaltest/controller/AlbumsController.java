package co.com.wolox.technicaltest.controller;


import co.com.wolox.technicaltest.model.album.Album;
import co.com.wolox.technicaltest.repository.AlbumActionsRepository;
import co.com.wolox.technicaltest.service.InterfaceAlbumService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumsController {

    final
    InterfaceAlbumService albumService;

    final
    AlbumActionsRepository albumActionsRepository;

    public AlbumsController(InterfaceAlbumService albumService, AlbumActionsRepository albumActionsRepository) {
        this.albumService = albumService;
        this.albumActionsRepository = albumActionsRepository;
    }

    @Operation(summary = "Operation to search all albums")
    @GetMapping("/")
    public ResponseEntity<List<Album>> albums() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(albumService.findAll());
    }

    @Operation(summary = "Operation to find by id an album")
    @GetMapping("/{id}")
    public ResponseEntity<Album> album(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(albumService.findById(id));
    }

    @Operation(summary = "Operation to find all albums of a user by userId")
    @GetMapping("/albumsbyuser/{id}")
    public ResponseEntity<List<Album>> albumsByUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(albumService.findByUserId(id));
    }

}
