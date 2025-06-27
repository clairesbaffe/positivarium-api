package com.positivarium.api.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class LogoutController {

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Suppression du cookie JWT
        Cookie cookie = new Cookie("access_token", null);  // Remplace "access_token" par le nom de ton cookie
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // Assure-toi que ce flag est bien mis en fonction de ton environnement (production vs dev)
        cookie.setPath("/");
        cookie.setMaxAge(0);  // Expire immédiatement
        response.addCookie(cookie);  // Ajoute le cookie expiré à la réponse

        return ResponseEntity.ok("Déconnexion réussie");
    }

}
