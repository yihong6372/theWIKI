package net.geekh.wiki.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:19
 */

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CommonResponseVo<T> {

    private Integer status;

    private String msg;

    private T date;


    public CommonResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public CommonResponseVo(Integer status, T date) {
        this.status = status;
        this.date = date;
    }

    public CommonResponseVo(Integer status, String msg, T date) {
        this.status = status;
        this.msg = msg;
        this.date = date;
    }


}
