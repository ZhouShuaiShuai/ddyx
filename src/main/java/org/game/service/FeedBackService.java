package org.game.service;

import org.game.dao.FeedBackDao;
import org.game.pojo.Feedback;
import org.game.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedBackService {

    @Autowired
    private FeedBackDao feedBackDao;

    public Result save(Feedback feedback) {
        return new Result(feedBackDao.saveAndFlush(feedback));
    }

}
