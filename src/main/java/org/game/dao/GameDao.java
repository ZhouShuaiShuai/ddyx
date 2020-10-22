package org.game.dao;

import org.game.pojo.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Zhouyf
 * @Data 2020-07-24  15:43
 */
@Repository
public interface GameDao extends JpaRepository<Game, Integer> {

    @Query(value = "SELECT * FROM game WHERE re_time = 0",
            countQuery = "SELECT count(*) FROM game WHERE re_time = 0",
            nativeQuery = true)
    Page<Game> findGames(Pageable pageable);

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
                    "where re_time < 1 and end_time < now()" +
                    "group by g.id desc limit 20",
            nativeQuery = true)
    List<Game> find20Games(Integer userId);


    @Query(value = "SELECT g.id,g.number,g.start_time,g.numbers,g.re_time,g.end_time,'0' as win_money, (select sum(hl) from user_info where game_id = g.id ) as jackpot ,(select count(*) from user_info where game_id = g.id ) as win_num FROM game g " +
            "where re_time < 1 " +
            "group by id desc limit 20 ",nativeQuery = true )
    List<Game> find20Games();

    @Query(value = "select sum(game_money) from yebill where user_id = ?1 group by game_id order by game_id desc limit 1",nativeQuery = true )
    Integer getLastGameMoney(Integer userId);

    @Query(value =
            /*"select " +
            "g.id gameid," +
            "g.number num ," +
            "g.number%3 as num3 ," +
            "g.number%4 as num4 ," +
            "g.number%5 as num5 ," +
            "g.id - (select g2.id from game g2 where g2.number = g.number and g2.id <= g.id ORDER BY g2.id desc limit 1,1) as jg ," +
            "if(g.number > 14,'大','小') dx," +
            "g.id - (select g2.id from game g2 where if(g2.number > 14,'大','小') = if(g.number > 14,'大','小') and g2.id <= g.id ORDER BY g2.id desc limit 1,1) as dxjg," +
            "if(g.number BETWEEN 10 AND 17 ,'中','边') zb," +
            "g.id - (select g2.id from game g2 where if(g2.number BETWEEN 10 AND 17 ,'中','边') = if(g.number BETWEEN 10 AND 17 ,'中','边') and g2.id <= g.id ORDER BY g2.id desc limit 1,1) as zbjg," +
            "if(g.number % 2 = 0 ,'双','单') ds," +
            "g.id - (select g2.id from game g2 where if(g2.number % 2 = 0 ,'双','单') = if(g.number % 2 = 0 ,'双','单') and g2.id <= g.id ORDER BY g2.id desc limit 1,1) as dsjg," +
            "(select count(*) from (select * from game where number is not null ORDER BY id desc limit 200) g2 where g2.number = g.number GROUP BY g2.number) sjcs " +
            "from game g where g.number is not null ORDER BY g.id desc limit 200;",*/
            "select " +
            "g.id gameid," +
            "g.number num ," +
            "g.number%3 as num3 ," +
            "g.number%4 as num4 ," +
            "g.number%5 as num5 ," +
            "if(g.number > 14,'大','小') dx," +
            "if(g.number BETWEEN 10 AND 17 ,'中','边') zb," +
            "if(g.number % 2 = 0 ,'双','单') ds " +
            "from game g where g.number is not null ORDER BY g.id desc limit 200;",
            nativeQuery = true)
    List<Map<String,Integer>> find100GameNum();

    @Query(value = "select g.number num ," +
            "(select id from game  where re_time = 150 ORDER BY id desc limit 1) - g.id -1 as jg , " +
            "(select id from game  where re_time = 150 ORDER BY id desc limit 1) - (select id from game where number >= 14 ORDER BY id desc limit 1) - 1 dxdjg," +
            "(select id from game  where re_time = 150 ORDER BY id desc limit 1) - (select id from game where number <= 14 ORDER BY id desc limit 1) - 1 dxxjg," +
            "(select id from game  where re_time = 150 ORDER BY id desc limit 1) - (select id from game where number % 2 = 1 ORDER BY id desc limit 1 ) - 1 dsdjg," +
            "(select id from game  where re_time = 150 ORDER BY id desc limit 1) - (select id from game where number % 2 = 0 ORDER BY id desc limit 1) - 1 dssjg," +
            "(select id from game  where re_time = 150 ORDER BY id desc limit 1) - (select id from game where number BETWEEN 10 AND 17 ORDER BY id desc limit 1) - 1 zbzjg," +
            "(select id from game  where re_time = 150 ORDER BY id desc limit 1) - (select id from game where number < 10 or number >= 17 ORDER BY id desc limit 1) - 1 zbbjg," +
            "(select count(*) from (select * from game where number is not null ORDER BY id desc limit 200) g2 where g2.number >= 14 ORDER BY id desc limit 200 ) dxdcs," +
            "(select count(*) from (select * from game where number is not null ORDER BY id desc limit 200) g2 where g2.number < 14 ORDER BY id desc limit 200 ) dxxcs," +
            "(select count(*) from (select * from game where number is not null ORDER BY id desc limit 200) g2 where g2.number % 2 = 1 ORDER BY id desc limit 200 ) dsdcs," +
            "(select count(*) from (select * from game where number is not null ORDER BY id desc limit 200) g2 where g2.number % 2 = 0 ORDER BY id desc limit 200 ) dsscs," +
            "(select count(*) from (select * from game where number is not null ORDER BY id desc limit 200) g2 where g2.number BETWEEN 10 AND 17 ORDER BY id desc limit 200 ) zbzcs," +
            "(select count(*) from (select * from game where number is not null ORDER BY id desc limit 200) g2 where g2.number < 10 or g2.number > 17 ORDER BY id desc limit 200 ) zbbcs," +
            "(select count(*) from (select * from game where number is not null ORDER BY id desc limit 200) g2 where g2.number = g.number GROUP BY g2.number) sjcs " +
            "from game g where g.id in " +
            "(select max(id) from game where number is not null GROUP BY number) ORDER BY g.number;",
            nativeQuery = true)
    List<Map<String,Integer>> findNumJg();

}
