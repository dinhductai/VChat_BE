package com.website.loveconnect.repository.custom.query;

public class RoleQueries {
    public static final String GET_ROLE_AND_PERMISSIONS =
            "SELECT r.role_name as roleName, r.description as roleDescription, r.created_date as createTime, \n" +
            "JSON_ARRAYAGG(\n" +
            "   JSON_OBJECT(\n" +
            "'permissionName', p.permission_name, \n" +
            "'description', p.description\n" +
            "   )) AS permissions\n" +
            "FROM roles r\n" +
            "LEFT JOIN role_permissions rp ON r.role_id = rp.role_id\n" +
            "LEFT JOIN permissions p ON p.permission_id = rp.permission_id\n" +
            "GROUP BY r.role_name, r.description, r.created_date;";
}
