package net.geekh.wiki.controller;

import jakarta.validation.Valid;
import net.geekh.wiki.form.DocQueryForm;
import net.geekh.wiki.form.DocSaveForm;
import net.geekh.wiki.service.IDocService;
import net.geekh.wiki.vo.CategoryVo;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.DocVo;
import net.geekh.wiki.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2023/12/8 17:15
 */
@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private IDocService docService;

    @GetMapping("/all")
    public CommonResponseVo<List<DocVo>> all() {
        return docService.all();
    }

    @GetMapping("/list")
    public CommonResponseVo<PageVo<DocVo>> list(@Valid DocQueryForm form) {

        PageVo<DocVo> pageVo = docService.list(form);
        return new CommonResponseVo<PageVo<DocVo>>(0,pageVo);
    }

    @PostMapping("/save")
    public  CommonResponseVo save(@RequestBody DocSaveForm saveForm){
        CommonResponseVo save = docService.save(saveForm);
        return save;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResponseVo delete(@PathVariable String idsStr){
        System.out.println("ids,,"+ idsStr);
        List<String> list = Arrays.asList(idsStr.split(","));
        System.out.println("list,,"+ list);

        return docService.delete(list);

    }

}
