package com.netply.botchan.web.rest.messaging;

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
    public void setUp() {
        messageManager = mock(MessageManager.class);
        mvc = MockMvcBuilders.standaloneSetup(new MessageController(messageManager)).build();
    }

    @Test
    public void test_Put_Reply_Adds_Reply_To_MessageManager() throws Exception {
        Reply reply = new Reply(0, "message");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/reply")
                .content(gson.toJson(reply))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(withValidSessionKey(requestBuilder)).andExpect(status().isOk());
        verify(messageManager).addReply("node", eq(reply));
        verifyNoMoreInteractions(messageManager);
    }

    @Test
    public void test_Post_Replies_Returns_List_Of_Replies_For_A_Matcher_List_Of_Platforms() throws Exception {
        ArrayList<String> inputMatchers = new ArrayList<>();
        inputMatchers.add("32487");
        inputMatchers.add("09548");
        inputMatchers.add("44129");

        ArrayList<String> expectedMatchers = new ArrayList<>();
        for (String inputMatcher : inputMatchers) {
            expectedMatchers.add(inputMatcher + "___" + VALID_CLIENT_ID);
        }

        ArrayList<Reply> expected = new ArrayList<>();
        expected.add(new Reply(32487, "sender"));
        expected.add(new Reply(9548, "sender"));
        expected.add(new Reply(44129, "sender"));
        ArrayList<Reply> toReturn = new ArrayList<>();
        for (Reply reply : expected) {
            toReturn.add(new Reply(reply.getOriginalMessageID(), reply.getMessage()));
        }

        doReturn(toReturn).when(messageManager).getUnProcessedReplies(eq(expectedMatchers), anyString());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/replies")
                .content(gson.toJson(inputMatchers))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(withValidSessionKey(requestBuilder))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(expected)));

        verify(messageManager).getUnProcessedReplies(eq(expectedMatchers), anyString());
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
        verify(messageManager).markReplyAsProcessed(anyInt(), anyString());
        verifyNoMoreInteractions(messageManager);
    }
}
