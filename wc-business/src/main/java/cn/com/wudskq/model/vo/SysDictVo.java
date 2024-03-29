package cn.com.wudskq.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;

/**
 * @author chenfangchao
 * @title: SysDictDTO
 * @projectName wc-manager-system
 * @description: TODO 字典视图数据模型
 * @date 2022/6/29 4:37 PM
 */
@Data
@ApiModel(value = "字典视图数据模型")
public class SysDictVo implements Serializable {

    @ApiModelProperty(value = "ID")
    //防止返回前端数据失真
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "字典序号")
    private Long orderNumber;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典代码")
    private String dictCode;

    @ApiModelProperty(value = "字典值")
    private String dictValue;

    @ApiModelProperty(value = "字典分类(0系统字典 1模块字典 2公共字典 3其他)")
    private Integer classify;

    @ApiModelProperty(value = "字典备注")
    private String  remark;

    @ApiModelProperty(value = "状态(0正常 1删除 2禁用)")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
