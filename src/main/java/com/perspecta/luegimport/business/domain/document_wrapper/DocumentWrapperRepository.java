package com.perspecta.luegimport.business.domain.document_wrapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentWrapperRepository extends JpaRepository<DocumentWrapper, Long> {
	DocumentWrapper findByDocument_Id(Long id);
}
