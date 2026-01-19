package org.greatsokol.springsecurityjpa.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;

@RestController
@RequestMapping("api/users")
public class UsersController {

    @GetMapping("user")
    public ResponseEntity<String> getUser(Principal principal) throws UnknownHostException {
        if (principal == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(principal.getName() + " " + InetAddress.getLocalHost().getHostAddress());
    }
}
