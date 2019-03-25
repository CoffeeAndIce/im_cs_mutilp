package dataHandle.emun;

/**
* @Description 类描述: TODO 用于存放Redis账户信息
* @author 作者: CoffeeAndIce <pre>
* @date 时间: 2018年6月19日 下午2:57:23 <pre>
 */
public enum RedisEnum {
	Host("127.0.0.1"),
	PWD("ajishu666"),
	Port("6379"),
	Max_Total("1000"),
	MaxWaitMillis("1000"),
	MaxIdle("100")
	;
	private String value;

	public String value() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private RedisEnum(String value) {
		this.value = value;
	}
	
}
