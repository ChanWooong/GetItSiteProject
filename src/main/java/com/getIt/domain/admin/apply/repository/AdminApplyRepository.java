package com.getit.domain.admin.apply.repository;

import com.getit.domain.apply.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface AdminApplyRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a " +
            "JOIN FETCH a.member m " +
            "JOIN FETCH m.memberInfo " +
            "WHERE a.isDraft = false " +
            "ORDER BY a.id DESC")
    Page<Application> findAllByIsDraftFalseOrderByIdDesc(Pageable pageable);
}