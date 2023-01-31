package com.example.garage.Services;

import com.example.garage.Dtos.Security.UserDto;
import com.example.garage.Models.User;
import com.example.garage.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    User user1;
    User user2;
    User user3;
    UserDto userDto1;
    UserDto userDto2;
    UserDto userDto3;


    @BeforeEach
    void setUp(){
        user1 = new User("user1","user","testapikey","testuser@email.com", "bart","simpson",true,null,null,null,null );
        user2 = new User("user2","user","testapikey","testuser@email.com", "lisa","simpson",true,null,null,null,null );



        userDto1 = new UserDto("user1","user",true,"testapikey","testuser@email.com", "bart","simpson",null,null,null);
        userDto2 = new UserDto("user2","user",true,"testapikey","testuser@email.com", "lisa","simpson",null,null,null);
        userDto3 = new UserDto("user","paswoord",true,"apikey","email@email.com","homer","simpson",null,null, null);

    }


    @Test
    void getUsersTest() {
        List<User> actualList = new ArrayList<>();
        actualList.add(user1);
        actualList.add(user2);

        List<UserDto> expectedList = new ArrayList<>();
        expectedList.add(userDto1);
        expectedList.add(userDto2);

        when(userRepository.findAll()).thenReturn(actualList);

        List<UserDto> findUsers = userService.getUsers();

        assertEquals( expectedList.get(0).getFirstname(), findUsers.get(0).getFirstname());
        assertEquals( expectedList.get(1).getFirstname(), findUsers.get(1).getFirstname());
        assertEquals( expectedList.get(0).getLastname(), findUsers.get(0).getLastname());
        assertEquals( expectedList.get(0).getPassword(), findUsers.get(0).getPassword());
    }
    @Test
    void getUser() {
        when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        UserDto userDto = userService.getUser("user1");

        assertEquals("user1", userDto.getUsername());
        assertEquals("user", userDto.getPassword());
        assertEquals("testuser@email.com", userDto.getEmail());

    }


    @Test
    void deleteUserTest() {
        // Arrange
        String username = "user1";
        User expectedUser = user1;
        when(userRepository.findById(username)).thenReturn(Optional.of(expectedUser));

        // Act
        userService.deleteUser(username);

        // Assert
        verify(userRepository).deleteById(username);
    }
    @Test
    void validatePasswordTest(){
        // Arrange
        String password = "User1!";
        String invalidpassword = "User";

        // Act
        boolean passwordcheck = userService.validatePassword(password);
        boolean invalidpasswordcheck = userService.validatePassword(invalidpassword);

        // Assert
        assertTrue(passwordcheck);
        assertFalse(invalidpasswordcheck);
    }


}