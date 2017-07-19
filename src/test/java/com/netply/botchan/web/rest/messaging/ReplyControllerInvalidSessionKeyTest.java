package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.MatcherList;
import com.netply.botchan.web.model.Message;
import com.netply.botchan.web.model.Reply;
import com.netply.botchan.web.rest.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReplyControllerInvalidSessionKeyTest extends BaseControllerTest {
    private MockMvc mvc;
    private MessageManager messageManager;


    @Before
    public void setUp() throws Exception {
        messageManager = mock(MessageManager.class);
        mvc = MockMvcBuilders.standaloneSetup(new MessageController(sessionHandler, messageManager)).build();
    }

    @Test
    public void test_Put_Reply_With_Valid_Invalid_Session_Key_Does_Not_Add_Reply_To_MessageManager_And_Returns_Error() throws Exception {
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
    public void test_Put_Reply_With_Valid_Invalid_ClientID_Does_Not_Add_Reply_To_MessageManager_And_Returns_Error() throws Exception {
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
    public void test_Post_RepliesWith_Invalid_Session_Key_Returns_Error() throws Exception {
        testUnauthorisedPostRepliesRequest(INVALID_SESSION_KEY, VALID_CLIENT_ID);
    }

    @Test
    public void test_Post_RepliesWith_Valid_Session_Key_But_Invalid_Client_ID_Returns_Error() throws Exception {
        testUnauthorisedPostRepliesRequest(VALID_SESSION_KEY, INVALID_CLIENT_ID);
    }

    private void testUnauthorisedPostRepliesRequest(String sessionKey, int clientId) throws Exception {
        ArrayList<String> matchers = new ArrayList<>();
        matchers.add("12312");
        matchers.add("54345");
        MatcherList matcherList = new MatcherList("", matchers);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/replies")
                .content(gson.toJson(matcherList))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder.param("sessionKey", sessionKey)).andExpect(status().isUnauthorized());
        verify(messageManager, never()).getUnProcessedReplies(any(matchers.getClass()), anyString());
        verifyNoMoreInteractions(messageManager);
    }

    @Test
    public void test_Delete_Reply_With_Invalid_Session_Key_Returns_Error() throws Exception {
        testDeleteMessageUnauthorised(INVALID_SESSION_KEY, VALID_CLIENT_ID);
    }

    @Test
    public void test_Delete_Reply_With_Valid_Session_Key_But_Invalid_Client_ID_Returns_Error() throws Exception {
        testDeleteMessageUnauthorised(VALID_SESSION_KEY, INVALID_CLIENT_ID);
    }

    private void testDeleteMessageUnauthorised(String sessionKey, int clientId) throws Exception {
        Reply reply = new Reply(0, "message");
        String messageID = "message-id";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/reply")
                .param("clientID", String.valueOf(clientId))
                .param("id", messageID)
                .content(gson.toJson(reply))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder.param("sessionKey", sessionKey)).andExpect(status().isUnauthorized());
        verify(messageManager, never()).markReplyAsProcessed(anyInt(), anyString());
        verifyNoMoreInteractions(messageManager);
    }
}
