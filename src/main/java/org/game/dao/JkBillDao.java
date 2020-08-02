package org.game.dao;

import org.game.pojo.JkBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JkBillDao extends JpaRepository<JkBill, Integer> {

    @Query(value = "SELECT * FROM jkbill WHERE user_id = ?1",
            countQuery = "SELECT count(*) FROM jkbill WHERE user_id = ?1",
            nativeQuery = true)
    Page<JkBill> findByUserId(Integer userId, Pageable pageable);

}
