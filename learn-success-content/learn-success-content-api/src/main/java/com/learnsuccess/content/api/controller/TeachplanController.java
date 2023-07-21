package com.learnsuccess.content.api.controller;

import com.learnsuccess.base.common.MoveEnum;
import com.learnsuccess.content.model.dto.SaveTeachplanDto;
import com.learnsuccess.content.model.dto.TeachplanDto;
import com.learnsuccess.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程计划编辑接口
 * @DataTime: 2023/7/6 15:49
 **/
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
@RestController
public class TeachplanController {

    @Autowired
    TeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        return teachplanService.findTeachplanTree(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplan){
        teachplanService.saveTeachplan(teachplan);
    }

    @ApiOperation("课程计划删除")
    @DeleteMapping("/teachplan/{planId}")
    public void deleteTeachplan(@PathVariable Long planId){
        teachplanService.deleteTeachplan(planId);
    }

    @ApiOperation("向下移动课程计划")
    @PostMapping("teachplan/movedown/{planId}")
    public void moveDown(@PathVariable Long planId){
        teachplanService.move(planId, MoveEnum.DOWN);
    }

    @ApiOperation("向上移动课程计划")
    @PostMapping("teachplan/moveup/{planId}")
    public void moveUp(@PathVariable Long planId){
        teachplanService.move(planId, MoveEnum.UP);
    }

}
