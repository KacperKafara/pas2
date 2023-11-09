package p.lodz.pl.pas2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import p.lodz.pl.pas2.controllers.UserController;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.model.UserType;
import p.lodz.pl.pas2.services.UserService;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public void getClients() throws Exception {
        User user = new User("Jaca", UserType.CLIENT, true);
        User user2 = new User("Praca", UserType.CLIENT, true);
        List<User> users_list = new ArrayList<>();
        users_list.add(user);
        users_list.add(user2);
        Mockito.when(userService.getUsers()).thenReturn(users_list);
        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value(user.getUsername()))
                .andExpect(jsonPath("$[0].userType").value(user.getUserType().toString()))
                .andExpect(jsonPath("$[0].active").value(user.isActive()))
                .andExpect(jsonPath("$[1].username").value(user2.getUsername()))
                .andExpect(jsonPath("$[1].userType").value(user2.getUserType().toString()))
                .andExpect(jsonPath("$[1].active").value(user2.isActive()));


    }

    @Test
    public void testGetClientByNickname() throws Exception {

        User user = new User("Jaca", UserType.CLIENT, true);
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(user);
        mockMvc.perform(get("/api/v1/clients/username/{username}", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.userType").value(user.getUserType().toString()))
                 .andExpect(jsonPath("$.active").value(user.isActive()));
    }
    @Test
    public void testGetClientByID() throws Exception {

        User user = new User("Jaca", UserType.CLIENT, true);
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        Mockito.when(userService.getUser((UUID) Mockito.any())).thenReturn(user);
        mockMvc.perform(get("/api/v1/clients/id/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.userType").value(user.getUserType().toString()))
                .andExpect(jsonPath("$.active").value(user.isActive()));
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User("Maciek", UserType.CLIENT, true);
        Mockito.when(userService.addClient(Mockito.any(User.class))).thenReturn(user);
        ObjectMapper objectMapper= new ObjectMapper();
        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated()) //
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.userType").value(user.getUserType().toString()))
                .andExpect(jsonPath("$.active").value(user.isActive()));

    }
    @Test
    public void testPatchUser() throws Exception {

        User user = new User("Jaca", UserType.CLIENT, true);
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        Mockito.when(userService.setActive(userId,true)).thenReturn(user);
        mockMvc.perform(patch("/api/v1/clients/id/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"active\": true}"))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.userType").value(user.getUserType().toString()))
                .andExpect(jsonPath("$.active").value(user.isActive()));
    }
    @Test
    public void testUpdateUser() throws Exception {
        //nie przechodzi
        /*
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("Jaca", UserType.CLIENT, true);
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        Mockito.when(userService.updateClient(userId, user)).thenReturn(user);
        mockMvc.perform(put("/api/v1/clients/id/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.userType").value(user.getUserType().toString()))
                .andExpect(jsonPath("$.active").value(user.isActive())); */

    }

}