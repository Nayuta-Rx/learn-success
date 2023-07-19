package com.learnsuccess.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learnsuccess.base.execption.LearnSuccessException;
import com.learnsuccess.base.model.PageParams;
import com.learnsuccess.base.model.PageResult;
import com.learnsuccess.content.mapper.CourseBaseMapper;
import com.learnsuccess.content.mapper.CourseCategoryMapper;
import com.learnsuccess.content.mapper.CourseMarketMapper;
import com.learnsuccess.content.model.dto.AddCourseDto;
import com.learnsuccess.content.model.dto.CourseBaseInfoDto;
import com.learnsuccess.content.model.dto.QueryCourseParamsDto;
import com.learnsuccess.content.model.po.CourseBase;
import com.learnsuccess.content.model.po.CourseCategory;
import com.learnsuccess.content.model.po.CourseMarket;
import com.learnsuccess.content.service.CourseBaseInfoService;
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

    //根据课程id查询课程基本信息，包括基本信息和营销信息
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

}
