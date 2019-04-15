package com.meiyou.hbase.manager.dao;

import com.meiyou.hbase.manager.entity.MigrationTask;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface MigrationTaskRepository extends CrudRepository<MigrationTask,Integer>{
    @Modifying
    @Transactional
    @Query(value = "update migration_task t set t.status = ?2 where t.id = ?1",nativeQuery = true)
    void updateStatus(int id, int status);

    MigrationTask findMigrationTaskByIdEqualsAndStatusEquals(int id,int status);

}
