package com.learnsuccess.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    // 数据列表
    @ApiModelProperty("数据列表")
    private List<T> items;

    //总记录数
    @ApiModelProperty("总记录数")
    private long counts;

    //当前页码
    @ApiModelProperty("当前页码")
    private long page;

    //每页记录数
    @ApiModelProperty("每页记录数")
    private long pageSize;
}
