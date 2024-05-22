package org.cs304.backend;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.cs304.backend.service.impl.UserInteractionServiceImpl;
import org.cs304.backend.mapper.UserInteractionMapper;
import org.cs304.backend.entity.UserInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        when(userInteractionMapper.update(new UpdateWrapper<UserInteraction>().eq("event_id",eventId).eq("user_id",userId).set("update_type",type).set("rating",action))).thenReturn(1);

        userInteractionService.changeUserInteraction(userId, eventId, type, action);

    }
}