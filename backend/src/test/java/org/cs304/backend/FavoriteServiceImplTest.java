package org.cs304.backend;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.Favorite;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.FavoriteMapper;
import org.cs304.backend.service.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FavoriteServiceImplTest {
    @Mock
    private FavoriteMapper favoriteMapper;
    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Mock
    EventMapper eventMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(favoriteService, "baseMapper", favoriteMapper);
    }

    @Test
    @DisplayName("Successfully deletes a favorite entry")
    public void testDeleteFavorite_Success() {
        // Arrange
        Favorite favorite = new Favorite();
        favorite.setUserId("user123");
        favorite.setEventId(1);

        // Stubbing the delete operation to return an affected row count
        when(favoriteMapper.delete((Wrapper<Favorite>) any(QueryWrapper.class))).thenReturn(1);

        // Act
        favoriteService.deleteFavorite(favorite);

        // Assert
        verify(favoriteMapper).delete((Wrapper<Favorite>) any(QueryWrapper.class));  // Verify that delete was actually called
    }

    @Test
    @DisplayName("Throws ServiceException when deleting with invalid data")
    public void testDeleteFavorite_InvalidData() {
        // Arrange
        Favorite favorite = new Favorite(); // Missing userId and eventId

        // Act & Assert
        assertThrows(ServiceException.class, () -> favoriteService.deleteFavorite(favorite));
    }

    @Test
    @DisplayName("Successfully retrieves all favorite events for a user")
    public void testGetAllFavorite_Success() {
        // Arrange
        String userId = "user123";
        List<Favorite> favorites = Arrays.asList(new Favorite(userId, 1), new Favorite(userId, 2));
        Event event1 = new Event();
        event1.setId(1);
        Event event2 = new Event();
        event2.setId(2);

        when(favoriteMapper.selectList(any())).thenReturn(favorites);
        when(eventMapper.selectById(1)).thenReturn(event1);
        when(eventMapper.selectById(2)).thenReturn(event2);

        // Act
        JSONArray jsonArray = favoriteService.getAllFavorite(userId);

        // Assert
        assertNotNull(jsonArray);
        assertEquals(2, jsonArray.size()); // Assuming that there are no duplicate eventIds
        verify(eventMapper).selectById(1);
        verify(eventMapper).selectById(2);
    }

    @Test
    @DisplayName("Returns an empty JSONArray when there are no favorites")
    public void testGetAllFavorite_NoFavorites() {
        // Arrange
        String userId = "user123";

        when(favoriteMapper.selectList(any())).thenReturn(Arrays.asList());

        // Act
        JSONArray jsonArray = favoriteService.getAllFavorite(userId);

        // Assert
        assertNotNull(jsonArray);
        assertTrue(jsonArray.isEmpty());
    }

}
