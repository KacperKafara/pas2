package p.lodz.pl.pas2.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.ClientController;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.request.ClientRequest;
import p.lodz.pl.pas2.request.UserRequest;
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
    @DirtiesContext
    public void testAddUser() throws Exception {
        UserRequest user = new ClientRequest("maciek", true,"Mateusz","Lewandowski");
        User user1 = new Client("maciek", true,"Mateusz","Lewandowski");
        Mockito.when(userService.addUser(Mockito.any(User.class))).thenReturn(user1)
                .thenThrow(new UsernameInUseException(UserMsg.USERNAME_IN_USE));
        ObjectMapper objectMapper= new ObjectMapper();
        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()));

        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isLocked());

    }
    @Test
    public void addUserButLoginEmpty() throws Exception {
        ClientRequest invalidClient = new ClientRequest("",true,"Mateusz","Lew"); // Blank firstName

        // When
        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidClient)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    public void testUpdateUser() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ClientRequest user = new ClientRequest("maciek", true,"Maciek","Smolinski");
        UUID id = UUID.randomUUID();
        Client user2 = new Client(id,"maciek", true,"Maciek","Smolinski");

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any(User.class))).thenReturn(user2)
                .thenThrow(new UserNotFoundException(UserMsg.USER_NOT_FOUND));
        mockMvc.perform(put("/api/v1/clients/id/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.id").isNotEmpty());
        mockMvc.perform(put("/api/v1/clients/id/{id}", user2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

    }
    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
