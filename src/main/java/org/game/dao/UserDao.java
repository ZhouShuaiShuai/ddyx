package org.game.dao;

import org.game.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Zhouyf
 * @Data 2020-07-17  22:45
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer> , JpaSpecificationExecutor<User> {

    User findByUserName(String userName);

    User findUserByPhone(String phone);

    @Query(value = "SELECT user_name from user where id = ?1",nativeQuery = true )
    String findNameById(Integer id);


}
