package vip.wyait.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import vip.wyait.admin.pojo.RolePermissionKey;

import java.util.List;

@Mapper
public interface RolePermissionMapper {
    int deleteByPrimaryKey(RolePermissionKey key);

    int insert(RolePermissionKey record);

    int insertSelective(RolePermissionKey record);

	List<RolePermissionKey> findByRole(int roleId);
}