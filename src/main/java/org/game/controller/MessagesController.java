package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.game.dao.MessagesDao;
import org.game.pojo.Messages;
import org.game.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "messages")
@Api(tags = {"喇叭通知相关接口"})
public class MessagesController {

    @Autowired
    private MessagesDao messagesDao;

    @GetMapping("findAll")
    @ApiOperation(value = "获取所有")
    public Result findAll(){
        return new Result(messagesDao.findAll());
    }

    @GetMapping("findUse")
    @ApiOperation(value = "获取正在使用的")
    public Result findUse(){
        Messages messages = messagesDao.findFirstByIsUse(true);
        return new Result(messages);
    }

    @PostMapping("addMessages")
    @ApiOperation(value = "添加喇叭通知")
    public Result addMessages(String msg){
        Messages messages = new Messages();
        messages.setMessage(msg);
        messages.setUse(false);
        return new Result(messagesDao.save(messages));
    }

    @GetMapping("delMessages")
    @ApiOperation(value = "删除喇叭通知")
    public Result delMessages(Integer id){
        messagesDao.deleteById(id);
        return new Result("ok");
    }

    @GetMapping("closeMessages")
    @ApiOperation(value = "禁用喇叭通知")
    public Result closeMessages(Integer id){
        Messages messages = messagesDao.findById(id).get();
        messages.setUse(false);
        return new Result(messagesDao.saveAndFlush(messages));
    }

    @GetMapping("openMessages")
    @ApiOperation(value = "启用喇叭通知")
    public Result openMessages(Integer id){
        Messages messages = messagesDao.findById(id).get();
        messages.setUse(true);
        return new Result(messagesDao.saveAndFlush(messages));
    }

}
