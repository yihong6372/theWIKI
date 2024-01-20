package net.geekh.wiki.mapper;

import java.util.List;
import net.geekh.wiki.domain.Content;
import net.geekh.wiki.domain.ContentExample;
import org.apache.ibatis.annotations.Param;

public interface ContentMapper {
    long countByExample(ContentExample example);

    int deleteByExample(ContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Content row);

    int insertSelective(Content row);

    List<Content> selectByExampleWithBLOBs(ContentExample example);

    List<Content> selectByExample(ContentExample example);

    Content selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Content row, @Param("example") ContentExample example);

    int updateByExampleWithBLOBs(@Param("row") Content row, @Param("example") ContentExample example);

    int updateByExample(@Param("row") Content row, @Param("example") ContentExample example);

    int updateByPrimaryKeySelective(Content row);

    int updateByPrimaryKeyWithBLOBs(Content row);
}