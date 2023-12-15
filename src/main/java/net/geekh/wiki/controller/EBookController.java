package net.geekh.wiki.controller;

import net.geekh.wiki.form.EbookQueryForm;
import net.geekh.wiki.form.EbookSaveForm;
import net.geekh.wiki.service.IEbookService;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.EbookVo;
import net.geekh.wiki.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:15
 */
@RestController
@RequestMapping("/ebook")
public class EBookController {

    @Autowired
    private IEbookService ebookService;

    @GetMapping("/list")
    public CommonResponseVo<PageVo<EbookVo>> list(EbookQueryForm form) {

        PageVo<EbookVo> pageVo = ebookService.list(form);
        return new CommonResponseVo<PageVo<EbookVo>>(0,pageVo);
    }

    @PostMapping("/save")
    public  CommonResponseVo save(@RequestBody EbookSaveForm saveForm){
        CommonResponseVo save = ebookService.save(saveForm);
        return save;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponseVo delete(@PathVariable Long id){
        return ebookService.delete(id);

    }

}
