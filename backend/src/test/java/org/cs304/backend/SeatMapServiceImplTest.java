package org.cs304.backend;

import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.Seat;
import org.cs304.backend.entity.SeatMap;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.SeatMapMapper;
import org.cs304.backend.mapper.SeatMapper;
import org.cs304.backend.service.impl.SeatMapServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatMapServiceImplTest {

    @InjectMocks
    private SeatMapServiceImpl seatMapService;

    @Mock
    private SeatMapMapper seatMapMapper;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventSessionMapper eventSessionMapper;

    @Mock
    private SeatMapper seatMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(seatMapService, "baseMapper", seatMapMapper);
    }

    @Test
    @DisplayName("Should get seat map with seats by id")
    public void shouldGetSeatMapWithSeatsById() {
        Integer seatMapId = 1;
        int userType = constant_User.USER;

        SeatMap seatMap = new SeatMap();
        seatMap.setId(seatMapId);
        seatMap.setType(1);

        when(seatMapMapper.selectById(any())).thenReturn(seatMap);
        when(eventSessionMapper.selectList(any())).thenReturn(List.of(new EventSession()));
        when(eventMapper.exists(any())).thenReturn(true);
        when(seatMapper.selectList(any())).thenReturn(List.of(new Seat()));

        SeatMap result = seatMapService.getSeatMapWithSeatsById(userType, seatMapId);

        assertEquals(seatMapId, result.getId());
    }
}