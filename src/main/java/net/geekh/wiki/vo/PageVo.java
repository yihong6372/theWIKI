package net.geekh.wiki.vo;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/9 14:05
 */
public class PageVo<T> {

    private Long total;

    private List<T> list;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageVo{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
