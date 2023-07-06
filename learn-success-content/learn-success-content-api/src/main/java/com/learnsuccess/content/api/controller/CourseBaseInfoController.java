package com.learnsuccess.content.api.controller;

import com.learnsuccess.base.model.PageParams;
import com.learnsuccess.base.model.PageResult;
import com.learnsuccess.content.model.dto.QueryCourseParamsDto;
import com.learnsuccess.content.model.po.CourseBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程信息编辑接口
 * @DataTime: 2023/7/6 15:49
 **/
@Api(value = "课程信息编辑接口", tags = "课程信息编辑接口")
@RestController
public class CourseBaseInfoController {

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams,
                                       @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto){
        CourseBase courseBase = new CourseBase();
        courseBase.setName("测试名称");
        courseBase.setCreateDate(LocalDateTime.now());
        List<CourseBase> courseBases = new ArrayList();
        courseBases.add(courseBase);
        PageResult<CourseBase> pageResult = new PageResult(courseBases,10,1,10);
        return pageResult;
    }

}
