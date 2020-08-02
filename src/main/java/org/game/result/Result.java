package org.game.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description TODO
 * @Author turnaround
 * @Date 2019-6-13
 * @Version 1.0
 **/
@Data
public class Result {

    /**
     * 结果状态码
     */
    @ApiModelProperty("结果状态码")
    protected String resultCode;
    /**
     * 响应结果描述
     */
    @ApiModelProperty("结果描述")
    protected String msg;

    /**
     * 返回对象
     */
    @ApiModelProperty("返回对象")
    private Object data;

    public Result(Object obj) {
        this.resultCode = "SUCCESS";
        this.msg = "操作成功！";
        this.data = obj;
    }

    public Result(String msg, String resultCode) {
        if (resultCode == null)
            this.resultCode = "ERROR";
        else
            this.resultCode = resultCode;

        this.msg = msg;
        this.data = null;
    }

}
