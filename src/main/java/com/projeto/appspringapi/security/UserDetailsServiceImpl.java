package com.projeto.appspringapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.appspringapi.model.UsuarioModel;
import com.projeto.appspringapi.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioModel usuarioModel = this.usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + username));
        UserDetails userDetails = User
                .withUsername(usuarioModel.getEmail())
                .password(usuarioModel.getSenha())
                .authorities(usuarioModel.getPerfis().stream().map(role -> role.getNome()).toArray(String[]::new))
                .build();
        return userDetails;
    }
}
