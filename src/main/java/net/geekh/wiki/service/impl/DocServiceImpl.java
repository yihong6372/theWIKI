package net.geekh.wiki.service.impl;

import com.github.pagehelper.PageInfo;
import net.geekh.wiki.domain.*;
import net.geekh.wiki.form.DocQueryForm;
import net.geekh.wiki.form.DocSaveForm;
import net.geekh.wiki.mapper.ContentMapper;
import net.geekh.wiki.mapper.DocMapper;
import net.geekh.wiki.mapper.DocMapperCust;
import net.geekh.wiki.service.IDocService;
import net.geekh.wiki.util.CopyUtil;
import net.geekh.wiki.util.SnowFlake;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.DocVo;
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
public class DocServiceImpl implements IDocService {

    private static final Logger Log = LoggerFactory.getLogger(DocServiceImpl.class);

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private DocMapperCust docMapperCust;


    @Override
    public CommonResponseVo<List<DocVo>> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort");
        List<Doc> Docs = docMapper.selectByExample(docExample);
        List<DocVo> DocVos = CopyUtil.copyList(Docs, DocVo.class);
        return new CommonResponseVo(0, DocVos);
    }

    @Override
    public PageVo<DocVo> list(DocQueryForm form) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(form.getName())) {
            criteria.andNameLike("%" + form.getName() + "%");
        }
//        PageHelper.startPage(form.getPage(), form.getSize());
        List<Doc> docs = docMapper.selectByExample(docExample);
        List<DocVo> docVos = CopyUtil.copyList(docs, DocVo.class);
        PageInfo pageInfo = new PageInfo<>(docs);

        PageVo<DocVo> pageVo = new PageVo<>();
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setList(docVos);

        Log.info("pageInfo{}",pageInfo);
        return pageVo;
    }

    @Override
    public CommonResponseVo save(DocSaveForm form) {

        Doc doc = CopyUtil.copy(form, Doc.class);
        Content content = CopyUtil.copy(form, Content.class);
        if (ObjectUtils.isEmpty(form.getId())) {
            doc.setId(snowFlake.nextId());
            content.setId(doc.getId());
            int i = docMapper.insertSelective(doc);
            int j = contentMapper.insert(content);
            if (i == 0) {
                return new CommonResponseVo(-1, "新增失败");
                //TODO 服务异常处理
            }
        } else {

            int i = docMapper.updateByPrimaryKeySelective(doc);
            int j = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (i == 0 ) {
                return new CommonResponseVo(-1, "编辑失败");
                //TODO 服务异常处理
            }
            if (j ==0 ) {
                content.setId(doc.getId());
                contentMapper.insert(content);
            }
        }
        return new CommonResponseVo(0, "成功");
    }

    @Override
    public CommonResponseVo delete(Long docId) {
        int i = docMapper.deleteByPrimaryKey(docId);
        if (i == 0) {
            return new CommonResponseVo(-1, "删除失败");
        }
        return new CommonResponseVo(0, "删除成功");
    }


    @Override
    public CommonResponseVo delete(List<String> idsStr) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(idsStr);
        int i = docMapper.deleteByExample(docExample);
        if (i == 0) {
            return new CommonResponseVo(-1, "删除失败");
        }
        return new CommonResponseVo(0, "删除成功");
    }

    @Override
    public CommonResponseVo findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        if (ObjectUtils.isEmpty(content)) {
            return new CommonResponseVo(0, "");
        } else {
            docMapperCust.increaseViewCount(id);
            return new CommonResponseVo(0, content);
        }
    }

    @Override
    public CommonResponseVo vote(Long id) {
        docMapperCust.increaseVoteCount(id);
        return new CommonResponseVo<>(0, "点赞成功");
    }

    @Override
    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }
}
