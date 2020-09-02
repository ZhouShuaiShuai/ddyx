package org.game.dao;

import org.game.pojo.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-07-24  15:43
 */
@Repository
public interface GameDao extends JpaRepository<Game, Integer> {

    @Query(value = "SELECT * FROM game where re_time < 1 group by id desc limit 10;",
            nativeQuery = true)
    List<Game> findTenGame();

    @Query(value = "SELECT * FROM game where re_time < 1 group by id desc limit 20;",
            nativeQuery = true)
    List<Game> find20Game();

    @Query(value = "SELECT g.id,g.jackpot,g.number,g.start_time,g.numbers,g.re_time,g.end_time,g.win_num, sum(y.game_money) as win_money FROM game g " +
            "LEFT JOIN yebill y on g.id = y.game_id and y.user_id = ?1 " +
            "where re_time < 1 " +
            "group by id desc limit 20 ",
            nativeQuery = true)
    List<Game> find20GameByUser(Integer userId);


    @Query(value =
            "SELECT " +
                    "g.id, " +
                    "(select sum(hl) from user_info where game_id = g.id ) as jackpot ," +
                    "(select count(*) from user_info where game_id = g.id ) as win_num," +
                    "g.number,g.start_time,g.numbers,g.re_time,g.end_time, " +
                    "(select sum(game_money) from yebill where game_id = g.id and user_id = ?1) as win_money " +
                    "FROM game g " +
                    "where re_time < 1 " +
                    "group by g.id desc limit 20",
            nativeQuery = true)
    List<Game> find20Games(Integer userId);


    @Query(value = "SELECT g.id,g.number,g.start_time,g.numbers,g.re_time,g.end_time,'0' as win_money, (select sum(hl) from user_info where game_id = g.id ) as jackpot ,(select count(*) from user_info where game_id = g.id ) as win_num FROM game g " +
            "where re_time < 1 " +
            "group by id desc limit 20 ",nativeQuery = true )
    List<Game> find20Games();

    @Query(value = "select sum(game_money) from yebill where user_id = ?1 group by game_id order by game_id desc limit 1",nativeQuery = true )
    Integer getLastGameMoney(Integer userId);

}
