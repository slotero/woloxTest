package co.com.wolox.technicaltest.controller;

import co.com.wolox.technicaltest.controller.exception.NotFoundException;
import co.com.wolox.technicaltest.model.album.AlbumActions;
import co.com.wolox.technicaltest.model.album.Action;
import co.com.wolox.technicaltest.model.album.AlbumActionsDTO;
import co.com.wolox.technicaltest.service.InterfaceAlbumActionService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/albumActions")
public class AlbumActionController {

    final
    InterfaceAlbumActionService albumRolesService;

    final
    ModelMapper modelMapper;

    public AlbumActionController(InterfaceAlbumActionService albumRolesService, ModelMapper modelMapper) {
        this.albumRolesService = albumRolesService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Operation to save permissions")
    @PostMapping("/")
    public ResponseEntity<AlbumActionsDTO> saveAlbumRoles(@RequestBody AlbumActionsDTO albumActionsDTO) {
        try {
            AlbumActionsDTO resultDTO = convertToDTO(albumRolesService.create(convertToEntity(albumActionsDTO)));
            addSelfLink(resultDTO);
            addLinks(resultDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(resultDTO);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "the combination of user and album already exists", e);
        }
    }

    @Operation(summary = "Operation to find the permissions of a user on albumId")
    @GetMapping( "/album")
    public ResponseEntity<AlbumActionsDTO> findAlbumUser(@RequestParam Long albumId, @RequestParam Long userId) {
        AlbumActionsDTO result = convertToDTO(albumRolesService.findByAlbumIdAndUserId(albumId, userId)
                .orElseThrow(NotFoundException::new));
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @Operation(summary = "Operation to update permissions")
    @PatchMapping("/")
    public ResponseEntity<AlbumActionsDTO> updateAlbumRoles(@RequestBody AlbumActionsDTO albumActionsDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(convertToDTO(albumRolesService.update(convertToEntity(albumActionsDTO))));
    }

    @Operation(summary = "Operation to find the permissions of a user by albumId or action")
    @GetMapping(value = "/albumsUsers", produces = { "application/hal+json" })
    public ResponseEntity<List<AlbumActionsDTO>> findAllUser
            (@RequestParam Long albumId, @RequestParam Action action) {

        Optional<List<AlbumActions>> result = albumRolesService.findByAlbumIdAndActions(albumId, action);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result.orElseThrow(NotFoundException::new).stream()
                        .map(this::convertToDTO)
                        .map(this::addSelfLink)
                        .collect((Collectors.toList())));
    }


    private AlbumActionsDTO convertToDTO(AlbumActions albumActions) {
        return modelMapper.map(albumActions, AlbumActionsDTO.class);
    }

    private AlbumActions convertToEntity(AlbumActionsDTO albumActionsDTO) {
        return modelMapper.map(albumActionsDTO, AlbumActions.class);
    }

    private AlbumActionsDTO addSelfLink(@NotNull final AlbumActionsDTO albumActionsDTO) {
        Link selfLink = linkTo(methodOn(AlbumActionController.class)
                .findAlbumUser(albumActionsDTO.getAlbumId(), albumActionsDTO.getUserId()))
                .withSelfRel().withType("GET");
        albumActionsDTO.add(selfLink);
        return albumActionsDTO;
    }

    private AlbumActionsDTO addLinks(@NotNull final AlbumActionsDTO albumActionsDTO) {

        Link update = linkTo(methodOn(AlbumActionController.class)
                .updateAlbumRoles(albumActionsDTO))
                .withRel("update")
                .withMedia("application/json")
                .withType("PATCH");
        albumActionsDTO.add(update);

        Link userRead = linkTo(methodOn(AlbumActionController.class)
                .findAllUser(albumActionsDTO.getAlbumId(), Action.READ))
                .withRel("users-read")
                .withType("GET");
        albumActionsDTO.add(userRead);

        Link userWrite = linkTo(methodOn(AlbumActionController.class)
                .findAllUser(albumActionsDTO.getAlbumId(), Action.WRITE))
                .withRel("users-write")
                .withType("GET");
        albumActionsDTO.add(userWrite);
        return albumActionsDTO;
    }
}
