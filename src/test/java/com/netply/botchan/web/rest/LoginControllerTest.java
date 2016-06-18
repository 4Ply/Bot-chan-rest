package com.netply.botchan.web.rest;

import com.netply.botchan.web.model.BasicResultResponse;
import com.netply.botchan.web.rest.error.InvalidCredentialsException;
import com.netply.web.security.login.LoginDatabase;
import com.netply.web.security.login.LoginHandler;
import com.netply.web.security.login.controller.LoginController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginControllerTest extends BaseControllerTest {
    private MockMvc mvc;
    private LoginDatabase loginDatabase;


    @Before
    public void setUp() throws Exception {
        loginDatabase = mock(LoginDatabase.class);
        mvc = MockMvcBuilders.standaloneSetup(new LoginController(new LoginHandler(loginDatabase), sessionHandler)).build();
    }

    @Test
    public void test_Login_Call_Returns_Valid_Response_But_Error_Type_When_Login_Fails() throws Exception {
        doThrow(new InvalidCredentialsException()).when(loginDatabase).login(anyString(), anyString());

        mvc.perform(MockMvcRequestBuilders.get("/login").param("username", "username").param("password", "HASH1123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(new BasicResultResponse("Invalid credentials"))));
    }

    @Test
    public void test_Login_Call_Returns_Valid_Response_With_Success_Result_When_Login_Succeeds() throws Exception {
        doThrow(new InvalidCredentialsException()).when(loginDatabase).login(anyString(), anyString());
        doReturn(new BasicResultResponse(true, "sessionKey")).when(loginDatabase).login(eq("username"), eq("HASH1123"));

        mvc.perform(MockMvcRequestBuilders.get("/login").param("username", "username").param("password", "HASH1123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(new BasicResultResponse(true, "sessionKey"))));
    }

    @Test
    public void testLoginCheck() throws Exception {

    }
}
