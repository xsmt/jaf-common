/* =============================================================
 * Created: [2015/4/29 17:40] by wuzj(971643)
 * =============================================================
 *
 * Copyright 2014-2015 NetDragon Websoft Inc. All Rights Reserved
 *
 * =============================================================
 */
package cn.jcloud.jaf.common.handler;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class RoleAspect {

    private static Logger logger = LoggerFactory.getLogger(RoleAspect.class);

//    private static Map requestRoleMap = new HashMap<>();
//
//    static {
//        try {
//            InputStream in = RoleAspect.class.getClassLoader().getResourceAsStream("requestRoleList.json");
//            if (in != null) {
//                ObjectMapper objectMapper = new ObjectMapper();
//                requestRoleMap = objectMapper.readValue(in, Map.class);
//            } else {
//                logger.error("requestRoleList not found!!");
//            }
//        } catch (IOException e) {
//            logger.error("Read requestRoleList.json error!!", e);
//        }
//    }

//    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)" +
//            "||@within(org.springframework.stereotype.Controller)")
//    public void contControllerExec() {
//        //Empty
//    }
//
//    @Before("contControllerExec()")
//    public void doAccessCheck(JoinPoint jp) throws Throwable {
//
//    }

//    @Before("contControllerExec()")
//    public void doAccessCheck(JoinPoint jp) throws Throwable {
//        String controllerName = JoinPointUtil.getClassName(jp);
//        String actionName = JoinPointUtil.getMethodName(jp);
//        Object object = SecurityContextHolder.getContext().getAuthentication().getDetails();
//        Boolean isGetRequest = JoinPointUtil.getRequestMethod(jp).equals(RequestMethod.GET);
//        Boolean matchesBearerToken = matcher(controllerName, actionName,
//                (List<Map<String, ?>>) requestRoleMap.get("bearerToken"));
//        Boolean isSecret = matcher(controllerName, actionName, (List<Map<String, ?>>) requestRoleMap.get("secret"));
//        // 所有非GET请求和要求beartoken请求的 都要求登录
//        if (!isGetRequest || matchesBearerToken || isSecret) {
//            if (!(object instanceof UserCenterUserDetails)) {
//                throw WafI18NException.of(ErrorCode.USER_NOT_LOGIN);
//            }
//            // 对beartoken的权限处理
//            if (matchesBearerToken) {
//                UserInfo userInfo = ((UserCenterUserDetails) object).getUserInfo();
//                if (userInfo.getUserType() == null
//                        || !AuthorizationTypePrefix.BEARER.equalsIgnoreCase(userInfo.getUserType())) {
//                    throw WafI18NException.of(ErrorCode.NO_PERMISSION);
//                }
//                // 对bearer账号做判断
//                if (!vaildBearerUid(controllerName)) {
//                    logger.error(controllerName + "," + actionName + " 不在白名单内的" + UserHandler.getBearerUser());
//                    throw WafI18NException.of(ErrorCode.NO_PERMISSION);
//                }
//            }
//            if (isSecret) {
//                RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//                HttpServletRequest request = sra.getRequest();
//                if (request == null
//                        || request.getParameter("secret") == null
//                        || !request.getParameter("secret").equals(
//                        "nd" + new SimpleDateFormat("yyyyMMddHH").format(new Date()) + WafProperties.getProperty("waf.uc.realm"))) {
//                    throw WafI18NException.of(ErrorCode.NO_PERMISSION);
//                }
//            }
//        }
//
//    }
//
//    private Boolean matcher(String controller, String action, List<Map<String, ?>> list) {
//        for (Map<String, ?> map : list) {
//            if (map.get("controller").equals(controller)) {
//                if (map.containsKey("included")) {
//                    List<String> included = (List<String>) map.get("included");
//                    if (included.contains(action))
//                        return true;
//                } else if (map.containsKey("excluded")) {
//                    List<String> excluded = (List<String>) map.get("excluded");
//                    if (!excluded.contains(action))
//                        return true;
//                } else {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 对bearer账号进行鉴权
//     *
//     * @param controllerName
//     * @return
//     */
//    private Boolean vaildBearerUid(String controllerName) {
//        String currentBearerUid = UserHandler.getBearerUser();
//        if (currentBearerUid == null) {
//            return false;
//        }
//        // 全局部分的判断
//        if (requestRoleMap.containsKey("bearerUid")) {
//            List<String> globalBearers = (List<String>) requestRoleMap.get("bearerUid");
//            if (globalBearers == null) {
//                return true; // 没配置的话默认全部允许
//            } else {
//                if (globalBearers.contains(currentBearerUid)) {
//                    return true; // 被包含，通过。
//                }
//            }
//        }
//        // 局部的判断
//        List<Map<String, ?>> list = (List<Map<String, ?>>) requestRoleMap.get("bearerToken");
//        if (list == null || list.size() == 0) {
//            return false; // 其实是多余的判断
//        }
//        for (Map<String, ?> map : list) {
//            if (map.get("controller").equals(controllerName)) { // 这个controllerName一定会命中，因为外部的逻辑
//                if (!map.containsKey("bearerUid")) {
//                    return false; // 找到对应的controllerName，如果局部里的也没配置，则拒绝
//                }
//                List<String> localBearers = (List<String>) map.get("bearerUid");
//                if (localBearers == null) {
//                    return false; // 局部也没配置当前环境，拒绝
//                }
//                if (!localBearers.contains(currentBearerUid)) {
//                    return false; // 白名单数组为空，代表没有允许的。不包含也是不允许
//                }
//                return true; // 被包含，通过。和下面的不合并，逻辑清晰一些
//            }
//        }
//        return false;
//    }

}
