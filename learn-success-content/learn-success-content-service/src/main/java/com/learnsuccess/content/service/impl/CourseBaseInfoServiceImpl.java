package com.learnsuccess.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learnsuccess.base.execption.LearnSuccessException;
import com.learnsuccess.base.model.PageParams;
import com.learnsuccess.base.model.PageResult;
import com.learnsuccess.content.mapper.*;
import com.learnsuccess.content.model.dto.AddCourseDto;
import com.learnsuccess.content.model.dto.CourseBaseInfoDto;
import com.learnsuccess.content.model.dto.EditCourseDto;
import com.learnsuccess.content.model.dto.QueryCourseParamsDto;
import com.learnsuccess.content.model.po.*;
import com.learnsuccess.content.service.CourseBaseInfoService;
import com.learnsuccess.content.service.CourseTeacherService;
import com.learnsuccess.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程基本信息管理业务实现类
 * @DataTime: 2023/7/8 16:09
 **/
@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;
   
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(queryCourseParamsDto.getCourseName()), CourseBase::getName, queryCourseParamsDto.getCourseName());
        wrapper.eq(StringUtils.isNotBlank(queryCourseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, queryCourseParamsDto.getAuditStatus());
        wrapper.eq(StringUtils.isNotBlank(queryCourseParamsDto.getPublishStatus()), CourseBase::getStatus, queryCourseParamsDto.getPublishStatus());

        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<CourseBase> courseBasePage = courseBaseMapper.selectPage(page, wrapper);

        PageResult<CourseBase> result = new PageResult<>(courseBasePage.getRecords(), courseBasePage.getTotal(), pageParams.getPageNo(), pageParams.getPageSize());
        return result;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {
        log.info("创建新课程[{}]", dto);
        //保存课程基本信息
        Long courseId = saveCourseBase(companyId, dto);
        //保存课程营销信息
        saveCourseMarket(courseId, dto);
        //查询课程基本信息及营销信息并返回
        return getCourseBaseInfo(courseId);
    }

    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto) {
        log.info("更新课程[{}]", dto);
        // 更新课程
        updateCourse(companyId, dto);
        //封装营销信息的数据
        saveCourseMarket(dto.getId(), dto);
        //查询课程信息
        return getCourseBaseInfo(dto.getId());
    }

    //根据课程id查询课程基本信息，包括基本信息和营销信息
    @Override
    public CourseBaseInfoDto getCourseBaseInfo(long courseId){
        log.info("查询课程基本信息[{}]", courseId);
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket != null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }
        //查询分类名称
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());
        return courseBaseInfoDto;
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null){
            return;
        }
        if (!"202002".equals(courseBase.getAuditStatus())) {
            throw new LearnSuccessException("课程未提交才可删除");
        }
        // 删除教师
        courseTeacherMapper.delete(getWrapper("course_Id", courseId, new CourseTeacher()));
        // 删除媒体资源
        teachplanMediaMapper.delete(getWrapper("course_Id", courseId, new TeachplanMedia()));
        // 删除课程计划
        teachplanMapper.delete(getWrapper("course_Id", courseId, new Teachplan()));
        // 删除课程销售信息
        courseMarketMapper.deleteById(courseId);
        // 删除课程基本信息
        courseBaseMapper.deleteById(courseId);
    }

    private <T> QueryWrapper<T> getWrapper(String cloumn, Long eqId, T t){
        QueryWrapper<T> result = new QueryWrapper<>();
        result.eq(cloumn, eqId);
        return result;
    }


    //更新课程基本信息
    private void updateCourse(Long companyId, EditCourseDto dto) {
        CourseBase courseBase = courseBaseMapper.selectById(dto.getId());
        if(courseBase==null){
            LearnSuccessException.cast("课程不存在");
        }
        if(!courseBase.getCompanyId().equals(companyId)){
            LearnSuccessException.cast("本机构只能修改本机构的课程");
        }
        BeanUtils.copyProperties(dto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());
        log.info("更新课程基本信息[{}]", courseBase);
        //更新课程基本信息
        if (courseBaseMapper.updateById(courseBase) <= 0) {
            throw new LearnSuccessException("更新课程基本信息失败");
        }
    }

    //保存课程基本信息
    private Long saveCourseBase(Long companyId, AddCourseDto dto){
        CourseBase courseBaseNew = new CourseBase();
        BeanUtils.copyProperties(dto, courseBaseNew);
        courseBaseNew.setAuditStatus("202002");
        courseBaseNew.setStatus("203001");
        courseBaseNew.setCompanyId(companyId);
        courseBaseNew.setCreateDate(LocalDateTime.now());
        log.info("保存课程基本信息[{}]", courseBaseNew);
        //插入课程基本信息
        if (courseBaseMapper.insert(courseBaseNew) <= 0) {
            throw new LearnSuccessException("新增课程基本信息失败");
        }
        return courseBaseNew.getId();
    }

    //保存课程营销信息
    private void saveCourseMarket(Long courseId, AddCourseDto dto){
        CourseMarket courseMarketNew = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarketNew);
        courseMarketNew.setId(courseId);
        //收费规则为收费校验
        if("201001".equals(courseMarketNew.getCharge())){
            if(courseMarketNew.getPrice() == null || courseMarketNew.getPrice().floatValue()<=0){
                throw new LearnSuccessException("课程为收费价格不能为空且必须大于0");
            }
        }
        log.info("保存课程营销信息[{}]", courseMarketNew);
        //根据id从课程营销表查询
        CourseMarket courseMarketObj = courseMarketMapper.selectById(courseId);
        if(courseMarketObj == null){
            if (courseMarketMapper.insert(courseMarketNew) <= 0) {
                throw new LearnSuccessException("新增课程基本信息失败");
            }
        }else{
            BeanUtils.copyProperties(courseMarketNew,courseMarketObj);
            courseMarketObj.setId(courseMarketNew.getId());
            if (courseMarketMapper.updateById(courseMarketObj) <= 0) {
                throw new LearnSuccessException("新增课程基本信息失败");
            }
        }
    }

}
