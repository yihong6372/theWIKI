package net.geekh.wiki.service;



import com.baomidou.mybatisplus.extension.service.IService;
import net.geekh.wiki.domain.ErrorLog;

public interface ErrorLogService extends IService<ErrorLog> {

     void saveLog(Exception e);

     void saveLog(Exception e, String msg);
}
