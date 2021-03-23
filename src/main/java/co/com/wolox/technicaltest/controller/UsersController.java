package co.com.wolox.technicaltest.controller;

import co.com.wolox.technicaltest.model.user.User;
import co.com.wolox.technicaltest.service.InterfaceUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    final
    InterfaceUserService userService;

    public UsersController(InterfaceUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Operation to search all users")
    @GetMapping("/")
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService
                        .findAll());
    }

    @Operation(summary = "Operation to search by id a user")
    @GetMapping("/{id}")
    public ResponseEntity<User> user(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findById(id));
    }


}
