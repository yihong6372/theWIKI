package net.geekh.wiki.controller;


import net.geekh.wiki.domain.ErrorLog;
import net.geekh.wiki.service.ErrorLogService;
import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/25 19:47
 */
@RestController
@RequestMapping("/errorLog")
public class ErrorLogController {

    @Autowired
    private ErrorLogService errorLogService;

    @GetMapping("/list")
    public CommonResponseVo list() {
        List<ErrorLog> list = errorLogService.list();
        return new CommonResponseVo<>(200, list);
    }

}
