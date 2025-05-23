package com.migros.couriertracking.courier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros.couriertracking.common.util.ObjectMapperCreator;
import com.migros.couriertracking.courier.dto.CreateOrUpdateCourierRequest;
import com.migros.couriertracking.courier.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourierControllerTest {

    @InjectMocks
    private CourierController courierController;

    @Mock
    private CourierService courierService;

    private ObjectMapper mapper ;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mapper = ObjectMapperCreator.getInstance();
        mockMvc = MockMvcBuilders.standaloneSetup(courierController).build();
    }

    @Test
    void should_create_courier() throws Exception {

        //GIVEN
        var createCourierRequest = new CreateOrUpdateCourierRequest("John", "Doe", "1234567890", "11223344556");
        doNothing().when(courierService).createCourier(any(CreateOrUpdateCourierRequest.class));

        //WHEN
        var resultActions = mockMvc.perform(post("/v1/courier").contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(mapper.writeValueAsString(createCourierRequest))
                                                                    .accept("application/json;charset=UTF-8"));

        //THEN
        resultActions.andExpect(status().is(200));
        resultActions.andDo(MockMvcResultHandlers.print());
        verify(courierService, times(1)).createCourier(any(CreateOrUpdateCourierRequest.class));
    }

}
