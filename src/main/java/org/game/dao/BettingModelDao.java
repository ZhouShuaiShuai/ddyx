package org.game.dao;

import org.game.pojo.Betting;
import org.game.pojo.BettingModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Zhouyf
 * @Data 2020-09-01  19:01
 */
@Repository
public interface BettingModelDao extends JpaRepository<BettingModel, Integer>  {

    List<BettingModel> findAllByUserId(Integer userId);

    @Query(value = "select sum(ye.game_money) from yebill ye where ye.user_id = ?1 and DATE_FORMAT( ye.create_date,'%Y-%m-%d') = DATE_FORMAT(now(), '%Y-%m-%d');",
            nativeQuery = true)
    String findToDay(Integer userId);

    @Query(value = "select sum(ye.game_money) from yebill ye where ye.user_id = ?1 and TO_DAYS( NOW( ) ) - TO_DAYS(ye.create_date) <= 1;",
    nativeQuery = true)
    String findLastDay(Integer userId);

    @Query(value = "select sum(ye.game_money) from yebill ye where ye.user_id = ?1 and YEARWEEK(date_format(ye.create_date,'%Y-%m-%d')) = YEARWEEK(now());",
            nativeQuery = true)
    String findWeek(Integer userId);

    @Query(value = "select sum(ye.game_money) from yebill ye where ye.user_id = ?1 and DATE_FORMAT( ye.create_date,'%Y-%m') = DATE_FORMAT(now(), '%Y-%m');",
            nativeQuery = true)
    String findMonth(Integer userId);

    @Query(value = "select ye.game_id gameid ,ye.create_date date,ye.game_money money,(select `number` from game where id = ye.game_id) num " +
            "from yebill ye where ye.user_id = ?1 ",
            countQuery = "select count(*) " +
                    "from yebill ye where ye.user_id = ?1 ",
            nativeQuery = true)
    List<Map<String,Object>> getWinOrLoserByGame(Integer userId, Pageable pageable);

    @Query(value = "select DATE_FORMAT(ye.create_date,'%Y-%m-%d') date , sum(ye.game_money) money from yebill ye " +
            "where ye.user_id = ?1 " +
            "GROUP BY DATE_FORMAT(ye.create_date,'%Y-%m-%d')" +
            "ORDER BY  ye.game_id DESC  limit 30",
            nativeQuery = true)
    List<Map<String,Object>> getWinOrLoserByDay(Integer userId);

}
