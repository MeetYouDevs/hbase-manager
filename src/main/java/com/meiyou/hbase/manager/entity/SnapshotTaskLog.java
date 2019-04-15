package com.meiyou.hbase.manager.entity;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "snapshot_task_log")
public class SnapshotTaskLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "snapshot_task_id")
    private Integer snapshotTaskId;

    private String cluster;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "snapshot_name")
    private String snapshotName;

    @Column(name = "`start_time`")
    private Date startTime;
    @Column(name = "`end_time`")
    private Date endTime;
    @Column(name = "`fire_time`")
    private Date fireTime;
    @Column(name = "`create_time`")
    private Date createTime;

    private String suffix;

    @Column(name = "`has_deleted`")
    private Integer hasDeleted;



    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getHasDeleted() {
        return hasDeleted;
    }

    public void setHasDeleted(Integer hasDeleted) {
        this.hasDeleted = hasDeleted;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSnapshotTaskId() {
        return snapshotTaskId;
    }

    public void setSnapshotTaskId(Integer snapshotTaskId) {
        this.snapshotTaskId = snapshotTaskId;
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

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
