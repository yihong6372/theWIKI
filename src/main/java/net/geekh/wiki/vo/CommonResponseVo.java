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

    private T data;


    public CommonResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public CommonResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    public CommonResponseVo(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }


    public CommonResponseVo() {

    }
}
