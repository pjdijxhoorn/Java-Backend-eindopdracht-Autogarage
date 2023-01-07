package com.example.garage.Dtos.Security;

import com.example.garage.Models.Authority;
import com.example.garage.Models.Car;
import com.example.garage.Models.Invoice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    public String username;
    public String password;
    public Boolean enabled;
    public String apikey;
    public String email;
    private String firstname;
    private String lastname;
    public Set<Authority> authorities;

    public List<Car> cars;
    public List<Invoice> invoices;



    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}