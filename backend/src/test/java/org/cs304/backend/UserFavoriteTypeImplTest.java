package org.cs304.backend;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.History;
import org.cs304.backend.entity.UserFavoriteType;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.HistoryMapper;
import org.cs304.backend.mapper.UserFavoriteTypeMapper;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.IAttachmentService;
import org.cs304.backend.service.impl.UserFavoriteTypeServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.cs304.backend.service.IEventSessionService;
import org.cs304.backend.service.INotificationService;
import org.cs304.backend.service.impl.EventServiceImpl;
import org.cs304.backend.service.impl.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class UserFavoriteTypeImplTest {
    @Mock
    private UserFavoriteTypeMapper userFavoriteTypeMapper;
    @InjectMocks
    private UserFavoriteTypeServiceImpl userFavoriteTypeService;

    @Mock
    EventMapper eventMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userFavoriteTypeService, "baseMapper", userFavoriteTypeMapper);
    }

    @Test
    @DisplayName("Successfully returns all favorite types for a user")
    public void testGetAllType() {
        // Arrange
        String userId = "user123";
        List<UserFavoriteType> userFavoriteTypes = Arrays.asList(
                new UserFavoriteType(1),
                new UserFavoriteType(2)
        );
        when(userFavoriteTypeMapper.selectList(any())).thenReturn(userFavoriteTypes);

        // Act
        JSONArray result = userFavoriteTypeService.getAllType(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
    }
    @Test
    @DisplayName("Successfully changes user's favorite types")
    public void testChangeType() {
        // Arrange
        String userId = "user123";
        String favTypes = "[1, 2, 3]";

        // Stub the delete and insert operations
        when(userFavoriteTypeMapper.delete(any())).thenReturn(1); // Assume deleting old types affects one row
        when(userFavoriteTypeMapper.insert(any(UserFavoriteType.class))).thenReturn(1); // Each insert affects one row

        // Act
        JSONArray result = userFavoriteTypeService.changeType(userId, favTypes);

        // Assert
        verify(userFavoriteTypeMapper).delete(any());
        verify(userFavoriteTypeMapper, times(3)).insert(any(UserFavoriteType.class)); // Ensure insert is called 3 times
        assertEquals(0, result.size()); // Ensure result is empty as per method logic
    }

}
