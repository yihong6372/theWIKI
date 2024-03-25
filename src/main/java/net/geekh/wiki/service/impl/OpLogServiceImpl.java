package net.geekh.wiki.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.geekh.wiki.domain.OpLog;
import net.geekh.wiki.mapper.OpLogMapper;
import net.geekh.wiki.service.OpLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpLogServiceImpl extends ServiceImpl<OpLogMapper, OpLog> implements OpLogService {


    @Override
    public List<OpLog> getLoginLogList() {
        QueryWrapper<OpLog> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("re_url", "/user/login");
        queryWrapper.orderBy(true, false, "re_time");
        queryWrapper.last("limit 1000");
        return this.getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public List<OpLog> getOpLogList() {
        return this.lambdaQuery()
                .orderBy(true, false, OpLog::getReTime)
                .last("limit 1000")
                .list();
    }
}
