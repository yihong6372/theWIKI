package net.geekh.wiki.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 14:45
 */
@Mapper
public interface DocMapperCust {

    void increaseVoteCount(Long id);

    void increaseViewCount(Long id);

    void updateEbookInfo();
}
