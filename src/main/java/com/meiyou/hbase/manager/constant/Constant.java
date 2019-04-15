package com.meiyou.hbase.manager.constant;

import org.codehaus.jackson.type.TypeReference;

import com.meiyou.hbase.manager.entity.Table;

public class Constant {
	public static final TypeReference<Table> TABLE_TYPE = new TypeReference<Table>() {
	};

	public static final int MIGRATION_STATUS_CREATE = 0;//运行状态 0-创建  1-运行中 3-完成 9-失败
	public static final int MIGRATION_STATUS_RUNNING= 1;
	public static final int MIGRATION_STATUS_DONE = 3;
	public static final int MIGRATION_STATUS_FAIL = 9;

	public static final int TASK_CYCLE_TYPE_HOUR = 1;
	public static final int TASK_CYCLE_TYPE_DAY = 2;
	public static final int TASK_CYCLE_TYPE_WEEK = 3;
	public static final int TASK_CYCLE_TYPE_MONTH = 4;

	public static final String JOB_MIGRATION = "MIGRATION";


	public static final int STATUS_NORMAL = 0;//状态正常
	public static final int STATUS_DISABLE = 1;//状态禁用

	public static final int FIRE_TYPE_AUTO = 1;//调度方式：调度执行
	public static final int FIRE_TYPE_MANUAL = 2;//调度方式：手工执行

}
