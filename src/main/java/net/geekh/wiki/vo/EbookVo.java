package net.geekh.wiki.vo;

import lombok.Data;

@Data
public class EbookVo {
    private Long id;

    private String name;

    private Long category1Id;

    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;

}