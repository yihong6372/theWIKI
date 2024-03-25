package net.geekh.wiki.service;



import com.baomidou.mybatisplus.extension.service.IService;
import net.geekh.wiki.domain.OpLog;

import java.util.List;

public interface OpLogService extends IService<OpLog> {

    List<OpLog> getLoginLogList();

    List<OpLog> getOpLogList();
}
