package org.game.dao;

import org.game.pojo.YeBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface YeBillDao extends JpaRepository<YeBill, Integer> {

    @Query(value = "SELECT * FROM yebill WHERE user_id = ?1",
            countQuery = "SELECT count(*) FROM yebill WHERE user_id = ?1",
            nativeQuery = true)
    Page<YeBill> findByUserId(Integer userId,Pageable pageable);

}
