package com.treetalk.util;

import com.treetalk.model.entity.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 安全用户工具类，用于获取当前登录用户信息
 *
 * @author lizheng
 * @created 2025/8/26 16:17
 */
@Component
public class SecurityUserUtils {

    /**
     * 获取当前登录用户的ID
     *
     * @return 用户ID，如果未登录则返回null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                try {
                    return Long.parseLong((String) principal);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("用户信息解析异常");
                }
            } else if (principal instanceof Account) {
                return ((Account) principal).getId();
            }
        }
        throw new RuntimeException("获取当前用户异常");
    }

    /**
     * 获取当前登录用户的用户名（邮箱）
     *
     * @return 用户名，如果未登录则返回null
     */
    public static Account getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Account) {
                return ((Account) principal);
            }
        }
        return null;
    }

    /**
     * 判断用户是否已登录
     *
     * @return 已登录返回true，否则返回false
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 清除安全上下文
     */
    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}