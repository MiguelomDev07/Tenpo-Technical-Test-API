package cl.tenpo.test.repositories.jpa;

import cl.tenpo.test.domain.models.RequestHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory, Long> {

    /**
     *
     *
     * @param pageable
     * @return
     */
    @Override
    @Transactional
    Page<RequestHistory> findAll(Pageable pageable);

}
