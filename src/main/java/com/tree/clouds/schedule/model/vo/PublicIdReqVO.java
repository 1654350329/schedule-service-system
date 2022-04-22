package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "PublicIdReqVO", description = "通用请求VO")
public class PublicIdReqVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", required = true)
    @NotEmpty(message = "主键不能为空")
    private String id;
}