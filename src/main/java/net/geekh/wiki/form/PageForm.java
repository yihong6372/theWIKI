package net.geekh.wiki.form;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/9 13:53
 */
public class PageForm {

    private int page;

    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageForm{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}
