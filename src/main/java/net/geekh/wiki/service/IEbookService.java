package net.geekh.wiki.service;

import net.geekh.wiki.domain.Ebook;
import net.geekh.wiki.form.EbookForm;
import net.geekh.wiki.vo.EbookVo;
import net.geekh.wiki.vo.PageVo;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:03
 */
public interface IEbookService {

    PageVo<EbookVo> list(EbookForm form);
}
