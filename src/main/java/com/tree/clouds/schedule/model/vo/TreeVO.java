package com.tree.clouds.schedule.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class TreeVO {
    private String id;
    private String label;
    private List<TreeVO> children;

}
