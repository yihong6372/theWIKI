package net.geekh.wiki.service.impl;

import net.geekh.wiki.domain.Ebook;
import net.geekh.wiki.mapper.EbookMapper;
import net.geekh.wiki.service.IEbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Ebook> list() {
        List<Ebook> ebooks = ebookMapper.selectByExample(null);
        return ebooks;
    }
}
