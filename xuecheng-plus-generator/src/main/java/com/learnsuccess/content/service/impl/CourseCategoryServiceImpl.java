package com.learnsuccess.content.service.impl;

import com.learnsuccess.content.model.po.CourseCategory;
import com.learnsuccess.content.mapper.CourseCategoryMapper;
import com.learnsuccess.content.service.CourseCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

}
