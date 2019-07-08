package com.dj.p2p.wind.common;

import java.util.UUID;

public class UUIDUtil {

	public static String getUUID() {
		
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
