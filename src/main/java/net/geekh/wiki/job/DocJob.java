package net.geekh.wiki.job;

import lombok.extern.slf4j.Slf4j;
import net.geekh.wiki.mapper.DocMapperCust;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/1/28 21:35
 */
@Component
@Slf4j
public class DocJob {

    @Autowired
    private DocMapperCust docMapperCust;

    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        log.info("开始更新电子书数据！");
        long start = System.currentTimeMillis();
        docMapperCust.updateEbookInfo();
        log.info("更新电子书数据完成，耗时{}毫秒",System.currentTimeMillis() - start);
    }
}
