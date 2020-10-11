package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.dao.NrbDao;
import org.game.pojo.Nrb;
import org.game.result.Result;
import org.game.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "nrb")
@Api(tags = {"牛人榜接口"})
@Slf4j
public class NrbController {

    @Autowired
    private NrbDao nrbDao;

    @GetMapping("add")
    @ApiOperation(value = "添加牛人榜")
    public Result add(String name,long hl,String type,long jl){
        Nrb nrb = new Nrb();
        nrb.setHl(hl);
        nrb.setJl(jl);
        nrb.setName(name);
        nrb.setType(type);
        nrb.setHead_img(MD5.random.nextInt(1032)+".jpg");
        return new Result(nrbDao.save(nrb));
    }

    @GetMapping("del")
    @ApiOperation(value = "删除牛人榜")
    public Result del(Integer id){
        nrbDao.deleteById(id);
        return new Result("ok");
    }

    @GetMapping("sel")
    @ApiOperation(value = "查找牛人榜")
    public Result sel(String type){
        return new Result(nrbDao.findAllByType(type,Sort.by("hl").descending()));
    }

}
