package com.website.loveconnect.repository.query;

public class ReportQueries {
    public static final String FIND_ALL_REPORT =
            "SELECT \n" +
                    "r.report_id as reportId,\n" +
                    "r.report_date as reportDate,\n" +
                    "r.status_report as reportStatus,\n" +
                    "r.additional_details as detail,\n" +
                    "r.review_date as reviewDate,\n" +
                    "r.reporter_id AS reporterId,\n"+
                    "reporter_up.full_name AS reporterFullName,\n" +
                    "r.reported_id AS reportedId,\n" +
                    "reported_up.full_name AS reportedFullName,\n" +
                    "rt.type_name AS reportTypeName,\n" +
                    "reviewer_up.full_name AS reviewerFullName\n" +
                    "FROM reports r\n" +
                    "JOIN users reporter_u ON r.reporter_id = reporter_u.user_id\n" +
                    "JOIN user_profiles reporter_up ON reporter_u.user_id = reporter_up.user_id\n" +
                    "JOIN users reported_u ON r.reported_id = reported_u.user_id\n" +
                    "JOIN user_profiles reported_up ON reported_u.user_id = reported_up.user_id\n" +
                    "JOIN report_types rt ON r.report_type_id = rt.report_type_id\n" +
                    "LEFT JOIN users reviewer_u ON r.reviewed_by = reviewer_u.user_id\n" +
                    "LEFT JOIN user_profiles reviewer_up ON reviewer_u.user_id = reviewer_up.user_id\n"+
                    "where reported_u.account_status = 'ACTIVE'";
}
