package com.learnsuccess.content.model.dto;

import com.learnsuccess.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程分类树型结点dto
 * @DataTime: 2023/7/13 15:52
 **/
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {
    /**
     * 叶子节点
     */
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
