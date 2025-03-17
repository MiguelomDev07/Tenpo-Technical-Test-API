package cl.tenpo.test.services.impl;

import cl.tenpo.test.components.ApiRequestComponent;
import cl.tenpo.test.config.CacheConfig;
import cl.tenpo.test.constants.ApiEndpointConstants;
import cl.tenpo.test.constants.ErrorMessageConstants;
import cl.tenpo.test.constants.GeneralConstants;
import cl.tenpo.test.domain.models.RequestHistory;
import cl.tenpo.test.services.RequestHistoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
public class CalculationRequestServiceImplTest {

    @InjectMocks
    private CalculationRequestServiceImpl calculationRequestServiceImpl;

    private static final Log LOGGER = LogFactory.getLog(CalculationRequestServiceImplTest.class);

    @Mock
    private RequestHistoryService requestHistoryService;

    @Mock
    private ApiRequestComponent apiRequestComponent;

    private final String externalServiceApiUrl = "http://localhost:8081/mock-service/api";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(calculationRequestServiceImpl, "externalServiceApiUrl", "http://localhost:8081/mock-service/api");
        ReflectionTestUtils.setField(calculationRequestServiceImpl, "cacheConfig", new CacheConfig());
    }

    @Test
    void test_calculateOperation_should_return_responseCorrectly_with_validInputs_and_externalService_isOkey () throws ExecutionException, InterruptedException {
        String status = "1";
        String percentage = String.valueOf(GeneralConstants.EXTERNAL_SERVICE_PERCENTAGE);
        Object operationResult = 345.0;

        LocalDate dateTime = LocalDate.now();
        RequestHistory calculationRequest = new RequestHistory();
        calculationRequest.setId(1L);
        calculationRequest.setEndpoint(ApiEndpointConstants.CALCULATION_API_REQUEST_URI);
        calculationRequest.setParameters("100-200");
        calculationRequest.setResponse("345");
        calculationRequest.setCreationDate(dateTime);

        Map<String, Object> response = new HashMap<>();
        response.put(GeneralConstants.RESPONSE_LITERAL, operationResult);
        response.put(GeneralConstants.STATUS_LITERAL, status);

        /** When */
        when(apiRequestComponent.get(String.class, this.externalServiceApiUrl.concat(ApiEndpointConstants.GET_PERCENTAGE_VALUE_EXTERNAL_SERVICE_URI))).thenReturn(percentage);
        when(requestHistoryService.saveAsynchronously(any())).thenReturn(CompletableFuture.completedFuture(calculationRequest));

        /** It is called to main method */
        CompletableFuture<Map<String, Object>> result = calculationRequestServiceImpl.calculateOperation(100, 200);

        /** It is validated the output from the main method in accordance with the inputs */
        assertNotNull(result);
        assertEquals(response, result.get());

        /** Verify */
        verify(apiRequestComponent, times(1)).get(String.class, this.externalServiceApiUrl.concat(ApiEndpointConstants.GET_PERCENTAGE_VALUE_EXTERNAL_SERVICE_URI));
        verify(requestHistoryService, times(1)).saveAsynchronously(any());
    }

    @Test
    void test_calculateOperation_should_return_responseCorrectly_with_validInputs_and_externalService_isDown_and_thereAreNot_cacheData () throws ExecutionException, InterruptedException {
        String status = "0";
        Object operationResult = ErrorMessageConstants.CALCULATION_ERROR_MESSAGE;

        LocalDate dateTime = LocalDate.now();
        RequestHistory calculationRequest = new RequestHistory();
        calculationRequest.setId(1L);
        calculationRequest.setEndpoint(ApiEndpointConstants.CALCULATION_API_REQUEST_URI);
        calculationRequest.setParameters("100-200");
        calculationRequest.setResponse("345");
        calculationRequest.setCreationDate(dateTime);

        Map<String, Object> response = new HashMap<>();
        response.put(GeneralConstants.RESPONSE_LITERAL, operationResult);
        response.put(GeneralConstants.STATUS_LITERAL, status);

        /** When */
        when(apiRequestComponent.get(String.class, this.externalServiceApiUrl.concat(ApiEndpointConstants.GET_PERCENTAGE_VALUE_EXTERNAL_SERVICE_URI))).thenThrow(new RuntimeException("The service has failed"));
        when(requestHistoryService.saveAsynchronously(any())).thenReturn(CompletableFuture.completedFuture(calculationRequest));

        /** It is called to main method */
        CompletableFuture<Map<String, Object>> result = calculationRequestServiceImpl.calculateOperation(100, 200);

        /** It is validated the output from the main method in accordance with the inputs */
        assertNotNull(result);
        assertEquals(response, result.get());

        /** Verify */
        verify(apiRequestComponent, times(1)).get(String.class, this.externalServiceApiUrl.concat(ApiEndpointConstants.GET_PERCENTAGE_VALUE_EXTERNAL_SERVICE_URI));
        verify(requestHistoryService, times(1)).saveAsynchronously(any());
    }

}
