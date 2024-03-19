package net.geekh.wiki.controller;


import jakarta.annotation.Resource;
import net.geekh.wiki.service.impl.EbookSnapshotService;
import net.geekh.wiki.vo.CommonResponseVo;
import net.geekh.wiki.vo.StatisticResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    public CommonResponseVo getStatistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.getStatistic();
        CommonResponseVo<List<StatisticResp>> commonResp = new CommonResponseVo<>();
        commonResp.setData(statisticResp);
        return commonResp;
    }

    @GetMapping("/get-30-statistic")
    public CommonResponseVo get30Statistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.get30Statistic();
        CommonResponseVo<List<StatisticResp>> commonResp = new CommonResponseVo<>();
        commonResp.setData(statisticResp);
        return commonResp;
    }

}
