package org.game.dao;

import org.game.pojo.Betting;
import org.game.pojo.BettingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-09-01  19:01
 */
@Repository
public interface BettingModelDao extends JpaRepository<BettingModel, Integer>  {

    List<BettingModel> findAllByUserId(Integer userId);

}
