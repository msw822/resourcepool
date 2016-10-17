package com.hp.xo.resourcepool.enumtype;

public enum PhysicsHostStatus {
	// 利用构造函数传参
	ASSIGNMENTESD("assignmented"), UNASSIGNMENTE("unassignment"),VIRTUALED("virtualed");

	// 定义私有变量
	private String stauts;

	// 构造函数，枚举类型只能为私有
	private PhysicsHostStatus(String _Stauts) {
		this.stauts = _Stauts;
	}

	@Override
	public String toString() {
		return stauts;
	}

}
