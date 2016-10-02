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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReplyControllerTest extends BaseControllerTest {
    private MockMvc mvc;
    private MessageManager messageManager;


    @Before
    public void setUp() throws Exception {
        messageManager = mock(MessageManager.class);
        mvc = MockMvcBuilders.standaloneSetup(new MessageController(sessionHandler, messageManager)).build();
    }

    @Test
    public void test_Put_Reply_Adds_Reply_To_MessageManager() throws Exception {
        Reply reply = new Reply("target", "message");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/reply")
                .content(gson.toJson(reply))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(withValidSessionKey(requestBuilder)).andExpect(status().isOk());
        verify(messageManager).addReply(eq(reply));
        verifyNoMoreInteractions(messageManager);
    }

    @Test
    public void test_Post_Replies_Returns_List_Of_Replies_For_A_Matcher_List_Of_Platforms() throws Exception {
        ArrayList<String> matchers = new ArrayList<>();
        matchers.add("32487");
        matchers.add("09548");
        matchers.add("44129");
        MatcherList matcherList = new MatcherList(VALID_CLIENT_ID, matchers);

        ArrayList<Reply> expected = new ArrayList<>();
        expected.add(new Reply("32487", "sender"));
        expected.add(new Reply("09548", "sender"));
        expected.add(new Reply("44129", "sender"));
        doReturn(expected).when(messageManager).getRepliesExcludingOnesDeletedForID(eq(matchers), eq(VALID_CLIENT_ID));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/replies")
                .content(gson.toJson(matcherList))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(withValidSessionKey(requestBuilder))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(expected)));
        verify(messageManager).getRepliesExcludingOnesDeletedForID(eq(matchers), eq(VALID_CLIENT_ID));
        verifyNoMoreInteractions(messageManager);
    }

    @Test
    public void test_Delete_Reply_Marks_Reply_As_Processed_In_MessageManager() throws Exception {
        Message message = new Message();
        String replyID = "message-id";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/reply")
                .param("clientID", String.valueOf(VALID_CLIENT_ID))
                .param("id", replyID)
                .content(gson.toJson(message))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(withValidSessionKey(requestBuilder)).andExpect(status().isOk());
        verify(messageManager).markReplyAsProcessed(eq(replyID), eq(VALID_CLIENT_ID));
        verifyNoMoreInteractions(messageManager);
    }
}
