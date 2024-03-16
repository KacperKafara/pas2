package p.lodz.pl.pas2.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.ModeratorController;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.request.ModeratorRequest;
import p.lodz.pl.pas2.services.UserService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ComponentScan(basePackages = "p.lodz.pl.pas2")
public class ModeratorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Test
    @DirtiesContext
    public void testAddUser() throws Exception {
        ModeratorRequest user = new ModeratorRequest("maciek", true, "1234");
        User user1 = new Moderator("maciek", true, "1234");
        Mockito.when(userService.addUser(Mockito.any(User.class))).thenReturn(user1)
                .thenThrow(new UsernameInUseException(UserMsg.USERNAME_IN_USE));
        ObjectMapper objectMapper= new ObjectMapper();
        mockMvc.perform(post("/api/v1/moderators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()));

        mockMvc.perform(post("/api/v1/moderators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());

    }

    @Test
    @DirtiesContext
    public void testUpdateUser() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ModeratorRequest user = new ModeratorRequest("maciek", true, "1234");
        UUID id = UUID.randomUUID();
        User user2 = new Moderator(id,"maciek", true, "1234");

        Mockito.when(userService.getUser(id))
                .thenReturn(user2);
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any(User.class), Mockito.any(String.class)))
                .thenReturn(user2);

        mockMvc.perform(put("/api/v1/moderators/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .header("If-Match", "abd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.active").value(user.isActive()))
                .andExpect(jsonPath("$.id").isNotEmpty());

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any(User.class), Mockito.any(String.class)))
                .thenThrow(new UserNotFoundException(UserMsg.USER_NOT_FOUND));

        mockMvc.perform(put("/api/v1/moderators/{id}", user2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .header("If-Match", "abd"))
                .andExpect(status().isNotFound());

    }

}
