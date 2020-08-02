package org.game.dao;

import org.game.pojo.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-07-24  15:43
 */
@Repository
public interface GameDao extends JpaRepository<Game, Integer> {

    @Query(value = "SELECT * FROM game where re_time = 0 group by id desc limit 10;",
            nativeQuery = true)
    List<Game> findTenGame();

}
