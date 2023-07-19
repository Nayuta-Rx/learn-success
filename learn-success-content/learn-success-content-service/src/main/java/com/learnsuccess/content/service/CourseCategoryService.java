package com.learnsuccess.content.service;

import com.learnsuccess.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程分类树形结构
 * @DataTime: 2023/7/13 16:48
 **/
public interface CourseCategoryService {

    /**
     * 课程分类树形结构查询
     * @param id
     * @return
     */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
