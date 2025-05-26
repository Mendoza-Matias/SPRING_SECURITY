package com.course.api.spring_security_course.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RoleEnum {
    ADMINISTRATOR(Arrays.asList(
            RolePermissionsEnum.READ_ALL_PRODUCT,
            RolePermissionsEnum.READ_ONE_PRODUCT,
            RolePermissionsEnum.READ_ALL_CATEGORY,
            RolePermissionsEnum.READ_ONE_CATEGORY,
            RolePermissionsEnum.CREATE_PRODUCT,
            RolePermissionsEnum.CREATE_CATEGORY,
            RolePermissionsEnum.UPDATE_PRODUCT,
            RolePermissionsEnum.UPDATE_CATEGORY,
            RolePermissionsEnum.DISABLE_PRODUCT,
            RolePermissionsEnum.DISABLE_CATEGORY,

            RolePermissionsEnum.READ_MY_PROFILE
    )),
    ASSISTANT_ADMINISTRATOR(
            Arrays.asList(
                    RolePermissionsEnum.READ_ALL_PRODUCT,
                    RolePermissionsEnum.READ_ONE_PRODUCT,
                    RolePermissionsEnum.READ_ALL_CATEGORY,
                    RolePermissionsEnum.READ_ONE_CATEGORY,
                    RolePermissionsEnum.CREATE_PRODUCT,
                    RolePermissionsEnum.CREATE_CATEGORY,
                    RolePermissionsEnum.UPDATE_PRODUCT,
                    RolePermissionsEnum.UPDATE_CATEGORY,

                    RolePermissionsEnum.READ_MY_PROFILE
            )
    ),
    CUSTOMER(Arrays.asList(
            RolePermissionsEnum.READ_MY_PROFILE //arrastra el permiso
    ));

    private List<RolePermissionsEnum> permissions;
}
