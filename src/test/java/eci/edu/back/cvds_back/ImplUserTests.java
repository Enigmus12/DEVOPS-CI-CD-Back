package eci.edu.back.cvds_back;


import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.controller.UserController;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.impl.UserRepositoryImpl;
import eci.edu.back.cvds_back.service.impl.UserServiceImpl;
import eci.edu.back.cvds_back.service.interfaces.UserMongoRepository;
import eci.edu.back.cvds_back.service.interfaces.UserRepository;
import eci.edu.back.cvds_back.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ImplUserTests {

    // Mocks y componentes para pruebas de User
    @Mock
    private UserMongoRepository userMongoRepository;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;
    private User user;
    private List<User> userList;

    @BeforeEach
    void setUp() throws UserServiceException{
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);

        // Configuración inicial para pruebas de User
        userDTO = new UserDTO();
        userDTO.setId("user123");
        userDTO.setUsername("testuser");
        userDTO.setPhone(123456789);

        user = new User(userDTO);
        userList = new ArrayList<>();
        userList.add(user);

        // Configuramos los mocks para User
        when(userMongoRepository.findAll()).thenReturn(userList);
        when(userMongoRepository.findById("user123")).thenReturn(Optional.of(user));

        when(mockUserRepository.findAll()).thenReturn(userList);
        when(mockUserRepository.findById("user123")).thenReturn(user);

        when(mockUserService.getAllUsers()).thenReturn(userList);
        when(mockUserService.getUser("user123")).thenReturn(user);
        when(mockUserService.saveUser(any(UserDTO.class))).thenReturn(user);

        // Configure with ReflectionTestUtils
        ReflectionTestUtils.setField(userService, "userRepository", mockUserRepository);
        ReflectionTestUtils.setField(userController, "userService", mockUserService);
    }

    // Tests para UserRepositoryImpl
    @Test
    void testUserRepositoryFindAll() {
        List<User> result = userRepository.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userMongoRepository).findAll();
    }

    @Test
    void testUserRepositoryFindById_Success() throws UserServiceException {
        User result = userRepository.findById("user123");
        assertNotNull(result);
        assertEquals("user123", result.getId());
        verify(userMongoRepository).findById("user123");
    }

    @Test
    void testUserRepositoryFindById_NotFound() {
        when(userMongoRepository.findById("nonExistingId")).thenReturn(Optional.empty());

        UserServiceException exception = assertThrows(UserServiceException.class, () -> {
            userRepository.findById("nonExistingId");
        });

        assertEquals("User Not found", exception.getMessage());
        verify(userMongoRepository).findById("nonExistingId");
    }

    @Test
    void testUserRepositorySave() {
        userRepository.save(user);
        verify(userMongoRepository).save(user);
    }

    @Test
    void testUserRepositoryDeleteById() throws UserServiceException {
        userRepository.deleteById("user123");
        verify(userMongoRepository).deleteById("user123");
    }

    // Tests para UserServiceImpl
    @Test
    void testGetUser_Success() throws UserServiceException {
        when(mockUserRepository.findById("user123")).thenReturn(user);

        User result = userService.getUser("user123");
        assertNotNull(result);
        assertEquals("user123", result.getId());
        verify(mockUserRepository).findById("user123");
    }

    @Test
    void testGetUser_NotFound() throws UserServiceException {
        when(mockUserRepository.findById("nonExistingId")).thenThrow(new UserServiceException("User Not found"));

        UserServiceException exception = assertThrows(UserServiceException.class, () -> {
            userService.getUser("nonExistingId");
        });

        assertEquals("User Not found", exception.getMessage());
    }

    @Test
    void testSaveUser() {
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setId("newUser");
        newUserDTO.setUsername("newUsername");
        newUserDTO.setPhone(987654321);

        User result = userService.saveUser(newUserDTO);
        assertNotNull(result);
        assertEquals("newUser", result.getId());
        assertEquals("newUsername", result.getUsername());
        assertEquals(987654321, result.getPhone());
        verify(mockUserRepository).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        List<User> result = userService.getAllUsers();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mockUserRepository).findAll();
    }

    @Test
    void testDeleteUser() throws UserServiceException {
        doNothing().when(mockUserRepository).deleteById(anyString());

        userService.deleteUser("user123");
        verify(mockUserRepository).deleteById("user123");
    }
}
