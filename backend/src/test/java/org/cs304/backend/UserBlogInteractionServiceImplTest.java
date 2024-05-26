package org.cs304.backend;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.entity.UserBlogInteraction;
import org.cs304.backend.mapper.CommentMapper;
import org.cs304.backend.mapper.UserBlogInteractionMapper;
import org.cs304.backend.service.IUserInteractionService;
import org.cs304.backend.service.impl.UserBlogInteractionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.cs304.backend.constant.constant_VoteType.DOWNVOTE;
import static org.cs304.backend.constant.constant_VoteType.UPVOTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserBlogInteractionServiceImplTest {

    @InjectMocks
    UserBlogInteractionServiceImpl userBlogInteractionService;

    @Mock
    UserBlogInteractionMapper userBlogInteractionMapper;

    @Mock
    IUserInteractionService userInteractionService;

    @Mock
    CommentMapper commentMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userBlogInteractionService, "baseMapper", userBlogInteractionMapper);
    }

    @Test
    @DisplayName("Should return correct voteType when getting blog")
    public void shouldReturnCorrectVoteTypeWhenGettingBlog() {
        Integer commentId = 1;
        UserBlogInteraction userBlogInteraction = new UserBlogInteraction();
        userBlogInteraction.setVoteType(true);

        when(userBlogInteractionMapper.selectList(any())).thenReturn(List.of(userBlogInteraction));

        JSONObject result = userBlogInteractionService.getBlog(commentId);

        assertEquals(UPVOTE, result.get("voteType"));
    }

    @Test
    @DisplayName("Should change vote when interaction does not exist")
    public void shouldChangeVoteWhenInteractionDoesNotExist() {
        Integer commentId = 1;
        String userId = "1";
        Integer voteType = UPVOTE;

        Comment comment = new Comment();
        comment.setUpVote(0);
        comment.setDownVote(0);

        when(userBlogInteractionMapper.selectOne(any())).thenReturn(null);
        when(commentMapper.selectById(any())).thenReturn(comment);

        userBlogInteractionService.changeVote(commentId, userId, voteType);

        UserBlogInteraction newUserBlogInteraction = new UserBlogInteraction();
        newUserBlogInteraction.setCommentId(commentId);
        newUserBlogInteraction.setUserId(userId);
        newUserBlogInteraction.setVoteType(voteType == UPVOTE);

        assertEquals(1, comment.getUpVote());
    }

    @Test
    @DisplayName("Should change vote when interaction already exists")
    public void shouldChangeVoteWhenInteractionAlreadyExists() {
        Integer commentId = 1;
        String userId = "1";
        Integer voteType = DOWNVOTE;

        Comment comment = new Comment();
        comment.setUpVote(1);
        comment.setDownVote(0);

        UserBlogInteraction existingUserBlogInteraction = new UserBlogInteraction();
        existingUserBlogInteraction.setCommentId(commentId);
        existingUserBlogInteraction.setUserId(userId);
        existingUserBlogInteraction.setVoteType(true); // User had previously upvoted

        when(userBlogInteractionMapper.selectOne(any())).thenReturn(existingUserBlogInteraction);
        when(commentMapper.selectById(any())).thenReturn(comment);

        userBlogInteractionService.changeVote(commentId, userId, voteType);

        assertEquals(0, comment.getUpVote());
        assertEquals(1, comment.getDownVote());
    }

    @Test
    @DisplayName("Should insert new UserBlogInteraction and update Comment when UserBlogInteraction does not exist")
    public void shouldInsertNewUserBlogInteractionAndUpdateCommentWhenUserBlogInteractionDoesNotExist() {
        // Arrange
        Integer commentId = 1;
        String userId = "1";
        Integer voteType = UPVOTE;

        Comment comment = new Comment();
        comment.setUpVote(0);
        comment.setDownVote(0);

        when(userBlogInteractionMapper.selectOne(any())).thenReturn(null);
        when(commentMapper.selectById(any())).thenReturn(comment);

        // Act
        userBlogInteractionService.changeVote(commentId, userId, voteType);

        // Assert
        verify(userBlogInteractionMapper).insert(any());
        verify(commentMapper).updateById(argThat(updatedComment -> updatedComment.getUpVote() == 1));
    }

    @Test
    @DisplayName("Should update UserBlogInteraction and Comment when UserBlogInteraction exists and voteType is UPVOTE")
    public void shouldUpdateUserBlogInteractionAndCommentWhenUserBlogInteractionExistsAndVoteTypeIsUpvote() {
        // Arrange
        Integer commentId = 1;
        String userId = "1";
        Integer voteType = UPVOTE;

        Comment comment = new Comment();
        comment.setUpVote(0);
        comment.setDownVote(1);

        UserBlogInteraction existingUserBlogInteraction = new UserBlogInteraction();
        existingUserBlogInteraction.setCommentId(commentId);
        existingUserBlogInteraction.setUserId(userId);
        existingUserBlogInteraction.setVoteType(false); // User had previously downvoted

        when(userBlogInteractionMapper.selectOne(any())).thenReturn(existingUserBlogInteraction);
        when(commentMapper.selectById(any())).thenReturn(comment);

        // Act
        userBlogInteractionService.changeVote(commentId, userId, voteType);

        // Assert
        verify(commentMapper).updateById(argThat(updatedComment -> updatedComment.getUpVote() == 1 && updatedComment.getDownVote() == 0));
    }

    @Test
    @DisplayName("Should update UserBlogInteraction and Comment when UserBlogInteraction exists and voteType is DOWNVOTE")
    public void shouldUpdateUserBlogInteractionAndCommentWhenUserBlogInteractionExistsAndVoteTypeIsDownvote() {
        // Arrange
        Integer commentId = 1;
        String userId = "1";
        Integer voteType = DOWNVOTE;

        Comment comment = new Comment();
        comment.setUpVote(1);
        comment.setDownVote(0);

        UserBlogInteraction existingUserBlogInteraction = new UserBlogInteraction();
        existingUserBlogInteraction.setCommentId(commentId);
        existingUserBlogInteraction.setUserId(userId);
        existingUserBlogInteraction.setVoteType(true); // User had previously upvoted

        when(userBlogInteractionMapper.selectOne(any())).thenReturn(existingUserBlogInteraction);
        when(commentMapper.selectById(any())).thenReturn(comment);

        // Act
        userBlogInteractionService.changeVote(commentId, userId, voteType);

        // Assert
        verify(commentMapper).updateById(argThat(updatedComment -> updatedComment.getUpVote() == 0 && updatedComment.getDownVote() == 1));
    }

    @Test
    @DisplayName("Should delete UserBlogInteraction and update Comment when UserBlogInteraction exists and voteType is neither UPVOTE nor DOWNVOTE")
    public void shouldDeleteUserBlogInteractionAndUpdateCommentWhenUserBlogInteractionExistsAndVoteTypeIsNeitherUpvoteNorDownvote() {
        // Arrange
        Integer commentId = 1;
        String userId = "1";
        Integer voteType = 0; // Neither UPVOTE nor DOWNVOTE

        Comment comment = new Comment();
        comment.setUpVote(1);
        comment.setDownVote(1);

        UserBlogInteraction existingUserBlogInteraction = new UserBlogInteraction();
        existingUserBlogInteraction.setCommentId(commentId);
        existingUserBlogInteraction.setUserId(userId);
        existingUserBlogInteraction.setVoteType(true); // User had previously upvoted

        when(userBlogInteractionMapper.selectOne(any())).thenReturn(existingUserBlogInteraction);
        when(commentMapper.selectById(any())).thenReturn(comment);

        // Act
        userBlogInteractionService.changeVote(commentId, userId, voteType);

        // Assert
        verify(userBlogInteractionMapper).delete(any());
        verify(commentMapper).updateById(argThat(updatedComment -> updatedComment.getUpVote() == 0 && updatedComment.getDownVote() == 1));
    }

    @Test
    @DisplayName("Should update UserBlogInteraction and Comment when UserBlogInteraction exists and voteType is DOWNVOTE")
    public void shouldUpdateUserBlogInteractionAndCommentWhenUserBlogInteractionExistsAndVoteTypeIsDownvote2() {
        // Arrange
        Integer commentId = 1;
        String userId = "1";
        Integer voteType = DOWNVOTE;

        Comment comment = new Comment();
        comment.setUpVote(1);
        comment.setDownVote(0);

        UserBlogInteraction existingUserBlogInteraction = new UserBlogInteraction();
        existingUserBlogInteraction.setCommentId(commentId);
        existingUserBlogInteraction.setUserId(userId);
        existingUserBlogInteraction.setVoteType(true); // User had previously upvoted

        when(userBlogInteractionMapper.selectOne(any())).thenReturn(existingUserBlogInteraction);
        when(commentMapper.selectById(any())).thenReturn(comment);

        // Act
        userBlogInteractionService.changeVote(commentId, userId, voteType);

        // Assert
        verify(commentMapper).updateById(argThat(updatedComment -> updatedComment.getUpVote() == 0 && updatedComment.getDownVote() == 1));
    }

}