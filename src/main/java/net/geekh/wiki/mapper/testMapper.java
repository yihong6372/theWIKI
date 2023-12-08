package net.geekh.wiki.mapper;

import net.geekh.wiki.domain.Test;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.Mapping;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 14:45
 */
@Mapper
public interface testMapper {

    Test list(Integer id);
}
