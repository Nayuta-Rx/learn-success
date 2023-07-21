package com.learnsuccess.content.service;

import com.learnsuccess.base.common.MoveEnum;
import com.learnsuccess.content.model.dto.SaveTeachplanDto;
import com.learnsuccess.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程基本信息管理业务接口
 * @DataTime: 2023/7/20 17:43
 **/
public interface TeachplanService {

    /**
     * @description 查询课程计划树型结构
     * @param courseId  课程id
     * @return List<TeachplanDto>
     */
    List<TeachplanDto> findTeachplanTree(long courseId);


    /**
     * 只在课程计划
     * @param teachplanDto
     */
    void saveTeachplan(SaveTeachplanDto teachplanDto);

    /**
     * 删除课程计划
     * @param planId
     */
    void deleteTeachplan(Long planId);

    /**
     * 移动课程计划
     * @param planId
     * @param moveEnum
     */
    void move(Long planId, MoveEnum moveEnum);
}
