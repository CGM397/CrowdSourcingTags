package stz.backend.web;

import org.springframework.web.bind.annotation.*;
import stz.backend.entity.Message;
import stz.backend.service.MessageManagementService;
import stz.backend.serviceImpl.MessageManagementImpl;

import java.util.ArrayList;

@RestController
@RequestMapping("/Message")
public class MessageManagementController implements MessageManagementService {

    private MessageManagementImpl messageManagement = new MessageManagementImpl();

    @Override
    @RequestMapping(value = "/getNewMessage", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<Message> getNewMessage(@RequestParam String userId) {
        return messageManagement.getNewMessage(userId);
    }

    @Override
    @RequestMapping(value = "/getReadMessage", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<Message> getReadMessage(@RequestParam String userId) {
        return messageManagement.getReadMessage(userId);
    }

    @Override
    public void createMessage(Message message) {
        messageManagement.createMessage(message);
    }

    @Override
    @RequestMapping(value = "/readMessage", method = RequestMethod.POST)
    @ResponseBody
    public void readMessage(@RequestParam String messageId, @RequestParam String userId) {
        messageManagement.readMessage(messageId,userId);
    }

    @Override
    @RequestMapping(value = "/deleteMessage", method = RequestMethod.POST)
    @ResponseBody
    public void deleteMessage(@RequestParam String messageId,@RequestParam String userId) {
        messageManagement.deleteMessage(messageId, userId);
    }

    @Override
    @RequestMapping(value = "/deleteReadMessage", method = RequestMethod.POST)
    @ResponseBody
    public void deleteReadMessage(@RequestParam String userId) {
        messageManagement.deleteReadMessage(userId);
    }

    @Override
    @RequestMapping(value = "/readAllMessage", method = RequestMethod.POST)
    @ResponseBody
    public void readAllMessage(@RequestParam String userId) {
        messageManagement.readAllMessage(userId);
    }
}
