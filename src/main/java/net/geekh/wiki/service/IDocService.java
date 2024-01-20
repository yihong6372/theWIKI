package net.geekh.wiki.service;

import net.geekh.wiki.form.DocQueryForm;
import net.geekh.wiki.form.DocSaveForm;
import net.geekh.wiki.vo.CategoryVo;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.DocVo;
import net.geekh.wiki.vo.PageVo;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:03
 */
public interface IDocService {

    PageVo<DocVo> list(DocQueryForm form);

    CommonResponseVo<List<DocVo>> all(Long ebookId);

    CommonResponseVo save(DocSaveForm form);

    CommonResponseVo delete(Long docId);

    CommonResponseVo delete(List<String> idsStr);

    CommonResponseVo findContent(Long id);

}
