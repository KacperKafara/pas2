package p.lodz.pl.pas2.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.ClientController;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.services.UserService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Test

    public void testAddUser() throws Exception {
        User user = new Client("maciek", true,"Maciek","Smolinski");
        Mockito.when(userService.addUser(Mockito.any(User.class))).thenReturn(user);
        ObjectMapper objectMapper= new ObjectMapper();
        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()));
        Mockito.when(userService.addUser(Mockito.any(User.class))).thenThrow(new UsernameInUseException(UserMsg.USERNAME_IN_USE));
        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void testSetActiveUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new Client("maciek", true,"Maciek","Smolinski");
        user.setId(userId);
        Mockito.when(userService.setActive(userId,true)).thenReturn(user);
        mockMvc.perform(patch("/api/v1/clients/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\": true}"))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.id").value(user.getId().toString()));
        Mockito.when(userService.setActive(userId,true)).thenThrow(new UserNotFoundException(UserMsg.USER_NOT_FOUND));
        mockMvc.perform(patch("/api/v1/clients/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"active\": true}"))
                .andExpect(status().isNotFound());

    }
    @Test
    public void testUpdateUser() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        User user = new Client(UUID.randomUUID(),"maciek", true,"Maciek","Smolinski");
        user.setUsername("Nowe");
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any(User.class))).thenReturn(user);
        mockMvc.perform(put("/api/v1/clients/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.id").isNotEmpty());
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any(User.class))).thenThrow(new UserNotFoundException(UserMsg.USER_NOT_FOUND));
        mockMvc.perform(put("/api/v1/clients/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

    }

}
