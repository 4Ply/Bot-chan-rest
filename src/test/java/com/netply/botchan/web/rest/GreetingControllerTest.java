package com.netply.botchan.web.rest;

import com.netply.botchan.web.model.BasicMessageObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GreetingControllerTest extends BaseControllerTest {
    private MockMvc mvc;


    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new GreetingController()).build();
    }

    @Test
    public void test_Greeting_Call_Returns_Non_Empty_Name_In_Hello_Message_Response() throws Exception {
        super.testInvalidSession(mvc, "/greeting");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/greeting")
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(withValidSessionKey(requestBuilder))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(new BasicMessageObject(1, "Hello, World!"))));
    }

    @Test
    public void test_Greeting_Call_Returns_Name_In_Hello_Message_Response() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/greeting").param("name", "Pawel")
                .accept(MediaType.APPLICATION_JSON);

        super.testInvalidSession(mvc, "/greeting");

        mvc.perform(withValidSessionKey(requestBuilder))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(new BasicMessageObject(1, "Hello, Pawel!"))));
    }
}
