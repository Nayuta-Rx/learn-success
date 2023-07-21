package com.learnsuccess.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learnsuccess.base.execption.LearnSuccessException;
import com.learnsuccess.content.mapper.CourseBaseMapper;
import com.learnsuccess.content.mapper.CourseTeacherMapper;
import com.learnsuccess.content.model.dto.CourseTeacherDto;
import com.learnsuccess.content.model.po.CourseBase;
import com.learnsuccess.content.model.po.CourseTeacher;
import com.learnsuccess.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 教师信息管理接口实现类
 * @DataTime: 2023/7/21 17:39
 **/
@Slf4j
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Autowired
    private CourseBaseMapper courseBaseMapper;


    @Override
    public List<CourseTeacher> list(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(courseId != null, CourseTeacher::getCourseId, courseId);
        return courseTeacherMapper.selectList(queryWrapper);
    }

    @Override
    public void saveOrUpdateTeacher(Long companyId, CourseTeacherDto dto) {
        validateCompany(companyId, dto.getCourseId());

        if (dto.getId() != null) {
            CourseTeacher courseTeacher = courseTeacherMapper.selectById(dto.getId());
            BeanUtils.copyProperties(dto, courseTeacher);
            courseTeacherMapper.updateById(courseTeacher);
        } else {
            CourseTeacher courseTeacher = new CourseTeacher();
            BeanUtils.copyProperties(dto, courseTeacher);
            courseTeacherMapper.insert(courseTeacher);
        }
    }

    @Override
    public void deleteTeacher(Long teacherId) {
        courseTeacherMapper.deleteById(teacherId);
    }

    private void validateCompany(Long companyId, Long courseId){
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            throw new LearnSuccessException("课程不存在");
        }

        if (!companyId.equals(courseBase.getCompanyId())){
            throw new LearnSuccessException("本机构只能修改本机构的老师");
        }
    }


}

