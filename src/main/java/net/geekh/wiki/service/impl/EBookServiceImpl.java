package net.geekh.wiki.service.impl;

import com.github.pagehelper.PageHelper;
import net.geekh.wiki.domain.Ebook;
import net.geekh.wiki.domain.EbookExample;
import net.geekh.wiki.form.EbookForm;
import net.geekh.wiki.mapper.EbookMapper;
import net.geekh.wiki.service.IEbookService;
import net.geekh.wiki.util.CopyUtil;
import net.geekh.wiki.vo.EbookVo;
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


    @Autowired
    private EbookMapper ebookMapper;

    @Override
    public List<EbookVo> list(EbookForm form) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(form)&&!ObjectUtils.isEmpty(form.getName())) {
            criteria.andNameLike("%" + form.getName() + "%");
        }
        PageHelper.startPage(1, 2);
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);
        return CopyUtil.copyList(ebooks, EbookVo.class);
    }
}
