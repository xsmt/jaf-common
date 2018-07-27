package cn.jcloud.jaf.common.handler;

import com.nd.social.common.approuter.domain.AppRouter;
import com.nd.social.common.constant.CommonModules;
import org.springframework.core.NamedThreadLocal;

/**
 * @author hasayaki(125473)
 *
 */
public class AppRouterHandler {
	
	private AppRouterHandler() {
    	
    }

    private static ThreadLocal<AppRouter> appRouterThreadLocal = new NamedThreadLocal<>("appRouter");

    public static String getAppId() {
    	
        AppRouter appRouter = appRouterThreadLocal.get();
        if (appRouter != null) {
            return appRouter.getAppId();
        } else {
            return null;
        }
    }
    
    public static String getStrictAppId() {
    	
        return getStrictAppRouter().getAppId();
    }

    public static AppRouter getAppRouter() {
    	
    	return appRouterThreadLocal.get();
    }
    
    public static AppRouter getStrictAppRouter() {
    	
        AppRouter appRouter = appRouterThreadLocal.get();
        if (appRouter == null) {
            throw CommonModules.APP_ROUTER.notFound();
        }
        return appRouter;
    }
    
    public static void setAppRouter(AppRouter appRouter) {
    	
        appRouterThreadLocal.set(appRouter);
    }
    
    public static void clear() {
    	
        appRouterThreadLocal.remove();
    }
}
