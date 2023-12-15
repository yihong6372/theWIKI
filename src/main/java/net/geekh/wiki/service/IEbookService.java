package net.geekh.wiki.service;

import net.geekh.wiki.form.EbookQueryForm;
import net.geekh.wiki.form.EbookSaveForm;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.EbookVo;
import net.geekh.wiki.vo.PageVo;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:03
 */
public interface IEbookService {

    PageVo<EbookVo> list(EbookQueryForm form);

    CommonResponseVo save(EbookSaveForm form);

    CommonResponseVo delete(Long ebookId);

}
