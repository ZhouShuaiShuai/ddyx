package org.game.dao;

import org.game.pojo.Nrb;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-09-28  11:07
 */
@Repository
public interface NrbDao extends JpaRepository<Nrb,Integer> {

    List<Nrb> findAllByType(String type ,Sort sort);

}
