package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.rest.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageControllerTest extends BaseControllerTest {
    private MockMvc mvc;
    private MessageManager messageManager;


    @Before
    public void setUp() throws Exception {
        messageManager = mock(MessageManager.class);
        mvc = MockMvcBuilders.standaloneSetup(new MessageController(sessionHandler, messageManager)).build();
    }

    @Test
    public void test_Invalid_Session() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("entity", "entity_without_messages");

        super.testInvalidSession(mvc, requestBuilder);
    }

    @Test
    public void test_Get_Messages_For_Entity_With_No_Messages_Returns_Empty_List() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("entity", "entity_without_messages");

        ArrayList<Message> messages = new ArrayList<>();

        mvc.perform(withValidSessionKey(requestBuilder))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(messages)));
    }
}
