package p.lodz.pl.pas2.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.AdministratorController;
import p.lodz.pl.pas2.exceptions.userExceptions.ThereIsNoUserToUpdateException;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.request.AdministratorRequest;
import p.lodz.pl.pas2.services.UserService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdministratorController.class)
public class AdministratorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Test
    @DirtiesContext
    public void testAddUser() throws Exception {
        AdministratorRequest user = new AdministratorRequest("maciek", true, "password");
        User user1 = new Administrator("maciek", true, "password");
        Mockito.when(userService.addUser(Mockito.any(User.class))).thenReturn(user1)
                .thenThrow(new UsernameInUseException(UserMsg.USERNAME_IN_USE));
        ObjectMapper objectMapper= new ObjectMapper();
        mockMvc.perform(post("/api/v1/administrators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()));

        mockMvc.perform(post("/api/v1/administrators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());

    }

    @Test
    @DirtiesContext
    public void testUpdateUser() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        AdministratorRequest user = new AdministratorRequest("maciek", true, "password");
        UUID id = UUID.randomUUID();
        User user2 = new Administrator(id,"maciek", true, "password");

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any(User.class))).thenReturn(user2)
                .thenThrow(ThereIsNoUserToUpdateException.class);
        mockMvc.perform(put("/api/v1/administrators/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.id").isNotEmpty());
        mockMvc.perform(put("/api/v1/administrators/{id}", user2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

    }

}
