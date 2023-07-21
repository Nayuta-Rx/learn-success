package com.learnsuccess.content.api.controller;

import com.learnsuccess.base.model.PageParams;
import com.learnsuccess.base.model.PageResult;
import com.learnsuccess.content.model.dto.AddCourseDto;
import com.learnsuccess.content.model.dto.CourseBaseInfoDto;
import com.learnsuccess.content.model.dto.EditCourseDto;
import com.learnsuccess.content.model.dto.QueryCourseParamsDto;
import com.learnsuccess.content.model.po.CourseBase;
import com.learnsuccess.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程信息编辑接口
 * @DataTime: 2023/7/6 15:49
 **/
@Api(value = "课程信息编辑接口", tags = "课程信息编辑接口")
@RestController
public class CourseBaseInfoController {

    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto){
        return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDto);
    }

    @ApiOperation("新增课程基础信息")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated AddCourseDto dto){
        //机构id，由于认证系统没有上线暂时硬编码
        Long companyId = 1232141425L;
        return courseBaseInfoService.createCourseBase(companyId,dto);
    }

    @ApiOperation("根据课程id查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId){
        return courseBaseInfoService.getCourseBaseInfo(courseId);
    }

    @ApiOperation("修改课程基础信息")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated EditCourseDto dto){
        Long companyId = 1232141425L;
        return courseBaseInfoService.updateCourseBase(companyId,dto);
    }

    @ApiOperation("课程教师删除")
    @DeleteMapping("/course/{courseId}")
    public void deleteCourse(@PathVariable Long courseId){
        courseBaseInfoService.deleteCourse(courseId);
    }



}
