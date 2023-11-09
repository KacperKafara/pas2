package p.lodz.pl.pas2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import p.lodz.pl.pas2.controllers.UserController;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.model.UserType;
import p.lodz.pl.pas2.services.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetClientByNickname() throws Exception {
        //uj wie nie przechodzi
        User user = new User("Jaca", UserType.CLIENT, true);
        String uri = "/username/Jaca";
        Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.username").value(user.getUsername()))
                .andExpect((ResultMatcher) jsonPath("$.userType").value(user.getUserType().toString()))
                .andExpect((ResultMatcher) jsonPath("$.active").value(user.isActive()));
    }

}