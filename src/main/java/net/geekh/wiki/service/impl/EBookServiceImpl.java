package net.geekh.wiki.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.geekh.wiki.domain.Ebook;
import net.geekh.wiki.domain.EbookExample;
import net.geekh.wiki.form.EbookQueryForm;
import net.geekh.wiki.form.EbookSaveForm;
import net.geekh.wiki.mapper.EbookMapper;
import net.geekh.wiki.service.IEbookService;
import net.geekh.wiki.util.CopyUtil;
import net.geekh.wiki.util.SnowFlake;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.EbookVo;
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
public class EBookServiceImpl implements IEbookService {

    private static final Logger Log = LoggerFactory.getLogger(EBookServiceImpl.class);

    @Autowired
    private EbookMapper ebookMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Override
    public PageVo<EbookVo> list(EbookQueryForm form) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(form.getName())) {
            criteria.andNameLike("%" + form.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(form.getCategory2Id())) {
            criteria.andCategory2IdEqualTo(form.getCategory2Id());
        }
        PageHelper.startPage(form.getPage(), form.getSize());
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);
        List<EbookVo> ebookVos = CopyUtil.copyList(ebooks, EbookVo.class);
        PageInfo pageInfo = new PageInfo<>(ebooks);

        PageVo<EbookVo> pageVo = new PageVo<>();
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setList(ebookVos);

        Log.info("pageInfo{}",pageInfo);
        return pageVo;
    }

    @Override
    public CommonResponseVo save(EbookSaveForm form) {

        Ebook ebook = CopyUtil.copy(form, Ebook.class);
        if (ObjectUtils.isEmpty(form.getId())) {
            ebook.setId(snowFlake.nextId());
            int i = ebookMapper.insertSelective(ebook);
            if (i == 0) {
                return new CommonResponseVo(-1, "新增失败");
                //TODO 服务异常处理
            }
        } else {

            int i = ebookMapper.updateByPrimaryKeySelective(ebook);
            if (i == 0) {
                return new CommonResponseVo(-1, "编辑失败");
                //TODO 服务异常处理
            }
        }
        return new CommonResponseVo(0, "成功");
    }

    @Override
    public CommonResponseVo delete(Long ebookId) {
        int i = ebookMapper.deleteByPrimaryKey(ebookId);
        if (i == 0) {
            return new CommonResponseVo(-1, "删除失败");
        }
        return new CommonResponseVo(0, "删除成功");
    }
}
