package com.netply.botchan.web.rest.messaging;

import com.netply.botchan.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
    private MessageManager messageManager;


    @Autowired
    public MessageController(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @RequestMapping(value = "/messages", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<Message> getMessages(@RequestBody MatcherList matcherList) {

        return messageManager.getUnProcessedMessagesForPlatform(matcherList.getMatchers(), matcherList.getPlatform());
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public void addMessage(@RequestBody Message message) {

        messageManager.addMessage(message);
    }

    @RequestMapping(value = "/message", method = RequestMethod.DELETE)
    public void deleteMessage(@RequestParam(value = "platform") String platform,
                              @RequestParam(value = "id") Integer messageID) {

        messageManager.markMessageAsProcessed(messageID, platform);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.PUT)
    public void addReply(@RequestBody Reply reply) {

        messageManager.addReply(reply);
    }

    @RequestMapping(value = "/directMessage", method = RequestMethod.PUT)
    public void addDirectMessage(@RequestBody ServerMessage serverMessage) {

        messageManager.addDirectMessage(serverMessage);
    }

    @RequestMapping(value = "/replies", produces = "application/json", method = RequestMethod.POST)
    public @ResponseBody
    List<ToUserMessage> getReplies(@RequestBody MatcherList matcherList) {

        return messageManager.getUnProcessedReplies(matcherList.getMatchers(), matcherList.getPlatform());
    }

    @RequestMapping(value = "/reply", method = RequestMethod.DELETE)
    public void deleteReply(@RequestParam(value = "platform") String platform,
                            @RequestParam(value = "id") Integer replyID) {

        messageManager.markReplyAsProcessed(replyID, platform);
    }
}
