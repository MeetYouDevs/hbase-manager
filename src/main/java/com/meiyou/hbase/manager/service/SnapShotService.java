package com.meiyou.hbase.manager.service;

import com.meiyou.hbase.manager.entity.MySnapshotDescription;

import java.util.List;

public interface SnapShotService {
    List<MySnapshotDescription> list(String clusterName);
    boolean delSnapshot(String clusterName,String name);
    boolean createSnapshot(String clusterName,String tableName,String snapshotName);
    boolean deployAutoClean();
}
