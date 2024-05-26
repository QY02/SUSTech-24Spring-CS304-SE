package org.cs304.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.controller.FavoriteController;
import org.cs304.backend.entity.Favorite;
import org.cs304.backend.mapper.FavoriteMapper;
import org.cs304.backend.service.IFavoriteService;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FavoriteControllerTest {
    @InjectMocks
    FavoriteController favoriteController;

    @Mock
    FavoriteMapper favoriteMapper;

    @Mock
    IFavoriteService favoriteService;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("success - should return favorite result")
    public void shouldReturnFavoriteResult() {
        Favorite favorite = new Favorite();
        favorite.setUserId("10000000");
        favorite.setEventId(1);

        when(favoriteMapper.selectOne(new QueryWrapper<Favorite>().eq("user_id", "10000000").eq("event_id", 1))).thenReturn(new Favorite());

        Result result = favoriteController.isFavouriteExists(response, favorite);

        assertEquals("200", result.getCode());
    }
}
