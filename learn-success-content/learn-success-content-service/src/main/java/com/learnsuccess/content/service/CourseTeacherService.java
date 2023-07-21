package com.learnsuccess.content.service;

import com.learnsuccess.content.model.dto.CourseTeacherDto;
import com.learnsuccess.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 教师信息管理接口
 * @DataTime: 2023/7/21 17:37
 **/
public interface CourseTeacherService {

    /**
     * 获取教师列表
     * @param courseId
     * @return List<CourseTeacher>
     */
    List<CourseTeacher> list(Long courseId);

    /**
     * 新增或保存教师
     * @param companyId
     * @param dto
     */
    void saveOrUpdateTeacher(Long companyId, CourseTeacherDto dto);

    /**
     * 根据id删除教师哦
     * @param teacherId
     */
    void deleteTeacher(Long teacherId);
}
