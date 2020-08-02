package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.dao.UserDao;
import org.game.pojo.Feedback;
import org.game.pojo.User;
import org.game.result.Result;
import org.game.service.FeedBackService;
import org.game.util.StringUtils;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "feedback")
@Api(tags = {"意见反馈"})
@Slf4j
public class FeedBackController {

    @Autowired
    private FeedBackService feedBackService;

    @Autowired
    private UserDao userDao;

    @GetMapping("sendFeedback")
    @ApiOperation(value = "意见反馈")
    public Result feedback(String msg, String phoneOrMail, HttpServletRequest req) {
        if (StringUtils.isEmpty(phoneOrMail)) return new Result("联系方式不能为空！");
        User user = UserUtil.getUserByReq(req, userDao);
        Feedback feedback = new Feedback(msg, phoneOrMail, user.getId());
        return feedBackService.save(feedback);
    }
}
