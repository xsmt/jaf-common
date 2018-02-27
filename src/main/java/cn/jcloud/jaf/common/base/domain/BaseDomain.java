package cn.jcloud.jaf.common.base.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by Wei Han on 2016/1/5.
 */
@MappedSuperclass
public abstract class BaseDomain<I extends Serializable> {

    @Id
    @GeneratedValue(generator = "id")
    @Column(length = 36)
    private I id;

    public I getId() {
        return this.id;
    }

    public void setId(I id) {
        this.id = id;
    }
}
