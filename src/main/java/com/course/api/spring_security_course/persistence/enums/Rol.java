package com.course.api.spring_security_course.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Rol {
    ROLE_ADMINISTRATOR(Arrays.asList(
            RolPermissions.READ_ALL_PRODUCT,
            RolPermissions.READ_ONE_PRODUCT,
            RolPermissions.READ_ALL_CATEGORY,
            RolPermissions.READ_ONE_CATEGORY,
            RolPermissions.CREATE_PRODUCT,
            RolPermissions.CREATE_CATEGORY,
            RolPermissions.UPDATE_PRODUCT,
            RolPermissions.UPDATE_CATEGORY,
            RolPermissions.DISABLE_PRODUCT,
            RolPermissions.DISABLE_CATEGORY,

            RolPermissions.READ_MY_PROFILE
    )),
    ROLE_ASSISTANT_ADMINISTRATOR(
            Arrays.asList(
                    RolPermissions.READ_ALL_PRODUCT,
                    RolPermissions.READ_ONE_PRODUCT,
                    RolPermissions.READ_ALL_CATEGORY,
                    RolPermissions.READ_ONE_CATEGORY,
                    RolPermissions.CREATE_PRODUCT,
                    RolPermissions.CREATE_CATEGORY,
                    RolPermissions.UPDATE_PRODUCT,
                    RolPermissions.UPDATE_CATEGORY,

                    RolPermissions.READ_MY_PROFILE
            )
    ),
    ROLE_CUSTOMER(Arrays.asList(
            RolPermissions.READ_MY_PROFILE //arrastra el permiso
    ));

    private List<RolPermissions> permissions;
}
