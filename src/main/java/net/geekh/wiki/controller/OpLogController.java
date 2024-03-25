package net.geekh.wiki.controller;


import net.geekh.wiki.domain.OpLog;
import net.geekh.wiki.service.OpLogService;
import net.geekh.wiki.vo.CommonResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author YIHONG
 * @Description
 * @Date 2024/3/25 20:33
 */
@RestController
@RequestMapping("/opLog")
public class OpLogController {


    @Autowired
    private OpLogService opLogService;

    @RequestMapping()
    public CommonResponseVo getLogList() {
        List<OpLog> opLogList = opLogService.getOpLogList();
        return new CommonResponseVo(200, opLogList);
    }

    @RequestMapping("/login")
    public CommonResponseVo getLoginLogList() {
        List<OpLog> loginLogList = opLogService.getLoginLogList();
        return new CommonResponseVo<>(200, loginLogList);
    }
}
