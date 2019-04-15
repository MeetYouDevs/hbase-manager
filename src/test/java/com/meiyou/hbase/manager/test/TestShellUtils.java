package com.meiyou.hbase.manager.test;

import com.jcraft.jsch.Session;
import com.meiyou.hbase.manager.utils.ShellUtils;

public class TestShellUtils {
	public static void main(String[] args) throws Exception {
		Session session = ShellUtils.connect("", "", "", 22);

		String result = ShellUtils.runCmd(session,
				"source /etc/profile; hbase org.apache.hadoop.hbase.snapshot.ExportSnapshot -Dmapreduce.map.memory.mb=1024 -Dmapreduce.job.queuename=test -snapshot snapshot_z_test_snapshot -copy-to  -mappers 1", new ShellUtils.LogCallBack() {
					@Override
					public void call(String message) {
						System.out.println(message);
					}
				});

		System.out.println("Snapshot export result: " + result);
	}
}
