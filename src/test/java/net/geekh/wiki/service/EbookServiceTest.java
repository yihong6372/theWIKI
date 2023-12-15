package net.geekh.wiki.service;

import lombok.extern.slf4j.Slf4j;
import net.geekh.wiki.WikiApplicationTests;
import net.geekh.wiki.domain.Ebook;
import net.geekh.wiki.form.EbookQueryForm;
import net.geekh.wiki.form.EbookSaveForm;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.EbookVo;
import net.geekh.wiki.vo.PageVo;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.logging.Logger;

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
        EbookQueryForm queryForm = new EbookQueryForm();
        queryForm.setPage(1);
        queryForm.setSize(100);
        PageVo<EbookVo> list = IEbookService.list(queryForm);
        log.info("list==>{}",list);

    }

    @Test
    public void save() {
        EbookSaveForm ebookSaveForm = new EbookSaveForm();
//        ebookSaveForm.setName("aaa");
        ebookSaveForm.setDescription("aaabbbb");
        ebookSaveForm.setId(388883596087267328L);
        CommonResponseVo save = IEbookService.save(ebookSaveForm);
        System.out.println(save);
    }

    @Test
    public void deleteTest() {
        CommonResponseVo commonResponseVo = IEbookService.delete(388876370933059584L);
        log.info("删除测试{}" ,commonResponseVo);
    }
}