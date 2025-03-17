package cl.tenpo.test.services;

import cl.tenpo.test.domain.models.RequestHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.concurrent.CompletableFuture;

public interface RequestHistoryService {

    /**
     * This method is used to perform a query to retrieve the request history data in a paged list.
     * @param pageable
     * @return
     */
    Page<RequestHistory> retrieveRequestHistoryList(Pageable pageable);

    /**
     * This method is used to store the request object in the RequestHistory table in db.
     * @param calculationRequest
     * @return
     */
    CompletableFuture<RequestHistory> saveAsynchronously(RequestHistory calculationRequest);

}
