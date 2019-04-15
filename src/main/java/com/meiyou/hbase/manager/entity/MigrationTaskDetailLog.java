package com.meiyou.hbase.manager.entity;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "migration_task_detail_log")
public class MigrationTaskDetailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "migration_task_log_id")
    private Integer migrationTaskLogId;

    @Column(name = "create_time")
    private Date createTime;

    private String content;

    public MigrationTaskDetailLog(){

    }

    public MigrationTaskDetailLog(int migrationTaskLogId, String content){
        this.migrationTaskLogId = migrationTaskLogId;
        this.content = content;
        this.createTime= new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMigrationTaskLogId() {
        return migrationTaskLogId;
    }

    public void setMigrationTaskLogId(Integer migrationTaskLogId) {
        this.migrationTaskLogId = migrationTaskLogId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
