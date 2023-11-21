package p.lodz.pl.pas2.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.UserController;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.services.UserService;


import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    public void getUsers() throws Exception {
        User user = new Moderator("Jaca", true);
        User user2 = new Moderator("Praca", true);
        List<User> users_list = new ArrayList<>();
        users_list.add(user);
        users_list.add(user2);
        Mockito.when(userService.getUsers()).thenReturn(users_list);
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value(user.getUsername()))
                .andExpect(jsonPath("$[0].active").value(user.isActive()))
                .andExpect(jsonPath("$[1].username").value(user2.getUsername()))
                .andExpect(jsonPath("$[1].active").value(user2.isActive()));

        Mockito.when(userService.getUser("Jaca")).thenReturn(user);
        mockMvc.perform(get("/api/v1/users").param("username", "Jaca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));

        List<User> users_list1 = new ArrayList<>();
        users_list1.add(user);

        Mockito.when(userService.getUsersByPattern("ca")).thenReturn(users_list1);
        mockMvc.perform(get("/api/v1/users").param("username", "ca"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        Mockito.when(userService.getUsers()).thenReturn(null);
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetUserByID() throws Exception {

        User user = new Client("Jaca", true, "Jaca", "Jaca");
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        Mockito.when(userService.getUser(user.getId())).thenReturn(user);
        mockMvc.perform(get("/api/v1/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.id").value(user.getId().toString()));;
    }

    @Test
    public void setActiveTest() throws Exception {
        UUID userId = UUID.randomUUID();
        Map<String, Boolean> body = new HashMap<>();
        body.put("active", true);

        User user = new Client("Jaca", false, "Jaca", "Jaca");
        Mockito.when(userService.setActive(userId, false)).thenReturn(user);
        mockMvc.perform(patch("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(true));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}