package org.game.dao;

import org.game.pojo.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackDao extends JpaRepository<Feedback, Integer> {
}
