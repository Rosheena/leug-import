package com.perspecta.luegimport.business.domain.document_wrapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentWrapperRepository extends JpaRepository<DocumentWrapper, Long> {
	List<DocumentWrapper> findByUserNameEquals(String userName);
}
