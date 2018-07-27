package cn.jcloud.jaf.common.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Title: Module Information         </p>
 * <p>Description: Function Description </p>
 * <p>Copyright: Copyright (c) 2017     </p>
 * <p>Company: ND Co., Ltd.       </p>
 * <p>Create Time: 2017年1月26日           </p>
 * @author Linhua(831008)
 * <p>Update Time:                      </p>
 * <p>Updater:                          </p>
 * <p>Update Comments:                  </p>
 */
public class BtsHandler implements IWebAnnotationHandler<BtsApi> {

    private static Set<String> btsMethods = Collections.emptySet();

    public static boolean isBtsMethod(String methodSignature) {
        return btsMethods.contains(methodSignature);
    }

    @Override
    public void init() {
        btsMethods = new HashSet<String>();
    }

    @Override
    public void handle(BtsApi annotation, String methodSignature) {
        btsMethods.add(methodSignature);
    }

    @Override
    public void complete() {
        btsMethods = Collections.unmodifiableSet(btsMethods);
    }

    @Override
    public Class<BtsApi> annotationClass() {
        return BtsApi.class;
    }

}
