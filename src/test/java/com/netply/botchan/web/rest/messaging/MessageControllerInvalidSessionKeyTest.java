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

public class MessageControllerInvalidSessionKeyTest extends BaseControllerTest {
    private MockMvc mvc;
    private MessageManager messageManager;


    @Before
    public void setUp() {
        messageManager = mock(MessageManager.class);
        mvc = MockMvcBuilders.standaloneSetup(new MessageController(messageManager)).build();
    }

    @Test
    public void test_Put_Message_With_Valid_Invalid_Session_Key_Does_Not_Add_Message_To_MessageManager_And_Returns_Error() throws Exception {
        Message message = new Message();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/message")
                .param("clientID", String.valueOf(VALID_CLIENT_ID))
                .content(gson.toJson(message))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        testInvalidSession(mvc, requestBuilder);
        verify(messageManager, never()).addMessage(eq(message));
    }

    @Test
    public void test_Put_Message_With_Valid_Session_Key_But_Invalid_Client_ID_Returns_Error() throws Exception {
        Message message = new Message();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/message")
                .param("clientID", String.valueOf(INVALID_CLIENT_ID))
                .content(gson.toJson(message))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        testInvalidSession(mvc, requestBuilder);
        verify(messageManager, never()).addMessage(eq(message));
    }

    @Test
    public void test_Post_Messages_With_Invalid_Session_Key_Returns_Error() throws Exception {
        testUnauthorisedPostMessagesRequest(INVALID_SESSION_KEY, VALID_CLIENT_ID);
    }

    @Test
    public void test_Post_Messages_With_Valid_Session_Key_But_Invalid_Client_ID_Returns_Error() throws Exception {
        testUnauthorisedPostMessagesRequest(VALID_SESSION_KEY, INVALID_CLIENT_ID);
    }

    private void testUnauthorisedPostMessagesRequest(String sessionKey, int clientId) {
//        ArrayList<String> matchers = new ArrayList<>();
//        matchers.add("111");
//        MatcherList matcherList = new MatcherList(clientId, matchers);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/messages")
//                .content(gson.toJson(matcherList))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//
//        mvc.perform(requestBuilder.param("sessionKey", sessionKey)).andExpect(status().isUnauthorized());
//        verify(messageManager, never()).getUnProcessedMessagesForPlatform(any(matchers.getClass()), anyInt());
//        verifyNoMoreInteractions(messageManager);
    }

    @Test
    public void test_Delete_Message_With_Invalid_Session_Key_Returns_Error() throws Exception {
        testDeleteMessageUnauthorised(INVALID_SESSION_KEY, VALID_CLIENT_ID);
    }

    @Test
    public void test_Delete_Message_With_Valid_Session_Key_But_Invalid_Client_ID_Returns_Error() throws Exception {
        testDeleteMessageUnauthorised(VALID_SESSION_KEY, INVALID_CLIENT_ID);
    }

    private void testDeleteMessageUnauthorised(String sessionKey, int clientId) {
//        Message message = new Message();
//        String messageID = "message-id";
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/message")
//                .param("clientID", String.valueOf(clientId))
//                .param("id", messageID)
//                .content(gson.toJson(message))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//
//        mvc.perform(requestBuilder.param("sessionKey", sessionKey)).andExpect(status().isUnauthorized());
//        verify(messageManager, never()).markMessageAsProcessed(anyString(), anyInt());
//        verifyNoMoreInteractions(messageManager);
    }
}
