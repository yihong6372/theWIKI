package net.geekh.wiki.mapper;

import java.util.List;
import net.geekh.wiki.domain.Demo;
import net.geekh.wiki.domain.DemoExample;
import org.apache.ibatis.annotations.Param;

public interface DemoMapper {
    long countByExample(DemoExample example);

    int deleteByExample(DemoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Demo row);

    int insertSelective(Demo row);

    List<Demo> selectByExample(DemoExample example);

    Demo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Demo row, @Param("example") DemoExample example);

    int updateByExample(@Param("row") Demo row, @Param("example") DemoExample example);

    int updateByPrimaryKeySelective(Demo row);

    int updateByPrimaryKey(Demo row);
}