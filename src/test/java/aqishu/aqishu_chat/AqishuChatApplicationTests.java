package aqishu.aqishu_chat;

import dataHandle.emun.ChatFlag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import utils.RedisUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AqishuChatApplicationTests {
	private static final DateFormat TIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Test
	public void TestMain() {
//		String  keys = "CHAT_MSGf34c0325dbff4e46ba76b909b52e7e18";
//		RedisUtil.Hash hashMethod = RedisUtil.getHashMethod();
//		long hlen = hashMethod.hlen(keys);
//		Map<String, String> stringStringMap = hashMethod.hgetAll(keys);
//		stringStringMap.forEach((k,v)->{
//			String replace = k.replace("T", " ");
//			System.out.println(replace +" "+v);
//		});
		Map<String, String> stringStringMap = RedisUtil.getHashMethod().hgetAll(ChatFlag.ALIVE_LIST);
		System.out.println(stringStringMap);
	}

}
