package net.geekh.wiki.form;

import lombok.Data;

public class EbookForm extends PageForm{
    private Long id;

    private String name;


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
        return "EbookForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}