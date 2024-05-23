package com.project.controller;

import com.project.modal.Chat;
import com.project.modal.Message;
import com.project.modal.User;
import com.project.request.CreateMessageRequest;
import com.project.service.MessageService;
import com.project.service.ProjectService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody CreateMessageRequest request,
                                               @RequestHeader("Authorization")String jwt)throws Exception{

        User user = userService.findUserProfileByJwt(jwt);

        Chat chat = projectService.getProjectById(request.getProjectId()).getChat();

        if (chat==null)throw new Exception("Chats not found");
        Message sendMessage = messageService.sendMessage(request.getSenderId(), request.getProjectId(), request.getContent());

        return new ResponseEntity<>(sendMessage, HttpStatus.OK);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long projectId,
                                                             @RequestHeader("Authorization")String jwt)throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        List<Message> messages = messageService.getMessagesByProjectId(projectId);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
