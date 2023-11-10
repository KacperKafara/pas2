package p.lodz.pl.pas2;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.UserController;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.model.UserType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private p.lodz.pl.pas2.services.UserService userService;

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

}