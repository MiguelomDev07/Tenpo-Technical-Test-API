package cl.tenpo.test.controllers;

import cl.tenpo.test.constants.GeneralConstants;
import cl.tenpo.test.domain.models.RequestHistory;
import cl.tenpo.test.services.CalculationRequestService;
import cl.tenpo.test.services.RequestHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = RestApiController.class)
public class RestApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalculationRequestService calculationRequestService;

    @MockitoBean
    private RequestHistoryService requestHistoryService;

    @Test
    void test_getOperatingCalculation_should_return_responseCorrectly_with_validInputs() throws Exception {

        String params = "?number_one=100&number_two=200";

        Map<String, Object> calculationResponse = new HashMap<>();
        calculationResponse.put(GeneralConstants.STATUS_LITERAL, "1");
        calculationResponse.put(GeneralConstants.RESPONSE_LITERAL, "345");

        CompletableFuture<Map<String, Object>> calculationResponseAsync = new CompletableFuture<>();
        calculationResponseAsync.complete(calculationResponse);

        // When
        when(calculationRequestService.calculateOperation(anyDouble(), anyDouble())).thenReturn(calculationResponseAsync);

        //Call to api
        this.mockMvc.perform(get("/tenpo/api/v1/get-operating-calculation".concat(params))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("345")));
    }

    @Test
    void test_getOperatingCalculation_should_return_UnavailableService_with_validInputs_when_externalService_isDown_and_thereAreNot_cacheData() throws Exception {

        String params = "?number_one=100&number_two=200";

        Map<String, Object> calculationResponse = new HashMap<>();
        calculationResponse.put(GeneralConstants.STATUS_LITERAL, "0");

        CompletableFuture<Map<String, Object>> calculationResponseAsync = new CompletableFuture<>();
        calculationResponseAsync.complete(calculationResponse);

        // When
        when(calculationRequestService.calculateOperation(anyDouble(), anyDouble())).thenReturn(calculationResponseAsync);

        //Call to api
        this.mockMvc.perform(get("/tenpo/api/v1/get-operating-calculation".concat(params))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void test_getOperatingCalculation_should_return_badRequest_with_missingInputs() throws Exception {

        String params = "?number_one=100";

        //Call to api
        this.mockMvc.perform(get("/tenpo/api/v1/get-operating-calculation".concat(params))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Required request parameter 'number_two' for method parameter type double is not present")));
    }

    @Test
    void test_getOperatingCalculation_should_return_badRequest_with_invalidInputs() throws Exception {
        String params = "?parameter_one=x&parameter_two=200";

        //Call to api
        this.mockMvc.perform(get("/tenpo/api/v1/get-operating-calculation".concat(params))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_getRequestHistory_should_return_responseCorrectly_with_validInputs() throws Exception {

        String params = "?page=0&size=1";
        Pageable pageable = PageRequest.of(0, 1);
        LocalDateTime date = LocalDateTime.now();

        String jsonResponse = "{\n" +
                "  \"content\": [\n" +
                "    {\n" +
                "      \"endpoint\": \"/tenpo/api/v1/get-operating-calculation\",\n" +
                "      \"parameters\": \"100-200\",\n" +
                "      \"response\": \"345\",\n" +
                "      \"creationDate\": \"2025-03-15\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"pageable\": \"INSTANCE\",\n" +
                "  \"last\": true,\n" +
                "  \"totalPages\": 1,\n" +
                "  \"totalElements\": 1,\n" +
                "  \"size\": 1,\n" +
                "  \"number\": 0,\n" +
                "  \"sort\": {\n" +
                "    \"empty\": true,\n" +
                "    \"unsorted\": true,\n" +
                "    \"sorted\": false\n" +
                "  },\n" +
                "  \"numberOfElements\": 1,\n" +
                "  \"first\": true,\n" +
                "  \"empty\": false\n" +
                "}";

        RequestHistory requestHistoryObject = new RequestHistory();
        requestHistoryObject.setEndpoint("/tenpo/api/v1/get-operating-calculation");
        requestHistoryObject.setParameters("100-200");
        requestHistoryObject.setResponse("345");
        requestHistoryObject.setCreationDate(LocalDate.parse("2025-03-15"));

        List<RequestHistory> list = new ArrayList<>();
        list.add(requestHistoryObject);
        Page<RequestHistory> pagedList = new PageImpl<>(list);

        // When
        when(requestHistoryService.retrieveRequestHistoryList(any())).thenReturn(pagedList);

        //Call to api
        this.mockMvc.perform(get("/tenpo/api/v1/get-request-history".concat(params))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    void test_getRequestHistory_should_return_badRequest_with_invalidInputs() throws Exception {

        String params = "?page=x&size=1";

        //Call to api
        this.mockMvc.perform(get("/tenpo/api/v1/get-request-history".concat(params))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
