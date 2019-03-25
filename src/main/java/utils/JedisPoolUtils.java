package utils;

import dataHandle.emun.RedisEnum;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
* @Description 类描述: TODO 初始化连接池
* @author 作者: CoffeeAndIce <pre>
* @date 时间: 2018年6月19日 下午3:10:24 <pre>
 */
public class JedisPoolUtils {
	
    private static JedisPool pool = null;
    private static int manxTotal = Integer.parseInt(RedisEnum.Max_Total.value());
    private static int maxWaitMillis = Integer.parseInt(RedisEnum.MaxWaitMillis.value());
    private static int idle = Integer.parseInt(RedisEnum.MaxIdle.value());
    private static int port = Integer.parseInt(RedisEnum.Port.value());
    private static String host = RedisEnum.Host.value();

    public static void main(String[] args) {
        Jedis jedis = JedisPoolUtils.getJedis();
        System.out.printf(jedis.get("666")+"777");
        System.out.print(jedis);
    }
    /**
 * 建立连接池 真实环境，一般把配置参数缺抽取出来。
 * 
 */
    private static void createJedisPool() {

        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();

        // 设置最大连接数
        config.setMaxTotal(manxTotal);

        // 设置最大阻塞时间，记住是毫秒数milliseconds
        config.setMaxWaitMillis(maxWaitMillis);

        // 设置空闲连接
        config.setMaxIdle(idle);

        // 创建连接池
        pool = new JedisPool(config,host, port);

    }

    /**
 * 在多线程环境同步初始化
 */
    private static synchronized void poolInit() {
        if (pool == null)
            createJedisPool();
    }

    /**
 * 获取一个jedis 对象
 * 
 * @return
 */
    public static Jedis getJedis() {

        if (pool == null)
            poolInit();
        Jedis resource = pool.getResource();
        resource.auth(RedisEnum.PWD.value());
        return resource;
    }

    /**
 * 归还一个连接
 * 
 * @param jedis
 */
    public static void returnRes(Jedis jedis) {
        jedis.close();
    }

}