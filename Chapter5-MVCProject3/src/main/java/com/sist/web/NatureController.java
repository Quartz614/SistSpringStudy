package com.sist.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.dao.*;
import com.sist.vo.*;

@Controller
public class NatureController {
 // DAO의 주소값 얻기 
    @Autowired
    private NatureDAO dao;

    //요청에 대한 처리 
    @GetMapping("nature/list.do")
    public String nature_list(String page, Model model) {
        if (page == null)
            page = "1";
        int curpage = Integer.parseInt(page);
        Map map = new HashMap();
        int rowSize = 20;
        int start = (rowSize * curpage) - (rowSize - 1);
        int end = rowSize * curpage;
        map.put("start", start);
        map.put("end", end);
        List<NatureVO> list = dao.natureListData(map);
        int totalpage = dao.natureTotalPage();

        final int BLOCK = 5;
        int startPage = ((curpage - 1) / BLOCK * BLOCK) + 1;
        int endPage = ((curpage - 1) / BLOCK * BLOCK) + BLOCK;
        if (endPage > totalpage) {
            endPage = totalpage;
        }

        model.addAttribute("list", list);
        model.addAttribute("curpage", curpage);
        model.addAttribute("totalpage", totalpage);
        model.addAttribute("BLOCK", BLOCK);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "nature/list";
    }

    @GetMapping("nature/detail.do")
    public String nature_detail(int no, Model model) {
        NatureVO vo = dao.natureDetailData(no);
        model.addAttribute("vo", vo);
        return "nature/detail";
    }
}