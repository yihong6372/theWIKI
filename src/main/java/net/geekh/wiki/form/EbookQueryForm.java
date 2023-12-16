package net.geekh.wiki.form;

import lombok.Data;

public class EbookQueryForm extends PageForm{


    private Long id;

    private Long category2Id;

    private String name;

    public Long getCategory2Id() {
        return category2Id;
    }

    public void setCategory2Id(Long category2Id) {
        this.category2Id = category2Id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EbookQueryForm{" +
                "id=" + id +
                ", category2Id=" + category2Id +
                ", name='" + name + '\'' +
                '}';
    }
}