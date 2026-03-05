package com.getit.domain.admin.apply.repository;

import com.getit.domain.admin.apply.entity.Application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 관리자 전용 지원서 조회 Repository
// - Application 엔티티에 대한 DB 접근 담당
@Repository
public interface AdminApplyRepository extends JpaRepository<Application, Long> {

    // 임시 저장이 아닌 지원서 전체 조회 
    // SELECT * FROM application 
    // WHERE is_draft = false
    // ORDER BY id DESC
    // LIMIT ?, ?
    //
    // @param pageable 페이지 정보 (page, size, sort)
    // @return 제출 완료된 지원서 페이지
    Page<Application> findAllByIsDraftFalseOrderByIdDesc(Pageable pageable);
}