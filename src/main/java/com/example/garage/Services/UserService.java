package com.example.garage.Services;

import com.example.garage.Dtos.Security.UserDto;
import com.example.garage.Exceptions.InvalidPasswordException;
import com.example.garage.Exceptions.RecordNotFoundException;
import com.example.garage.Models.Authority;
import com.example.garage.Models.User;
import com.example.garage.Repositories.UserRepository;
import com.example.garage.Utilities.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    // deze is apart en lazy Autowired omdat ik anders een circular error krijg
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<UserDto> getUsers() {
        List<UserDto> collection = new ArrayList<>();
        List<User> list = (List<User>) userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserDto getUser(String username) {
        UserDto dto = new UserDto();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            dto = fromUser(user.get());
        } else {
            throw new UsernameNotFoundException(username);
        }
        return dto;
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUser(UserDto userDto) {
        String password = userDto.getPassword();
        if (validatePassword(password)) {
            String randomString = RandomStringGenerator.generateAlphaNumeric(20);
            userDto.setApikey(randomString);
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User newUser = userRepository.save(toUser(userDto));
            return newUser.getUsername();
        } else {
            throw new InvalidPasswordException("Your password must contain:\n At least 6 characters, 1 uppercase letter, 1 lowercase letter, 1 special character and may not contain any whitespaces");
        }
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserDto userDto) {
        String password = userDto.getPassword();
        if (!userRepository.existsById(username)) throw new RecordNotFoundException();
        if (validatePassword(password)) {
            User user = userRepository.findById(username).get();
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.setFirstname(userDto.getFirstname());
            user.setLastname(userDto.getLastname());
            userRepository.save(user);
        } else {
            throw new InvalidPasswordException("Your password must contain:\n At least 6 characters, 1 uppercase letter, 1 lowercase letter, 1 special character and may not contain any whitespaces");
        }
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserDto userDto = fromUser(user);
        return userDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) {

        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if(!Objects.equals(authority, "ROLE_USER")&!Objects.equals(authority, "ROLE_DESK")&!Objects.equals(authority, "ROLE_ADMIN")&!Objects.equals(authority, "ROLE_MECHANIC")){
            throw new RecordNotFoundException(" please fill in one of these roles: ROLE_USER, ROLE_DESK, ROLE_ADMIN, ROLE_MECHANIC");}
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        try{
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();System.out.println(authorityToRemove);
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
        }catch (Exception e){
            throw new RecordNotFoundException("that role is already removed, or was never there!");
        }

    }

    public static UserDto fromUser(User user) {

        var dto = new UserDto();

        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEnabled(user.isEnabled());
        dto.setApikey(user.getApikey());
        dto.setEmail(user.getEmail());
        dto.setAuthorities(user.getAuthorities());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setCars(user.getCars());
        dto.setInvoices(user.getInvoices());
        return dto;
    }

    public User toUser(UserDto userDto) {

        var user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEnabled(userDto.getEnabled());
        user.setApikey(userDto.getApikey());
        user.setEmail(userDto.getEmail());
        user.setInvoices(userDto.getInvoices());
        user.setCars(userDto.getCars());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());


        return user;
    }

    public Boolean validatePassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*?=])(?=\\S+$).{6,}$");
    }
/*  ^                 - start-of-string
    (?=.*[0-9])       - a digit must occur at least once
    (?=.*[a-z])       - a lower case letter must occur at least once
    (?=.*[A-Z])       - an upper case letter must occur at least once
    (?=.*[@#$%^&+=])  - a special character must occur at least once
    (?=\S+$)          - no whitespace allowed in the entire string
    .{6,}             - anything, at least six places though
    $                 # end-of-string*/


}
