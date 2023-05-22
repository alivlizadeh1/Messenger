package com.av.user.service;

import com.av.user.entity.MessageType;
import com.av.user.exception.Message.MessageNotFoundException;
import com.av.user.exception.User.UserMessageFoundException;
import com.av.user.exception.User.UserNotFoundException;
import com.av.user.repository.MessageRepositoryImpl;
import com.av.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MessagesServiceImpl implements MessagesService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final MessageRepositoryImpl messageRepository;

    @Autowired
    public MessagesServiceImpl(UserRepository userRepository, RestTemplate restTemplate, UserService userService, MessageRepositoryImpl messageRepository) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.messageRepository = messageRepository;
    }

    @Override
    public void addMessage(Long userId, String messageId , List<MessageType> messageTypes)
            throws MessageNotFoundException, UserNotFoundException , UserMessageFoundException {
        // fixme : check it was working or not
        isMessageIdExist(messageId);
        checkUserIdIsExist(userId);
        // fixme : add message to db
        messageRepository.insertMessage(userId , messageId , messageTypes);
    }

    private void checkUserIdIsExist(Long userId) throws UserNotFoundException{
        userService.findById(userId);
    }

    private void isMessageIdExist(String messageId) throws MessageNotFoundException{
        restTemplate.getForObject(
                "http://localhost:8081/messages/{id}/id",
                String.class ,
                messageId
        );
    }
}
