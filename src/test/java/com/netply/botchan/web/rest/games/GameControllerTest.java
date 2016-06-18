package com.netply.botchan.web.rest.games;

import com.netply.botchan.web.rest.BaseControllerTest;
import org.junit.Before;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class GameControllerTest extends BaseControllerTest {
    private MockMvc mvc;


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new GameController(sessionHandler)).build();
    }
}
