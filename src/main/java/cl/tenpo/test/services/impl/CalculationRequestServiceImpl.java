package cl.tenpo.test.services.impl;

import cl.tenpo.test.components.ApiRequestComponent;
import cl.tenpo.test.config.CacheConfig;
import cl.tenpo.test.constants.ApiEndpointConstants;
import cl.tenpo.test.constants.ErrorMessageConstants;
import cl.tenpo.test.constants.GeneralConstants;
import cl.tenpo.test.domain.models.RequestHistory;
import cl.tenpo.test.services.CalculationRequestService;

import cl.tenpo.test.services.RequestHistoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.cache.Cache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class CalculationRequestServiceImpl implements CalculationRequestService {

    private static final Log LOGGER = LogFactory.getLog(CalculationRequestServiceImpl.class);

    @Autowired
    private CacheConfig cacheConfig;

    @Autowired
    private RequestHistoryService requestHistoryService;

    @Autowired
    private ApiRequestComponent apiRequestComponent;

    @Value("${mocked.external.service.api.url}")
    private String externalServiceApiUrl;

    @Override
    public CompletableFuture<Map<String, Object>> calculateOperation(double parameterOne, double parameterTwo) {
        LOGGER.info("Starting the calculation process with the thread: " + Thread.currentThread().getName());

        Object result;
        Map<String, Object> response = new HashMap<>();

        double percentage = 0;
        double operation = 0;
        String status = "0";

        /**
         * 1. It is performed the operation.
         */
        operation = parameterOne + parameterTwo;

        /**
         * 2. It is got the percentage value from external service.
         */
        try {
            percentage = this.getPercentageFromExternalService();
            result = operation + (operation * (percentage/100));
            status = "1";
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            LOGGER.error(ErrorMessageConstants.EXTERNAL_SERVICE_FAILED_ERROR_LOG);

            /**
             * ---> it is tried to retrieve the percentage value from cache.
             */
            Cache cache = cacheConfig.getCacheBean();
            if (cache != null && cache.get(GeneralConstants.CACHE_PERCENTAGE_KEY, Double.class) != null) {
                percentage = cache.get(GeneralConstants.CACHE_PERCENTAGE_KEY, Double.class);
                result = operation + (operation * (percentage/100));
                status = "1";
            } else {

                /**
                 * ---> If there is no cache data then it is sent an error message.
                 */
                LOGGER.error(ErrorMessageConstants.THERE_IS_NOT_CACHE_ERROR_LOG);
                result = ErrorMessageConstants.CALCULATION_ERROR_MESSAGE;
            }
        }

        /**
         * 3. It is saved in db all information about the calculation request process.
         */
        RequestHistory calculationRequest = new RequestHistory();
        calculationRequest.setEndpoint(ApiEndpointConstants.CALCULATION_API_REQUEST_URI);
        calculationRequest.setParameters(parameterOne + "-" + parameterTwo);
        calculationRequest.setResponse(result.toString());
        calculationRequest.setCreationDate(LocalDateTime.now().toLocalDate());
        requestHistoryService.saveAsynchronously(calculationRequest);

        response.put(GeneralConstants.RESPONSE_LITERAL, result);
        response.put(GeneralConstants.STATUS_LITERAL, status);
        return CompletableFuture.completedFuture(response);
    }

    /**
     * This method is used to perform a get api request to the external service and to retrieve the percentage value.
     * @return
     */
    private double getPercentageFromExternalService() {
        double percentage  = Double.parseDouble(apiRequestComponent.get(String.class, this.externalServiceApiUrl.concat(ApiEndpointConstants.GET_PERCENTAGE_VALUE_EXTERNAL_SERVICE_URI)));
        Cache cache = cacheConfig.getCacheBean();
        if (cache != null) {
            cache.put(GeneralConstants.CACHE_PERCENTAGE_KEY, percentage);
        }
        return percentage;
    }

}
