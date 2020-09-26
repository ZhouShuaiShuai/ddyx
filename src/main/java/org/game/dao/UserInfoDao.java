package org.game.dao;

import org.game.pojo.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Zhouyf
 * @Data 2020-08-15  20:20
 */
@Repository
public interface UserInfoDao extends JpaRepository<UserInfo,Integer> {

    Page<UserInfo> findAllByGameIdOrderByYlDesc(Integer gameId, Pageable pageable);

    List<UserInfo> findAllByGameId(Integer gameId);

    @Query(value = "select ui.user_name,sum(ui.hl) hl from user_info ui " +
            "where DATE_FORMAT( ui.create_date,'%Y-%m-%d') = DATE_FORMAT(now(), '%Y-%m-%d') " +
            "GROUP BY ui.user_name ORDER BY hl desc LIMIT 50 ",
            nativeQuery = true)
    List<Map<String,Object>> findDayRanking();

    @Query(value = "select ui.user_name,sum(ui.hl) hl from user_info ui " +
            "where DATE_FORMAT( ui.create_date,'%Y-%m') = DATE_FORMAT(now(), '%Y-%m') " +
            "GROUP BY ui.user_name ORDER BY hl desc LIMIT 50 ",
            nativeQuery = true)
    List<Map<String,Object>> findMonthRanking();

    @Query(value = "select ui.user_name,sum(ui.hl) hl from user_info ui " +
            "where DATE_FORMAT( ui.create_date,'%Y') = DATE_FORMAT(now(), '%Y') " +
            "GROUP BY ui.user_name ORDER BY hl desc LIMIT 50 ",
            nativeQuery = true)
    List<Map<String,Object>> findYearRanking();

}
