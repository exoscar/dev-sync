package org.devsync.spring.infrastructure.redis;

import java.util.UUID;

public class RedisKey {
    private static final String PREFIX = "devsync:";

    private RedisKey(){

    }

    public static String workspace(UUID workspaceId){
        return PREFIX +"workspace:"+workspaceId;
    }
    public static String project(UUID projectId){
        return PREFIX +"project:id"+projectId;
    }
    public static String workspaceDashboard(UUID workspaceId) {
        return PREFIX + "dashboard:workspace:" + workspaceId;
    }

    public static String projectDashboard(UUID projectId) {
        return PREFIX + "dashboard:project:" + projectId;
    }

    public static String memberStatistics(UUID workspaceId){
        return PREFIX+"dashboard:memberstatistics:workspace"+workspaceId;
    }

    public static String unreadNotifications(UUID userId, UUID workspaceId) {
        return PREFIX + "notification:unread:" + userId + ":" + workspaceId;
    }

    public static String workspaceMembership(
            UUID workspaceId,
            UUID userId
    ) {
        return PREFIX
                + "membership:"
                + workspaceId
                + ":"
                + userId;
    }

    public static String unreadNotification(UUID userId){
        return PREFIX + "notifications:"+userId;
    }

    public static String jwtBlacklist(String jti) {
        return PREFIX + "jwt:blacklist:" + jti;
    }



}
