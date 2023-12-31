package net.geekh.wiki.controller;

import jakarta.validation.Valid;
import net.geekh.wiki.form.CategoryQueryForm;
import net.geekh.wiki.form.CategorySaveForm;
import net.geekh.wiki.service.ICategoryService;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.CategoryVo;
import net.geekh.wiki.vo.PageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:15
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping("/all")
    public CommonResponseVo<List<CategoryVo>> all() {
        return categoryService.all();
    }
    @GetMapping("/list")
    public CommonResponseVo<PageVo<CategoryVo>> list(@Valid CategoryQueryForm form) {

        PageVo<CategoryVo> pageVo = categoryService.list(form);
        return new CommonResponseVo<PageVo<CategoryVo>>(0,pageVo);
    }

    @PostMapping("/save")
    public  CommonResponseVo save(@RequestBody CategorySaveForm saveForm){
        return categoryService.save(saveForm);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponseVo delete(@PathVariable Long id){
        return categoryService.delete(id);
    }

}
