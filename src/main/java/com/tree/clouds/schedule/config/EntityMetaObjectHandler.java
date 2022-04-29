package com.tree.clouds.schedule.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tree.clouds.schedule.model.entity.BaseEntity;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * mybatis-plus自动置值
 */
@Component
public class EntityMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
        if (baseEntity.getCreatedTime() == null) {
            this.setFieldValByName("createdTime", DateUtil.now(), metaObject);
        }
        if (baseEntity.getDel() == null) {
            this.setFieldValByName("del", 0, metaObject);
        }
        if (baseEntity.getCreatedUser() == null && LoginUserUtil.getUserId() != null) {
            this.setFieldValByName("createdUser", LoginUserUtil.getUserId(), metaObject);
        }
        if (baseEntity.getUpdatedUser() == null && LoginUserUtil.getUserId() != null) {
            this.setFieldValByName("updatedUser", LoginUserUtil.getUserId(), metaObject);
        }
        if (baseEntity.getUpdatedTime() == null) {
            this.setFieldValByName("updatedTime", DateUtil.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
        if (baseEntity.getUpdatedUser() == null && LoginUserUtil.getUserId() != null) {
            this.setFieldValByName("updatedUser", LoginUserUtil.getUserId(), metaObject);
        }
        if (baseEntity.getUpdatedTime() == null) {
            this.setFieldValByName("updatedTime", DateUtil.now(), metaObject);
        }
    }
}
