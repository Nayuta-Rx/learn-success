package com.learnsuccess.content.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.learnsuccess.content.mapper.CourseCategoryMapper;
import com.learnsuccess.content.model.dto.CourseCategoryTreeDto;
import com.learnsuccess.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Nayuta-Rx
 * @Description: 课程分类树形结构
 * @DataTime: 2023/7/13 16:49
 **/
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        List<CourseCategoryTreeDto> treeDtos = courseCategoryMapper.selectTreeNodes(id);
        return buildTree(treeDtos, id);
    }

    /**
     * 递归构建树
     * @param dtoList
     * @param parentId
     * @return
     */
    private List<CourseCategoryTreeDto> buildTree(List<CourseCategoryTreeDto> dtoList, String parentId){
        // 获取父节点为parentId的所有集合
        List<CourseCategoryTreeDto> collect = dtoList
                .stream()
                .filter(dto -> parentId.equals(dto.getParentid()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)){
            return null;
        }

        for (CourseCategoryTreeDto categoryTreeDto : collect) {
            // 子节点不需要递归
            if (categoryTreeDto.getIsLeaf() == 0) {
                categoryTreeDto.setChildrenTreeNodes(buildTree(dtoList, categoryTreeDto.getId()));
            }
        }

        return collect;
    }
}
