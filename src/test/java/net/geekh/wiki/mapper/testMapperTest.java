package net.geekh.wiki.mapper;


import net.geekh.wiki.WikiApplicationTests;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 15:06
 */
public class testMapperTest extends WikiApplicationTests {


    @Autowired
    private testMapper testMapper;

    private static final Logger log = LoggerFactory.getLogger(testMapperTest.class);

    @Test
    public void list() {
        net.geekh.wiki.domain.Test list = testMapper.list(1);

        log.info("safsf===={}",list);

    }
}