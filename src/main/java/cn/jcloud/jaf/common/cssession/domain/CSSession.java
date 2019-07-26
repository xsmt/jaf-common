package cn.jcloud.jaf.common.cssession.domain;

/**
 * Created by Wei Han on 2016-08-11.
 */
public class CSSession {

    private long uid;
    private String path;
    private String session;
    private long expireAt;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }
}
