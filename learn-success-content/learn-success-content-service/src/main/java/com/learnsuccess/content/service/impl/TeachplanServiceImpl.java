package com.learnsuccess.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.learnsuccess.base.common.MoveEnum;
import com.learnsuccess.base.execption.LearnSuccessException;
import com.learnsuccess.content.mapper.TeachplanMapper;
import com.learnsuccess.content.mapper.TeachplanMediaMapper;
import com.learnsuccess.content.model.dto.SaveTeachplanDto;
import com.learnsuccess.content.model.dto.TeachplanDto;
import com.learnsuccess.content.model.po.Teachplan;
import com.learnsuccess.content.model.po.TeachplanMedia;
import com.learnsuccess.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @Author: Nayuta-Rx
 * @Description: 课程计划service接口实现类
 * @DataTime: 2023/7/20 17:43
 **/
@Slf4j
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachplanDtos;
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        log.info("保存课程计划, teachplanDto = [{}]", teachplanDto);
        Long id = teachplanDto.getId();
        //修改课程计划
        if(id!=null){
            Teachplan teachplan = this.selectTeachPlanById(id);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }else{
            //取出同父同级别的课程计划数量
            int count = getTeachPlanOrder(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachPlanNew = new Teachplan();
            teachPlanNew.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachPlanNew);
            teachplanMapper.insert(teachPlanNew);
        }
    }

    @Override
    @Transactional
    public void deleteTeachplan(Long planId) {
        log.info("删除课程计划, planId = [{}]", planId);
        Teachplan teachplan = this.selectTeachPlanById(planId);
        // 删除课程计划
        if (1 == teachplan.getGrade()) {
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(planId != null, Teachplan::getParentid, planId);
            List<Teachplan> list = teachplanMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(list)) {
                throw new LearnSuccessException("课程计划信息还有子级信息，无法操作");
            }
            teachplanMapper.deleteById(planId);
        }else{
            LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(planId != null, TeachplanMedia::getTeachplanId, planId);
            // 先删除关联的媒体文件,再删除课程计划
            teachplanMediaMapper.delete(queryWrapper);
            teachplanMapper.deleteById(planId);
        }
    }

    @Override
    @Transactional
    public void move(Long planId, MoveEnum moveEnum) {
        log.info("移动课程计划, planId = [{}]", planId);
        // 将指定集合的某一个元素,与前一个或者后一个元素替换orderby
        Teachplan teachplan = this.selectTeachPlanById(planId);

        // 获取同级集合
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(teachplan.getParentid() != null, Teachplan::getParentid, teachplan.getParentid());
        queryWrapper.eq(teachplan.getCourseId() != null, Teachplan::getCourseId, teachplan.getCourseId());
        queryWrapper.orderByAsc(teachplan.getOrderby() != null, Teachplan::getOrderby);
        List<Teachplan> list = teachplanMapper.selectList(queryWrapper);
        if (list.size() <= 1) {
            return;
        }

        if (moveEnum == MoveEnum.UP) {
            for (int i = 0; i < list.size(); i++) {
                if (planId.equals(list.get(i).getId()) && i != 0) {
                    int j = i;
                    moveTeachPlan(teachplan, list.get(--j));
                }
            }
        }
        if (moveEnum == MoveEnum.DOWN) {
            for (int i = 0; i < list.size(); i++) {
                if (planId.equals(list.get(i).getId()) && i != list.size() - 1) {
                    int j = i;
                    moveTeachPlan(teachplan, list.get(++j));
                }
            }
        }
    }

    // 交换orderBy的值然后更新数据
    private void moveTeachPlan(Teachplan plan1, Teachplan plan2){
        Teachplan newTeachPlan1 = new Teachplan();
        BeanUtils.copyProperties(plan1, newTeachPlan1);
        newTeachPlan1.setOrderby(plan2.getOrderby());

        Teachplan newTeachPlan2 = new Teachplan();
        BeanUtils.copyProperties(plan2, newTeachPlan2);
        newTeachPlan2.setOrderby(plan1.getOrderby());

        teachplanMapper.updateById(newTeachPlan1);
        teachplanMapper.updateById(newTeachPlan2);
    }

    // 根据id查询单条课程计划
    private Teachplan selectTeachPlanById(Long planId) {
        Teachplan teachplan = teachplanMapper.selectById(planId);
        if (teachplan == null) {
            throw new LearnSuccessException("课程不存在");
        }
        return teachplan;
    }

    // 获取最新的排序号
    private int getTeachPlanOrder(long courseId,long parentId){
        QueryWrapper<Teachplan> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Teachplan::getCourseId,courseId);
        wrapper.lambda().eq(Teachplan::getParentid,parentId);
        wrapper.select("max(orderby) as orderby");
        Teachplan teachplan = teachplanMapper.selectOne(wrapper);
        return teachplan == null ? 0 : teachplan.getOrderby();
    }

}
