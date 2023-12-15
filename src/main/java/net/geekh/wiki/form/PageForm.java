package net.geekh.wiki.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/9 13:53
 */
public class PageForm {

    @NotNull
    private Integer page;

    @NotNull
    @Max(value = 1000, message = "最大1000")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
