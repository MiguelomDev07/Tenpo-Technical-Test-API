package cl.tenpo.test.controllers;

import cl.tenpo.test.constants.GeneralConstants;
import cl.tenpo.test.domain.models.RequestHistory;
import cl.tenpo.test.services.CalculationRequestService;
import cl.tenpo.test.services.RequestHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/tenpo/api/v1")
@Tag(name = "The API Endpoints for the Tenpo Technical Test!")
public class RestApiController {

    @Autowired
    private CalculationRequestService calculationRequestService;

    @Autowired
    private RequestHistoryService requestHistoryService;

    @GetMapping("/get-operating-calculation")
    @ResponseBody
    @Operation(summary = "This endpoint allows to perform a calculation of the operation between two numbers and apply it a specific percentage.")
    public ResponseEntity getOperatingCalculation(
            @RequestParam(name = "number_one") double numberOne,
            @RequestParam(name = "number_two") double numberTwo
    ) throws ExecutionException, InterruptedException {
        HttpStatus httpStatus;
        CompletableFuture<Map<String, Object>> calculationResponseAsync;
        Map<String, Object> calculationResponse;

        /**
         * 1. It is performed the calculation process
         */
        calculationResponseAsync = calculationRequestService.calculateOperation(numberOne, numberTwo);
        calculationResponse = calculationResponseAsync.get();

        /**
         * 2. It is validated the status
         */
        if (calculationResponse != null && calculationResponse.get(GeneralConstants.STATUS_LITERAL) == "1") {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        }

        return ResponseEntity.status(httpStatus).body(calculationResponse.get(GeneralConstants.RESPONSE_LITERAL));
    }

    @GetMapping("/get-request-history")
    @ResponseBody
    @Operation(summary = "This endpoint allows to perform a recovery of the request history paged list.")
    public ResponseEntity<Page<RequestHistory>> getRequestHistory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        /**
         * 1. It is performed the request history query
         */
        Pageable pageable = PageRequest.of(page, size);
        Page<RequestHistory> paginedRequestHistoryList = requestHistoryService.retrieveRequestHistoryList(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(paginedRequestHistoryList);
    }

}
