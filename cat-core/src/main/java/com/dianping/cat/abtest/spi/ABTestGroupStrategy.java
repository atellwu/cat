package com.dianping.cat.abtest.spi;

public interface ABTestGroupStrategy {
	public void apply(ABTestContext ctx);
}