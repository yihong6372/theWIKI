package net.geekh.wiki.service;

import lombok.extern.slf4j.Slf4j;
import net.geekh.wiki.WikiApplicationTests;
import net.geekh.wiki.domain.Ebook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:09
 */
@Slf4j
public class EbookServiceTest extends WikiApplicationTests {

    @Autowired
    private IEbookService IEbookService;

    @Test
    void list() {
        List<Ebook> list = IEbookService.list();
        log.info("list==>{}",list);

    }
}