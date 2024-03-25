package net.geekh.wiki.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.geekh.wiki.domain.Category;
import net.geekh.wiki.domain.CategoryExample;
import net.geekh.wiki.form.CategoryQueryForm;
import net.geekh.wiki.form.CategorySaveForm;
import net.geekh.wiki.mapper.CategoryMapper;
import net.geekh.wiki.service.ICategoryService;
import net.geekh.wiki.util.CopyUtil;
import net.geekh.wiki.util.SnowFlake;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.CategoryVo;
import net.geekh.wiki.vo.PageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:04
 */

@Service
public class CategoryServiceImpl implements ICategoryService {

    private static final Logger Log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;


    @Override
    public PageVo<CategoryVo> list(CategoryQueryForm form) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(form.getName())) {
            criteria.andNameLike("%" + form.getName() + "%");
        }
//        PageHelper.startPage(form.getPage(), form.getSize());
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);
        List<CategoryVo> categoryVos = CopyUtil.copyList(categorys, CategoryVo.class);
        PageInfo pageInfo = new PageInfo<>(categorys);

        PageVo<CategoryVo> pageVo = new PageVo<>();
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setList(categoryVos);

        return pageVo;
    }

    @Override
    public CommonResponseVo<List<CategoryVo>> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort");
        List<Category> categories = categoryMapper.selectByExample(categoryExample);
        List<CategoryVo> categoryVos = CopyUtil.copyList(categories, CategoryVo.class);
        return new CommonResponseVo<List<CategoryVo>>(0, categoryVos);
    }

    @Override
    public CommonResponseVo save(CategorySaveForm form) {

        Category category = CopyUtil.copy(form, Category.class);
        if (ObjectUtils.isEmpty(form.getId())) {
            category.setId(snowFlake.nextId());
            int i = categoryMapper.insertSelective(category);
            if (i == 0) {
                return new CommonResponseVo(-1, "新增失败");
                //TODO 服务异常处理
            }
        } else {

            int i = categoryMapper.updateByPrimaryKeySelective(category);
            if (i == 0) {
                return new CommonResponseVo(-1, "编辑失败");
                //TODO 服务异常处理
            }
        }
        return new CommonResponseVo(0, "成功");
    }

    @Override
    public CommonResponseVo delete(Long categoryId) {
        int i = categoryMapper.deleteByPrimaryKey(categoryId);
        if (i == 0) {
            return new CommonResponseVo(-1, "删除失败");
        }
        return new CommonResponseVo(0, "删除成功");
    }
}
