package com.av.sharebook.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    // @JsonIgnore // Pour masquer le mot de passe lors de la sérialisation JSON
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    @Builder.Default
    // En Java (à partir de la version 9),
    // la méthode statique List.of(...)
    // permet de créer très rapidement une liste immuable (non modifiable)
    // contenant les éléments indiqués.
    private final List<String> userRoles = new ArrayList<>(List.of("USER"));

    // l'utilisation de stream() permet de manipuler de manière flexible
    // et expressive les éléments de la liste userRoles, en appliquant
    // des opérations de transformation sur chaque élément de cette liste
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
