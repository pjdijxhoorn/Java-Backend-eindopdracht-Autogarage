
package com.example.garage.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
    //variables.........................................
    @Id
    @Column(nullable = false, unique = true)
    private String username;
    @NotNull(message = "This isnt allowed to be Null " )
    @Column(length = 255)
    private String password;
    @Column
    private String apikey;
    @Column
    private String email;
    @Column(nullable = false)
    private boolean enabled = true;

    //relations.........................................
    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Car> cars;

    @OneToMany(mappedBy = "user")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "user")
    private List<CarPaper> carpapers;


    public Set<Authority> getAuthorities() { return authorities; }
    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }
    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }


}