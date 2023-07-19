package com.learnsuccess.content.service;

import com.learnsuccess.base.model.PageParams;
import com.learnsuccess.base.model.PageResult;
import com.learnsuccess.content.model.dto.AddCourseDto;
import com.learnsuccess.content.model.dto.CourseBaseInfoDto;
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
     * @author Mr.M
     * @date 2022/9/7 17:51
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

}
