package org.game.dao;

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

    @Query(value = "select ye.game_id gameid ,ye.create_date date,sum(ye.game_money) money,g.number num from yebill ye " +
            "LEFT JOIN game g on g.id = ye.game_id " +
            "where ye.user_id = ?1 " +
            "and g.re_time = 0 and g.end_time < now()" +
            "GROUP BY ye.game_id ",
            countQuery = "select count(*) from yebill ye " +
                    "LEFT JOIN game g on g.id = ye.game_id " +
                    "where ye.user_id = ?1 " +
                    "and g.re_time = 0 and g.end_time < now() " +
                    "GROUP BY ye.game_id ",
            nativeQuery = true)
    List<Map<String,Object>> getWinOrLoserByGame(Integer userId, Pageable pageable);

    @Query(value = "select DATE_FORMAT(ye.create_date,'%Y-%m-%d') date , sum(ye.game_money) money from yebill ye " +
            "where ye.user_id = ?1 " +
            "GROUP BY DATE_FORMAT(ye.create_date,'%Y-%m-%d')" +
            "ORDER BY  ye.game_id DESC  limit 30",
            nativeQuery = true)
    List<Map<String,Object>> getWinOrLoserByDay(Integer userId);

    @Query(value = "select DATE_FORMAT(ye.create_date,'%Y-%m-%d') date , sum(ye.game_money) money , ye.user_id , u.user_name from yebill ye LEFT JOIN `user` u on ye.user_id = u.id " +
            "where DATE_FORMAT(ye.create_date,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') " +
            "group by ye.user_id\n",
            nativeQuery = true)
    List<Map<String,Object>> getAllUserBillByDate();
}
