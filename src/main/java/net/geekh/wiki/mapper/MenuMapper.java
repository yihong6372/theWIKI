package net.geekh.wiki.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.geekh.wiki.domain.Menu;


import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/15 09:02
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);
}
