package net.geekh.wiki.controller;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import net.geekh.wiki.domain.Ebook;
import net.geekh.wiki.service.IEbookService;
import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public CommonResponseVo<List<Ebook>> list() {

        List<Ebook> list = ebookService.list();
        return new CommonResponseVo<List<Ebook>>(0,list);
    }
}
