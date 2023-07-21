package com.learnsuccess.content.api.controller;

import com.learnsuccess.content.model.dto.CourseTeacherDto;
import com.learnsuccess.content.model.po.CourseTeacher;
import com.learnsuccess.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 教师信息编辑api
 * @DataTime: 2023/7/21 16:51
 **/
@Api(value = "教师编辑接口",tags = "教师编辑接口")
@RestController
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

    private static final Long COMPANY_ID = 1232141425L;

    @ApiOperation("查询课程教师列表")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> list(@PathVariable Long courseId){
        return courseTeacherService.list(courseId);
    }


    @ApiOperation("课程教师创建或修改")
    @PostMapping("/courseTeacher")
    public void saveOrUpdateTeacher(@RequestBody CourseTeacherDto dto){
        courseTeacherService.saveOrUpdateTeacher(COMPANY_ID, dto);
    }


    @ApiOperation("课程教师删除")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteTeacher(@PathVariable Long courseId, @PathVariable Long teacherId){
        courseTeacherService.deleteTeacher(teacherId);
    }

}
