package org.game.controller;

import org.game.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Zhouyf
 * @Data 2020-07-18  13:33
 */

public class UserErrorController {

    @GetMapping("loginTimeOut")
    @ApiIgnore
    public Result loginTimeOut() {
        return new Result("登录超时！", "LOGINTIMEOUT");
    }

    @GetMapping("isNotLogin")
    @ApiIgnore
    public Result isNotLogin() {
        return new Result("请登录后访问！", "ISNOTLOGIN");
    }

    @GetMapping("userError")
    @ApiIgnore
    public Result userError() {
        return new Result("用户已在其它地方登录！", "USERERROR");
    }

    @GetMapping("userIsNull")
    @ApiIgnore
    public Result userIsNull() {
        return new Result("用户信息有误！", "USERISNULL");
    }

}
