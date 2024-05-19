package org.cs304.backend;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.controller.AdminController;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.service.IEventService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AdminControllerTest {

    @InjectMocks
    AdminController adminController;

    @Mock
    IEventService eventService;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Should return audit list when user is admin")
    public void shouldReturnAuditListWhenUserIsAdmin() {
        request.setAttribute("loginUserType", 0);
        when(eventService.getAuditList("0")).thenReturn(new JSONArray());

        Result result = adminController.getAuditList(request, response, "0");

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should throw ServiceException when user is not admin")
    public void shouldThrowServiceExceptionWhenUserIsNotAdmin() {
        request.setAttribute("loginUserType", 1);

        assertThrows(ServiceException.class, () -> adminController.getAuditList(request, response, "0"));

        request.setAttribute("loginUserType", 1);
        JSONObject requestBody = new JSONObject();
        requestBody.put("eventId", 1);
        requestBody.put("status", 1);
        requestBody.put("reason", "Test reason");

        assertThrows(ServiceException.class, () -> adminController.changeAudit(request, response, requestBody));
    }

    @Test
    @DisplayName("Should change audit status when user is admin")
    public void shouldChangeAuditStatusWhenUserIsAdmin() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("eventId", 1);
        requestBody.put("status", 1);
        requestBody.put("reason", "Test reason");

        Result result = adminController.changeAudit(request, response, requestBody);

        assertEquals("200", result.getCode());
    }

}