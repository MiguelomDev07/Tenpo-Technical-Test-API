package cl.tenpo.test.services.impl;

import cl.tenpo.test.domain.models.RequestHistory;
import cl.tenpo.test.repositories.jpa.RequestHistoryRepository;
import cl.tenpo.test.services.RequestHistoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class RequestHistoryServiceImpl implements RequestHistoryService {

    private static final Log LOGGER = LogFactory.getLog(RequestHistoryServiceImpl.class);

    @Autowired
    private RequestHistoryRepository requestHistoryRepository;

    @Override
    public Page<RequestHistory> retrieveRequestHistoryList(Pageable pageable) {
        Page<RequestHistory> requestHistoryList;
        try {
            /**
             * 1. It is retrieved the paged request history list.
             */
            requestHistoryList = requestHistoryRepository.findAll(pageable);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            requestHistoryList = new PageImpl<>(new ArrayList<>());
        }

        return requestHistoryList;
    }

    @Async
    public CompletableFuture<RequestHistory> saveAsynchronously(RequestHistory calculationRequest) {
        LOGGER.info("Saving calculation request object in the db with the thread: " + Thread.currentThread().getName());
        RequestHistory savedCalculationRequest = requestHistoryRepository.save(calculationRequest);
        return CompletableFuture.completedFuture(savedCalculationRequest);
    }

}
