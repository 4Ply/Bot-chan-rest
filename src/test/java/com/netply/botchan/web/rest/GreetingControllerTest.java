package com.netply.botchan.web.rest;

import com.netply.botchan.web.model.Greeting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class GreetingControllerTest extends BaseControllerTest {
    private MockMvc mvc;


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new GreetingController(sessionHandler)).build();
    }

    @Test
    public void test_Greeting_Call_Returns_Non_Empty_Name_In_Hello_Message_Response() throws Exception {
        super.testInvalidSession(mvc, "/greeting");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/greeting")
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(withValidSessionKey(requestBuilder))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(new Greeting(1, "Hello, World!"))));
    }

    @Test
    public void test_Greeting_Call_Returns_Name_In_Hello_Message_Response() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/greeting").param("name", "Pawel")
                .accept(MediaType.APPLICATION_JSON);

        super.testInvalidSession(mvc, "/greeting");

        mvc.perform(withValidSessionKey(requestBuilder))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(new Greeting(1, "Hello, Pawel!"))));
    }
}
