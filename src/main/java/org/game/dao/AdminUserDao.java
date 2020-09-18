package org.game.dao;

import org.game.pojo.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zhouyf
 * @Data 2020-09-18  15:45
 */
@Repository
public interface AdminUserDao extends JpaRepository<AdminUser, Integer> {

    AdminUser findByUserName(String userName);

}
