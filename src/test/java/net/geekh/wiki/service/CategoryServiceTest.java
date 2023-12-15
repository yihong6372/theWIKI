package net.geekh.wiki.service;

import lombok.extern.slf4j.Slf4j;
import net.geekh.wiki.WikiApplicationTests;
import net.geekh.wiki.form.CategoryQueryForm;
import net.geekh.wiki.form.CategorySaveForm;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.CategoryVo;
import net.geekh.wiki.vo.PageVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:09
 */
@Slf4j
public class CategoryServiceTest extends WikiApplicationTests {

    @Autowired
    private ICategoryService ICategoryService;

    @Test
    void list() {
        CategoryQueryForm queryForm = new CategoryQueryForm();
        queryForm.setPage(1);
        queryForm.setSize(100);
        PageVo<CategoryVo> list = ICategoryService.list(queryForm);
        log.info("list==>{}",list);

    }

    @Test
    public void save() {
        CategorySaveForm categorySaveForm = new CategorySaveForm();
//        categorySaveForm.setName("aaa");
//        categorySaveForm.setDescription("aaabbbb");
        categorySaveForm.setId(388883596087267328L);
        CommonResponseVo save = ICategoryService.save(categorySaveForm);
        System.out.println(save);
    }

    @Test
    public void deleteTest() {
        CommonResponseVo commonResponseVo = ICategoryService.delete(388876370933059584L);
        log.info("删除测试{}" ,commonResponseVo);
    }
}