package cn.com.wudskq.model.query;

import cn.com.wudskq.model.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenfangchao
 * @title: UserInfoQueryDTO
 * @projectName wc-manager-system
 * @description: TODO 用户信息查询模型
 * @date 2022/6/23 2:43 PM
 */
@Data
@ApiModel("用户信息查询模型")
public class UserInfoQueryDTO extends PageDTO implements Serializable {

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("账号")
    private String userName;


}