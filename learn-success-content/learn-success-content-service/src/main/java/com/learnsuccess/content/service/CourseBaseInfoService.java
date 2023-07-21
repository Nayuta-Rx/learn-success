package com.learnsuccess.content.service;

import com.learnsuccess.base.model.PageParams;
import com.learnsuccess.base.model.PageResult;
import com.learnsuccess.content.model.dto.AddCourseDto;
import com.learnsuccess.content.model.dto.CourseBaseInfoDto;
import com.learnsuccess.content.model.dto.EditCourseDto;
import com.learnsuccess.content.model.dto.QueryCourseParamsDto;
import com.learnsuccess.content.model.po.CourseBase;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程基本信息管理业务接口
 * @DataTime: 2023/7/8 15:56
 **/
public interface CourseBaseInfoService {

    /**
     * 课程查询接口
     * @param pageParams
     * @param queryCourseParamsDto
     * @return PageResult<CourseBase>
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * @description 添加课程基本信息
     * @param companyId  教学机构id
     * @param addCourseDto  课程基本信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
     * @description 修改课程信息
     * @param companyId  机构id
     * @param dto  课程信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     */
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);

    /**
     * 根据id查询课程基本信息
     * @param courseId
     * @return
     */
    CourseBaseInfoDto getCourseBaseInfo(long courseId);

    /**
     * 根据Id删除整个课程(教师,课程计划,课程基本信息)
     * @param courseId
     */
    void deleteCourse(Long courseId);
}
