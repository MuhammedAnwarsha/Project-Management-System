package com.project.controller;

import com.project.modal.Issue;
import com.project.modal.IssueDto;
import com.project.modal.User;
import com.project.request.IssueRequest;
import com.project.response.MessageResponse;
import com.project.service.IssueService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long issueId,
                                              @RequestHeader("Authorization")String jwt)throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(issueService.getIssueById(issueId), HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Issue>> getIssueByProjectId(@PathVariable Long projectId,
                                                           @RequestHeader("Authorization")String jwt)throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(issueService.getIssueByProjectId(projectId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<IssueDto> createIssue(@RequestBody IssueRequest issue,
                                                @RequestHeader("Authorization")String jwt)throws Exception{

       User tokenUser = userService.findUserProfileByJwt(jwt);
       User user = userService.findUserById(tokenUser.getId());

       Issue createdIssue = issueService.createIssue(issue,tokenUser);

       IssueDto issueDto = new IssueDto();
       issueDto.setDescription(createdIssue.getDescription());
       issueDto.setDueDate(createdIssue.getDueDate());
       issueDto.setId(createdIssue.getId());
       issueDto.setPriority(createdIssue.getPriority());
       issueDto.setProject(createdIssue.getProject());
       issueDto.setProjectId(createdIssue.getProjectID());
       issueDto.setStatus(createdIssue.getStatus());
       issueDto.setTitle(createdIssue.getTitle());
       issueDto.setTags(createdIssue.getTags());
       issueDto.setAssignee(createdIssue.getAssignee());

       return new ResponseEntity<>(issueDto,HttpStatus.OK);
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<MessageResponse> deleteIssue(@PathVariable Long issueId,
                                                       @RequestHeader("Authorization")String jwt)throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        issueService.deleteIssue(issueId,user.getId());
        MessageResponse res = new MessageResponse("Issue deleted");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{issueId}/assignee/{userId}")
    public ResponseEntity<Issue> addUserToIssue(@PathVariable Long issueId,
                                                @PathVariable Long userId,
                                                @RequestHeader("Authorization")String jwt)throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        Issue issue = issueService.addUserToIssue(issueId,userId);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<Issue> updateIssueStatus(@PathVariable Long issueId,
                                                   @PathVariable String status,
                                                   @RequestHeader("Authorization")String jwt)throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        Issue issue = issueService.updateStatus(issueId,status);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }
}
