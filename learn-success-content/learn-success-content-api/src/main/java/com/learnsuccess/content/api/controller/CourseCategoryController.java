package com.learnsuccess.content.api.controller;

import com.learnsuccess.content.model.dto.CourseCategoryTreeDto;
import com.learnsuccess.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 数据字典 前端控制器
 * @DataTime: 2023/7/13 15:54
 **/
@RestController
@Slf4j
public class CourseCategoryController {

    @Autowired
    private CourseCategoryService courseCategoryService;

    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes(){
        return courseCategoryService.queryTreeNodes("1");
    }
}
