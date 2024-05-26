package org.cs304.backend;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.cs304.backend.entity.UserInteraction;
import org.cs304.backend.mapper.UserInteractionMapper;
import org.cs304.backend.service.impl.UserInteractionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.cs304.backend.constant.constant_InteractionType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserInteractionServiceImplTest {

    @InjectMocks
    UserInteractionServiceImpl userInteractionService;

    @Mock
    UserInteractionMapper userInteractionMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userInteractionService, "baseMapper", userInteractionMapper);
    }

    @Test
    @DisplayName("Should change user interaction when interaction does not exist")
    public void shouldChangeUserInteractionWhenInteractionDoesNotExist() {
        String userId = "1";
        Integer eventId = 1;
        Integer type = 1;
        Integer action = 1;

        UserInteraction newUserInteraction = new UserInteraction();
        newUserInteraction.setUserId(userId);
        newUserInteraction.setEventId(eventId);
        newUserInteraction.setUpdateType(type);
        newUserInteraction.setRating(action);

        when(userInteractionMapper.selectOne(any())).thenReturn(null);
        when(userInteractionMapper.insert(newUserInteraction)).thenReturn(1);
        userInteractionService.changeUserInteraction(userId, eventId, type, action);

    }

    @Test
    @DisplayName("Should change user interaction when interaction exists and type is greater than or equal to update type")
    public void shouldChangeUserInteractionWhenInteractionExistsAndTypeIsGreaterThanOrEqualToUpdateType() {
        String userId = "1";
        Integer eventId = 1;
        Integer type = 1;
        Integer action = 1;

        UserInteraction existingInteraction = new UserInteraction();
        existingInteraction.setUserId(userId);
        existingInteraction.setEventId(eventId);
        existingInteraction.setUpdateType(0);
        existingInteraction.setRating(0);

        UserInteraction updatedUserInteraction = new UserInteraction();
        updatedUserInteraction.setUserId(userId);
        updatedUserInteraction.setEventId(eventId);
        updatedUserInteraction.setUpdateType(type);
        updatedUserInteraction.setRating(action);

        when(userInteractionMapper.selectOne(any())).thenReturn(existingInteraction);
        when(userInteractionMapper.update(new UpdateWrapper<UserInteraction>().eq("event_id", eventId).eq("user_id", userId).set("update_type", type).set("rating", action))).thenReturn(1);

        userInteractionService.changeUserInteraction(userId, eventId, type, action);

    }

    @Test
    @DisplayName("Should change rating when interaction type is STAR or BLOG")
    public void shouldChangeRatingWhenInteractionTypeIsStarOrBlog() {
        // Arrange
        String userId = "1";
        Integer eventId = 1;
        Integer action = 5;

        // Act
        userInteractionService.changeUserInteraction(userId, eventId, STAR, action);
        userInteractionService.changeUserInteraction(userId, eventId, BLOG, action);

        // Assert
        verify(userInteractionMapper, times(2)).insert(argThat(userInteraction -> userInteraction.getRating().equals(action)));
    }

    @Test
    @DisplayName("Should change rating when interaction type is FAVORITE")
    public void shouldChangeRatingWhenInteractionTypeIsFavorite() {
        // Arrange
        String userId = "1";
        Integer eventId = 1;

        // Act
        userInteractionService.changeUserInteraction(userId, eventId, FAVORITE, null);

        // Assert
        verify(userInteractionMapper).insert(argThat(userInteraction -> userInteraction.getRating().equals(5)));
    }

    @Test
    @DisplayName("Should change rating when interaction type is ATTEND")
    public void shouldChangeRatingWhenInteractionTypeIsAttend() {
        // Arrange
        String userId = "1";
        Integer eventId = 1;

        // Act
        userInteractionService.changeUserInteraction(userId, eventId, ATTEND, null);

        // Assert
        verify(userInteractionMapper).insert(argThat(userInteraction -> userInteraction.getRating().equals(4)));
    }

    @Test
    @DisplayName("Should change rating when interaction type is HISTORY")
    public void shouldChangeRatingWhenInteractionTypeIsHistory() {
        // Arrange
        String userId = "1";
        Integer eventId = 1;

        // Act
        userInteractionService.changeUserInteraction(userId, eventId, HISTORY, null);

        // Assert
        verify(userInteractionMapper).insert(argThat(userInteraction -> userInteraction.getRating().equals(3)));
    }

    @Test
    @DisplayName("Should update user interaction when interaction exists and type is greater than or equal to update type")
    public void shouldUpdateUserInteractionWhenInteractionExistsAndTypeIsGreaterThanOrEqualToUpdateType() {
        String userId = "1";
        Integer eventId = 1;
        Integer type = 2;
        Integer action = 5;

        UserInteraction existingInteraction = new UserInteraction();
        existingInteraction.setUserId(userId);
        existingInteraction.setEventId(eventId);
        existingInteraction.setUpdateType(1);
        existingInteraction.setRating(3);

        when(userInteractionMapper.selectList(any())).thenReturn(Collections.singletonList(existingInteraction));

        userInteractionService.changeUserInteraction(userId, eventId, type, action);

    }
}