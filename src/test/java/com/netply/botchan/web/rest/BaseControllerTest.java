package com.netply.botchan.web.rest;

import com.google.gson.Gson;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
@Ignore
public class BaseControllerTest {
    public static final String VALID_SESSION_KEY = "VALID_SESSION_KEY";
    public static final String INVALID_SESSION_KEY = "INVALID_SESSION_KEY";
    public static final int INVALID_CLIENT_ID = -1;
    public static final int VALID_CLIENT_ID = 10;
    protected Gson gson = new Gson();


    protected void testInvalidSession(MockMvc mvc, String url) throws Exception {
        testInvalidSession(mvc, url, new LinkedMultiValueMap<>());
    }

    protected void testInvalidSession(MockMvc mvc, String url, MultiValueMap<String, String> params) throws Exception {
        testInvalidSession(mvc, MockMvcRequestBuilders.get(url).params(params));
    }

    protected void testInvalidSession(MockMvc mvc, MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mvc.perform(requestBuilder.param("sessionKey", INVALID_SESSION_KEY))
                .andExpect(status().isUnauthorized());
    }

    protected MockHttpServletRequestBuilder withValidSessionKey(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.param("sessionKey", VALID_SESSION_KEY);
    }
}
