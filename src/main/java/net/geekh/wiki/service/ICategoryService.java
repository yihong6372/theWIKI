package net.geekh.wiki.service;

import net.geekh.wiki.form.CategoryQueryForm;
import net.geekh.wiki.form.CategorySaveForm;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.CategoryVo;
import net.geekh.wiki.vo.PageVo;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:03
 */
public interface ICategoryService {

    PageVo<CategoryVo> list(CategoryQueryForm form);

    CommonResponseVo save(CategorySaveForm form);

    CommonResponseVo delete(Long categoryId);

}
