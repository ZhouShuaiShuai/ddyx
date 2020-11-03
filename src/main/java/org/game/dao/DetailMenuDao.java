package org.game.dao;

import org.game.pojo.DetailMenu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailMenuDao extends JpaRepository<DetailMenu, Integer> {

    @Query(value = "select count(*) from detail_menu dm where dm.user_name = ?1 and ystate = 1 and ytype = ?2",
            nativeQuery = true)
    Integer findOnebyUserName(String userName,Integer ytype);

    @Query(value = "SELECT * FROM detail_menu WHERE yType = ?1 and yState = ?2 ",
            countQuery = "SELECT COUNT(*) FROM detail_menu WHERE yType = ?1 and yState = ?2 ",
            nativeQuery = true)
    List<DetailMenu> findAllByYtypeAndYstate(Integer yType,Integer yState, Pageable pageable);

}
