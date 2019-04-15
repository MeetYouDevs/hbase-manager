package com.meiyou.hbase.manager.entity;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "migration_task")
public class MigrationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "source_cluster")
    private String sourceCluster;
    @Column(name = "source_table")
    private String sourceTable;

    @Column(name = "target_cluster")
    private String targetCluster;
    @Column(name = "target_table")
    private String targetTable;

    @Column(name = "run_cluster")
    private String runCluster;

    @Column(name = "run_queue")
    private String runQueue;

    @Column(name = "status")
    private Integer status;

    @Column(name = "migrateType")
    private int migrateType;

    @Column(name = "program_arguments")
    private String programArguments;

    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "executed_date")
    private Date executedDate;

    @Column(name = "custom_cron")
    private String customCron;

    @Column(name = "custom_cron_on")
    private Integer customCronOn;

    private Integer cycle;

    @Column(name = "keep_cnt")
    private Integer keepCnt;

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

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

    public String getTargetCluster() {
        return targetCluster;
    }

    public void setTargetCluster(String targetCluster) {
        this.targetCluster = targetCluster;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getRunCluster() {
        return runCluster;
    }

    public void setRunCluster(String runCluster) {
        this.runCluster = runCluster;
    }


    public String getSourceCluster() {
        return sourceCluster;
    }

    public void setSourceCluster(String sourceCluster) {
        this.sourceCluster = sourceCluster;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public int getMigrateType() {
        return migrateType;
    }

    public void setMigrateType(int migrateType) {
        this.migrateType = migrateType;
    }

    public String getProgramArguments() {
        return programArguments;
    }

    public void setProgramArguments(String programArguments) {
        this.programArguments = programArguments;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExecutedDate() {
        return executedDate;
    }

    public void setExecutedDate(Date executedDate) {
        this.executedDate = executedDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRunQueue() {
        return runQueue;
    }

    public void setRunQueue(String runQueue) {
        this.runQueue = runQueue;
    }
}
