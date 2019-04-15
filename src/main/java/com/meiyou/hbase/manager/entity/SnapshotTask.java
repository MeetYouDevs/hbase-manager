package com.meiyou.hbase.manager.entity;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "snapshot_task")
public class SnapshotTask implements Serializable {
	private static final long serialVersionUID = -5122347783878328114L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cluster;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "cycle_type")
    private Integer cycleType;
    @Column(name = "snapshot_name")
    private String snapshotName;

    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "inure_date")
    private Date inureDate;
    @Column(name = "expire_date")
    private Date expireDate;
    private Integer status;
    @Column(name = "custom_cron")
    private String customCron;
    @Column(name = "custom_cron_on")
    private Integer customCronOn;

    @Column(name = "keep_cnt")
    private Integer keepCnt;

    public Integer getKeepCnt() {
        return keepCnt;
    }

    public void setKeepCnt(Integer keepCnt) {
        this.keepCnt = keepCnt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getCycleType() {
        return cycleType;
    }

    public void setCycleType(Integer cycleType) {
        this.cycleType = cycleType;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getInureDate() {
        return inureDate;
    }

    public void setInureDate(Date inureDate) {
        this.inureDate = inureDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCustomCron() {
        return customCron;
    }

    public void setCustomCron(String customCron) {
        this.customCron = customCron;
    }

    public Integer getCustomCronOn() {
        return customCronOn;
    }

    public void setCustomCronOn(Integer customCronOn) {
        this.customCronOn = customCronOn;
    }
}
