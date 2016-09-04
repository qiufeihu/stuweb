package com.qfh;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by QiuFeihu on 16/9/3.
 */
public class Demo01 {


    private Jedis jedis;

    @Before
    public void setup() {
        //连接redis服务器，192.168.0.100:6379
        jedis = new Jedis("192.168.99.105", 6379);
        //权限认证
        jedis.auth("requirepass");
    }

    /**
     * key - value 存储结构
     */
    @Test
    public  void testString(){
        //-----添加数据----------
        jedis.set("name", "小红");//向key-->name中放入了value-->xinxin
        System.out.println(jedis.get("name"));//执行结果：xinxin

        jedis.append("name", "小白"); //拼接
        System.out.println(jedis.get("name"));

        jedis.del("name");  //删除某个键
        System.out.println(jedis.get("name"));
        //设置多个键值对
        jedis.mset("name", "小明", "age", "23", "qq", "476777XXX");
        jedis.incr("age"); //进行加1操作
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
    }

    /**
     * redis操作Map
     */
    @Test
    public void testMap() {
        //-----添加数据----------
        Map<String, String> map = new HashMap<String, String>();
        map.put("name","小米");
        map.put("age", "22岁");
        map.put("qq", "123456");
        map.put("爱好", "看书");
        jedis.del("userSet");
        jedis.hmset("userSet", map);
        //取出userSet中的name，执行结果:[minxr]-->注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = jedis.hmget("userSet", "name", "age", "qq","爱好");
        System.out.println(rsmap);

        //删除map中的某个键值
        jedis.hdel("userSet", "age");
        System.out.println(jedis.hmget("userSet", "age")); //因为删除了，所以返回的是null
        System.out.println(jedis.hlen("userSet")); //返回key为userSet的键中存放的值的个数3
        System.out.println(jedis.exists("userSet"));//是否存在key为userSet的记录 返回true
        System.out.println(jedis.hkeys("userSet"));//返回map对象中的所有key
        System.out.println(jedis.hvals("userSet"));//返回map对象中的所有value

        //可以用循环Map的方式打印
        for(String key : jedis.hkeys("userSet")){
            System.out.println(key + ":" + jedis.hmget("userSet", key).get(0));  //值存的是List
        }
    }

    /**
     * jedis操作List
     */
    @Test
    public void testList() {
        //开始前，先移除所有的内容
        jedis.del("userSetList");
        System.out.println(jedis.lrange("list", 0, -1));
        //先向key userSetList中存放三条数据
        jedis.lpush("userSetList", "spring");
        jedis.lpush("userSetList", "struts");
        jedis.lpush("userSetList", "hibernate");
        //再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        List<String> list = jedis.lrange("userSetList", 0, -1);
        System.out.println(list.get(0));
        System.out.println(list);

        jedis.del("userSetList");
        jedis.rpush("userSetList", "spring");
        jedis.rpush("userSetList", "struts");
        jedis.rpush("userSetList", "hibernate");
        System.out.println(jedis.lrange("userSetList", 0, -1));
    }

    /**
     * jedis操作Set
     */
    @Test
    public void testSet() {

        //添加
        jedis.del("userSet");
        jedis.sadd("userSet", "liuling");
        jedis.sadd("userSet", "xinxin");
        jedis.sadd("userSet", "ling");
        jedis.sadd("userSet", "zhangxinxin");
        jedis.sadd("userSet", "who");
        //移除noname
        jedis.srem("userSet", "who");
        System.out.println(jedis.smembers("userSet"));//获取所有加入的value
        System.out.println(jedis.sismember("userSet", "who"));//判断 who 是否是userSet集合的元素
        System.out.println(jedis.srandmember("userSet"));
        System.out.println(jedis.scard("userSet"));//返回集合的元素个数
    }
}


