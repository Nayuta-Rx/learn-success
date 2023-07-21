package com.learnsuccess.content.model.dto;

import lombok.Data;

/**
 * @Author: Nayuta-Rx
 * @Description: 保存课程计划dto，包括新增、修改
 * @DataTime: 2023/7/20 21:53
 **/
@Data
public class SaveTeachplanDto {


    /***
     * 教学计划id
     */
    private Long id;

    /**
     * 课程计划名称
     */
    private String pname;

    /**
     * 课程计划父级Id
     */
    private Long parentid;

    /**
     * 层级，分为1、2、3级
     */
    private Integer grade;

    /**
     * 课程类型:1视频、2文档
     */
    private String mediaType;


    /**
     * 课程标识
     */
    private Long courseId;

    /**
     * 课程发布标识
     */
    private Long coursePubId;


    /**
     * 是否支持试学或预览（试看）
     */
    private String isPreview;

}
