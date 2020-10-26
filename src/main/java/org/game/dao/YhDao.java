package org.game.dao;

import org.game.pojo.YeBill;
import org.game.pojo.Yh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zhouyf
 * @Data 2020-10-26  19:31
 */
@Repository
public interface YhDao extends JpaRepository<Yh, Integer> {



}
