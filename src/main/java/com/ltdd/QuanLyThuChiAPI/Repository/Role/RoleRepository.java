package com.ltdd.QuanLyThuChiAPI.Repository.Role;

import com.ltdd.QuanLyThuChiAPI.Model.Role.RoleModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleModel, String> {
    RoleModel findByName(String name);

}
