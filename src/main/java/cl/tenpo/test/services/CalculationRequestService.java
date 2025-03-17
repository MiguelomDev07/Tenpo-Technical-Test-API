package cl.tenpo.test.services;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CalculationRequestService {

    /**
     * This method is used to perform the calculation of the operation.
     * @param parameterOne
     * @param parameterTwo
     * @return
     */
    CompletableFuture<Map<String, Object>> calculateOperation(double parameterOne, double parameterTwo);
}
