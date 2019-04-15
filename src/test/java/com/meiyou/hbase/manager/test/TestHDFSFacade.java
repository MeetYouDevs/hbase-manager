package com.meiyou.hbase.manager.test;

import com.meiyou.hbase.manager.utils.HDFSFacade;
import com.meiyou.hbase.manager.utils.HDFSFacadeFactory;

public class TestHDFSFacade {
	public static void main(String[] args) {
		HDFSFacade hdfsFacade = HDFSFacadeFactory.createHDFSFacade("");
		
		System.out.println(hdfsFacade.getPathSpaceSize("/hbase/data/default/test"));
	}
}
