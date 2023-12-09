package net.geekh.wiki.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.geekh.wiki.domain.Ebook;
import net.geekh.wiki.domain.EbookExample;
import net.geekh.wiki.form.EbookForm;
import net.geekh.wiki.mapper.EbookMapper;
import net.geekh.wiki.service.IEbookService;
import net.geekh.wiki.util.CopyUtil;
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

    @Override
    public PageVo<EbookVo> list(EbookForm form) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(form.getName())) {
            criteria.andNameLike("%" + form.getName() + "%");
        }
        PageHelper.startPage(form.getPage(), form.getSize());
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);
        List<EbookVo> ebookVos = CopyUtil.copyList(ebooks, EbookVo.class);
        PageInfo pageInfo = new PageInfo<>(ebooks);

        PageVo<EbookVo> pageVo = new PageVo<>();
        pageVo.setTotal(pageInfo.getTotal());

        Log.info("asfds{}",pageInfo);
        return pageVo;
    }
}
