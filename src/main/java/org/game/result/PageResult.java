package org.game.result;

import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * @author Zhouyf
 * @Data 2020-07-21  12:17
 */
@Data
public class PageResult extends Result {

    public PageResult(Object obj){
        super(obj);
    }

    public PageResult(String msg, String resultCode){
        super(msg, resultCode);
    }

    public PageResult(Page page){
        super(page.getContent());
        this.pageNum = page.getTotalPages();
        this.pageIndex = page.getNumber();
        this.pageSize = page.getSize();
        this.count = page.getTotalElements();
    }

    private Integer pageNum;    //总页码

    private Integer pageSize;   //每页大小

    private Integer pageIndex;  //当前页

    private Long count;  //总条数

}
