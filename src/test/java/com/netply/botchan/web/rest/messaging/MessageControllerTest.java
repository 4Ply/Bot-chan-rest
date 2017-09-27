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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageControllerTest extends BaseControllerTest {
    private MockMvc mvc;
    private MessageManager messageManager;


    @Before
    public void setUp() {
        messageManager = mock(MessageManager.class);
        mvc = MockMvcBuilders.standaloneSetup(new MessageController(messageManager)).build();
    }

    @Test
    public void test_Put_Message_Adds_Message_To_MessageManager() throws Exception {
        Message message = new Message();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/message")
                .param("clientID", String.valueOf(VALID_CLIENT_ID))
                .content(gson.toJson(message))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(withValidSessionKey(requestBuilder)).andExpect(status().isOk());
        message.setSender(null + "___" + VALID_CLIENT_ID);
        verify(messageManager).addMessage(eq(message));
        verifyNoMoreInteractions(messageManager);
    }

    @Test
    public void test_Post_Messages_Returns_List_Of_Messages_For_A_Client_ID() {
//        ArrayList<String> matchers = new ArrayList<>();
//        matchers.add("111");
//        MatcherList matcherList = new MatcherList("platform", matchers);
//
//        ArrayList<Message> expected = new ArrayList<>();
//        expected.add(new Message(32487, "Platform1", "Message"));
//        expected.add(new Message(9548, "Platform1", "Message2"));
//        expected.add(new Message(44129, "Platform2", "Message3"));
//        doReturn(expected).when(messageManager).getUnProcessedMessagesForPlatform(eq(matchers), eq(VALID_CLIENT_ID));
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/messages")
//                .content(gson.toJson(matcherList))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//
//        mvc.perform(withValidSessionKey(requestBuilder))
//                .andExpect(status().isOk())
//                .andExpect(content().json(gson.toJson(expected)));
//        verify(messageManager).getUnProcessedMessagesForPlatform(eq(matchers), eq(VALID_CLIENT_ID));
//        verifyNoMoreInteractions(messageManager);
    }

    @Test
    public void test_Delete_Message_Marks_Message_As_Processed_In_MessageManager() {
//        Message message = new Message();
//        String messageID = "message-id";
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/message")
//                .param("clientID", String.valueOf(VALID_CLIENT_ID))
//                .param("id", messageID)
//                .content(gson.toJson(message))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//
//        mvc.perform(withValidSessionKey(requestBuilder)).andExpect(status().isOk());
//        verify(messageManager).markMessageAsProcessed(eq(messageID), eq(VALID_CLIENT_ID));
//        verifyNoMoreInteractions(messageManager);
    }
}
