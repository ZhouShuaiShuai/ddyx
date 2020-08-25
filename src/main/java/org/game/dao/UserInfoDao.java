package org.game.dao;

import org.game.pojo.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-08-15  20:20
 */
@Repository
public interface UserInfoDao extends JpaRepository<UserInfo,Integer> {

    Page<UserInfo> findAllByGameIdOrderByYlDesc(Integer gameId, Pageable pageable);

    List<UserInfo> findAllByGameId(Integer gameId);

}
