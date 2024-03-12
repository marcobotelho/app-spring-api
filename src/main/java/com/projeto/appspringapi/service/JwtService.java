package com.projeto.appspringapi.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String chaveString;

    @Value("${jwt.expiration.time}")
    private Long tempoExpiracao;

    private SecretKey chaveSecreta;

    @PostConstruct
    private void setSecretKey() {
        byte[] chaveBytes = chaveString.getBytes(StandardCharsets.UTF_8);
        chaveSecreta = new SecretKeySpec(chaveBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String gerarToken(String usuarioEmail) {
        Date agora = new Date();
        Date expira = new Date(agora.getTime() + tempoExpiracao);

        return Jwts.builder()
                .setSubject(usuarioEmail)
                .setIssuedAt(agora)
                .setExpiration(expira)
                .signWith(chaveSecreta, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairUsuarioEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(chaveSecreta)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(chaveSecreta).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public SecretKey getChaveSecreta() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String secretKeyToString(SecretKey secretKey) {
        byte[] encodedKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    public SecretKey stringToSecretKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HS256");
    }
}
