package org.game.dao;

import org.game.pojo.DetailMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailMenuDao extends JpaRepository<DetailMenu, Integer> {

    @Query(value = "select count(*) from detail_menu dm where dm.user_name = ?1 and ystate = 1",
            nativeQuery = true)
    Integer findOnebyUserName(String userName);

    List<DetailMenu> findAllByYtypeAndYstateOrderByUpdateDateDesc(Integer yType,Integer yState);

}
