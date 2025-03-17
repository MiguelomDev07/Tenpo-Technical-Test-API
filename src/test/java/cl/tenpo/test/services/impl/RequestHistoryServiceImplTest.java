package cl.tenpo.test.services.impl;

import cl.tenpo.test.constants.ApiEndpointConstants;
import cl.tenpo.test.domain.models.RequestHistory;
import cl.tenpo.test.repositories.jpa.RequestHistoryRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestHistoryServiceImplTest {

    @InjectMocks
    private RequestHistoryServiceImpl requestHistoryServiceImpl;

    private static final Log LOGGER = LogFactory.getLog(RequestHistoryServiceImplTest.class);

    @Mock
    private RequestHistoryRepository requestHistoryRepository;

    @Test
    void test_retrieveRequestHistoryList_should_return_responseCorrectly_with_validInputs() {
        RequestHistory requestHistoryObject = new RequestHistory();
        requestHistoryObject.setEndpoint("/tenpo/api/v1/get-operating-calculation");
        requestHistoryObject.setParameters("100-200");
        requestHistoryObject.setResponse("345");
        requestHistoryObject.setCreationDate(LocalDate.parse("2025-03-15"));

        List<RequestHistory> list = new ArrayList<>();
        list.add(requestHistoryObject);
        Page<RequestHistory> pagedList = new PageImpl<>(list);

        Pageable pageable = PageRequest.of(0, 1);

        /** When */
        when(requestHistoryRepository.findAll(pageable)).thenReturn(pagedList);

        /** It is called to main method */
        Page<RequestHistory> result = requestHistoryServiceImpl.retrieveRequestHistoryList(pageable);

        /** It is validated the output from the main method in accordance with the inputs */
        assertNotNull(result);
        assertEquals(pagedList, result);

        /** Verify */
        verify(requestHistoryRepository, times(1)).findAll(pageable);
    }

    @Test
    void test_retrieveRequestHistoryList_should_return_Exception_when_dataBase_isDown_or_has_troubles() {
        Page<RequestHistory> pagedList = new PageImpl<>(new ArrayList<>());

        Pageable pageable = PageRequest.of(0, 1);

        /** When */
        when(requestHistoryRepository.findAll(pageable)).thenThrow(new RuntimeException("The data base does not response."));

        /** It is called to main method */
        Page<RequestHistory> result = requestHistoryServiceImpl.retrieveRequestHistoryList(pageable);

        /** It is validated the output from the main method in accordance with the inputs */
        assertNotNull(result);
        assertEquals(pagedList, result);

        /** Verify */
        verify(requestHistoryRepository, times(1)).findAll(pageable);
    }

    @Test
    void test_saveAsynchronously_should_return_correctResponse_with_validInput() {
        LocalDate dateTime = LocalDate.now();
        RequestHistory requestObjectToSave = new RequestHistory();
        requestObjectToSave.setEndpoint(ApiEndpointConstants.CALCULATION_API_REQUEST_URI);
        requestObjectToSave.setParameters("100-200");
        requestObjectToSave.setResponse("345");
        requestObjectToSave.setCreationDate(dateTime);

        RequestHistory requestObjectFromResponse = new RequestHistory();
        requestObjectFromResponse.setId(1L);
        requestObjectFromResponse.setEndpoint(ApiEndpointConstants.CALCULATION_API_REQUEST_URI);
        requestObjectFromResponse.setParameters("100-200");
        requestObjectFromResponse.setResponse("345");
        requestObjectFromResponse.setCreationDate(dateTime);

        /** When */
        when(requestHistoryRepository.save(requestObjectToSave)).thenReturn(requestObjectFromResponse);

        /** It is called to main method */
        CompletableFuture<RequestHistory> result = requestHistoryServiceImpl.saveAsynchronously(requestObjectToSave);

        /** It is validated the output from the main method in accordance with the inputs */
        assertNotNull(result);

        /** Verify */
        verify(requestHistoryRepository, times(1)).save(requestObjectToSave);
    }
}