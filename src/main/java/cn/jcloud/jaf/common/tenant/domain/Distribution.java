package cn.jcloud.jaf.common.tenant.domain;

import cn.jcloud.jaf.common.base.domain.BaseDomain;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Wei Han on 2016/2/4.
 */
@Entity
@Table(name = "t_distribution")
@GenericGenerator(name = "id", strategy = "assigned")
@Document(collection = "t_distribution")
@TypeAlias("Distribution")
public class Distribution extends BaseDomain<Integer> {

    @Column(length = 20)
    private String dbConn;

    private int tenancy;

    private int tenancyAmount;

    public String getDbConn() {
        return this.dbConn;
    }

    public void setDbConn(String dbConn) {
        this.dbConn = dbConn;
    }

    public int getTenancy() {
        return this.tenancy;
    }

    public void setTenancy(int tenancy) {
        this.tenancy = tenancy;
    }

    public int getTenancyAmount() {
        return this.tenancyAmount;
    }

    public void setTenancyAmount(int tenancyAmount) {
        this.tenancyAmount = tenancyAmount;
    }
}
