package org.game.dao;

import org.game.pojo.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Zhouyf
 * @Data 2020-09-22  15:18
 */
@Repository
public interface MessagesDao extends JpaRepository<Messages,Integer> {

    @Query(value = "SELECT * FROM messages WHERE `use` = ?1",
            nativeQuery = true)
    Messages findFirstByIsUse(boolean isUse);

}
