package org.game.dao;

import org.game.pojo.Betting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zhouyf
 * @Data 2020-08-07  21:20
 */
@Repository
public interface BettingDao extends JpaRepository<Betting, Integer> {

    Betting findFirstByUserIdAndGameId(Integer userId,Integer gameId);

}
