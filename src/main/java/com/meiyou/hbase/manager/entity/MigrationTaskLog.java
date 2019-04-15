package com.meiyou.hbase.manager.entity;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "migration_task_log")
public class MigrationTaskLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "migration_task_id")
    private Integer migrationTaskId;

    @Column(name="source_cluster")
    private String sourceCluster;

    @Column(name = "source_table_name")
    private String sourceTableName;

    @Column(name = "target_cluster")
    private String targetCluster;

    @Column(name = "target_table_name")
    private String targetTableName;

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
    @Column(name = "fire_type")
    private Integer fireType;

    public Integer getFireType() {
        return fireType;
    }

    public void setFireType(Integer fireType) {
        this.fireType = fireType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMigrationTaskId() {
        return migrationTaskId;
    }

    public void setMigrationTaskId(Integer migrationTaskId) {
        this.migrationTaskId = migrationTaskId;
    }

    public String getSourceCluster() {
        return sourceCluster;
    }

    public void setSourceCluster(String sourceCluster) {
        this.sourceCluster = sourceCluster;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getTargetCluster() {
        return targetCluster;
    }

    public void setTargetCluster(String targetCluster) {
        this.targetCluster = targetCluster;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
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
