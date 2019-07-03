# ant-nest
蚂蚁巢,专注于方便的注解

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu) [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE) [![Maven Central](https://img.shields.io/badge/ant--nest-1.1.11--RELEASE-ff69b4.svg)](https://search.maven.org/artifact/com.github.hwywl/ant-nest/1.1.11-RELEASE/jar)
### 说明
我们通过注解可以干很多的事，也很方便，大大提高我的开发效率，至此开发常用的注解方便我们使用。

### 版本
现在最新的版本：
```xml
<dependency>
  <groupId>com.github.hwywl</groupId>
  <artifactId>ant-nest</artifactId>
  <version>1.1.11-RELEASE</version>
</dependency>
```

如果想看看注解例子的使用可以参考这个项目：[spring-boot-mybatis-druid](https://github.com/HWYWL/spring-boot-2.x-examples/tree/master/spring-boot-mybatis-druid)

**注解描述：**

----------

- [@WebLog 请求日志拦截](#WebLog)
- [@SqlStatement mybatis SQL拦截](#SqlStatement)
- [@OperatingTime 方法耗时计算](#OperatingTime)
- [@AESEncryptBody @DESEncryptBody接口返回数据加密](#Encrypt)
- [@AESDecryptBody @DESDecryptBody接口请求数据解密](#Decrypt)
- [@MethodCounter 计算方法调用次数](#MethodCounter)
- [@GetProperties 项目启动时加载自定义properties配置文件](#GetProperties)
- [@ZooLock 分布式锁](#ZooLock)


<div id="WebLog"></div>

### @WebLog 请求日志拦截
我们只需要在controller接口中加入注解即可
![](https://i.imgur.com/uanFdPX.png)

加入注解之后会拦截请求的日志，日志被安排的整整齐齐，方便我们后端人员的调试工作。
![](https://i.imgur.com/9Zmeyuu.png)

<div id="SqlStatement"></div>

### @SqlStatement mybatis SQL拦截
我们只需要在我们操作的接口实现中加入注解即可
![](https://i.imgur.com/3PR2m1J.png)

加入注解之后会拦截mybatis的SQL，日志被安排的整整齐齐，方便我们的调试查看和测试SQL性能。
![](https://i.imgur.com/3YNBBZs.png)

**注意：此注解不能在接口上使用，只能在方法上**

<div id="OperatingTime"></div>

### @OperatingTime 方法耗时计算
我们只需要在我们操作的接口/方法实现中加入注解即可
![](https://i.imgur.com/0ciWYkr.png)

加入注解之后我们就能看到方法耗时，方便我们的测试方法编写的性能。
![](https://i.imgur.com/N0hSuUH.png)

<div id="Encrypt"></div>

### @AESEncryptBody & @DESEncryptBody 接口返回数据加密
使用之前我们先要在application.properties文件中进行参数配置,密钥至少16位，例如：
```
encrypt.body.aes-key=1234567812345678
encrypt.body.des-key=1234567812345678
```

配置完成，接下来我们就可以愉快的使用了，注解可以使用在类上也可以使用在mvc接口上，例如：
```java
@RestController
@DESEncryptBody
public class BaikeController {
    @Autowired
    BaikeService baikeService;

    @RequestMapping(value = "/selectByExample", method = RequestMethod.POST)
    public MessageResult selectByExample(){
        BaikeExample example = new BaikeExample();
        BaikeExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo("海明威");

        List<Baike> baikes = baikeService.selectByExample(example);

        return MessageResult.ok(baikes);
    }

    @RequestMapping(value = "/selectById", method = RequestMethod.POST)
    public MessageResult selectById(){
        Baike baike = baikeService.selectByPrimaryKey(1L);

        return MessageResult.ok(baike);
    }
}
```
**写在类上则所有接口的返回数据都会被加密。**

```java
@DESEncryptBody
@RequestMapping(value = "/selectById", method = RequestMethod.POST)
public MessageResult selectById(){
    Baike baike = baikeService.selectByPrimaryKey(1L);

    return MessageResult.ok(baike);
}
```
**写在单个接口上可以使改接口返回的数据被加密，不影响其他接口。**

<div id="Decrypt"></div>

我们只需要在请求接口的方法上加上解密的注解即可解密，例如：
```java
@DESDecryptBody
@RequestMapping(value = "/selectByIdDecrypt", method = RequestMethod.GET)
public MessageResult selectByIdDecrypt(@RequestBody String content){

    System.out.println(content);

    return MessageResult.ok(content);
}
```
**注意：请求参数必须写上@RequestBody注解，不然注解不会生效**

我们使用postman来请求一下我们的接口
![](https://i.imgur.com/d2TukWV.png)
加密解密至此完成！！！

- AES加密方式：加密模式:ECB、填充:pkcs5padding、数据块:128位、密码: 配置文件中的key、偏移量: ECB模式不需要、输出:hex、字符集：UTF-8

- DES加密方式：加密模式:ECB、填充:pkcs5padding、数据块:128位、密码: 配置文件中的key、偏移量: ECB模式不需要、输出:hex、字符集：UTF-8

返回的数据可以在这个网站上解密测试：http://tool.chacuo.net/cryptaes
![](https://i.imgur.com/6RQTVG8.png)


<div id="MethodCounter"></div>

### @MethodCounter 计算方法调用次数
使用@MethodCounter注解在我们的方法上即可使用，例如：
![](https://i.imgur.com/FKCepyl.png)

获取调用的次数是：
```java
Long num = MethodCounterAspect.cacheMap.get("方法名");
```
**注意：不支持分布式系统，只能计算当前服务器方法调用的次数。**

<div id="GetProperties"></div>

### @GetProperties 项目启动时加载自定义properties配置文件
作用就是标题所示啦，在我们需要使用配置一些东西但又不想写bean的时候可以使用此注解，下面我们来看看注解的使用方式。
注解可以放在类上面也可以放在方法上面，效果其实是一样的，放在方法上本来是想实现用完立刻卸载，目前此功能还没实现。

默认加载**resources**目录,好啦，我们来看看使用：
类加载：表示加载**resources**目录下**aaaa**文件夹下的a.properties
```java
@Configuration
@GetProperties(properties = "aaaa/a.properties")
public class Config {

}
```
![](https://i.imgur.com/dG8j1Am.png)

方法加载：表示加载**resources**目录下的c.properties和d.properties配置文件
```java
@RequestMapping(value = "/selectByIdEncrypt", method = RequestMethod.POST)
@GetProperties(properties = {"d.properties", "c.properties"})
public MessageResult selectByIdEncrypt(){


    Baike baike = baikeService.selectByPrimaryKey(1L);

    Map cachemap = GetPropertiesListener.CACHEMAP;
    System.out.println(cachemap.get("d.spring.datasource.username"));

    return MessageResult.ok(baike);
}
```

获取配置信息：
例如我们在**aaaa/a.properties**配置文件中配置了如下信息
```
mybatis.type-aliases-package=com.yi.mybatis.model
```
那么我们只需要使用：路径+文件名+配置key即可获取
```
Object value = GetPropertiesListener.CACHEMAP.get("aaaa.a.mybatis.type-aliases-package");
```

例如我们在**d.properties**配置文件中配置了如下信息
```
mybatis.type-aliases-package=com.yi.mybatis.model
```
那么我们只需要使用：文件名+配置key即可获取
```
Object value = GetPropertiesListener.CACHEMAP.get("d.mybatis.type-aliases-package");
```

**注意：在包含文件夹的路径下不用在路径前面加上“/”，例如：**
```
正确：@GetProperties(properties = "aaaa/a.properties")
```
```
错误：@GetProperties(properties = "/aaaa/a.properties")
```

<div id="ZooLock"></div>

### @ZooLock 基于zookeeper的分布式锁
这个注解作用于方法，让方法在分布式系统中拥有分布式锁的能力，当然也可以用于单体服务当普通锁用（不推荐）。
使用也非常简单，我们来看一个最简单的例子，在写例子之前我们需要在application.properties配置文件中配置一些参数
```
# 基于zookeeper分布式锁配置
# 要使用分布式锁，必须设置为true
zk.lock.enabled=true
zk.lock.url=127.0.0.1:2181
# 重试次数，默认3
zk.lock.retry=3
# 超时时间(毫秒)
zk.lock.timeout=1000
```
配置完成我们来看看简单使用此注解的例子：
```java
/**
 * 通过设置分布式锁，使得方法具有分布式锁的能力
 * <p>分布式锁key：/DISTRIBUTED_LOCK_books</p>
 * @return
 * @throws InterruptedException
 */
@ZooLock(key = "books")
@RequestMapping(value = "/selectByExample", method = RequestMethod.POST)
public MessageResult selectByExample() throws InterruptedException {
    BaikeExample example = new BaikeExample();
    BaikeExample.Criteria criteria = example.createCriteria();

    criteria.andNameEqualTo("海明威");
    List<Baike> hBaikes = baikeService.selectByExample(example);
    List<Baike> allBooks = new ArrayList<>(hBaikes);

    Thread.sleep(6000);

    return MessageResult.ok(allBooks);
}
```
默认如果没有设置，锁释放时间，默认五秒，我们延时是六秒钟，此时如果有两个请求过来，我们就会发现一个返回结果，一个如下图所示
![](https://i.imgur.com/1rmzdpA.png)

接一下我们看一个比较复杂的例子，可以生成分布式锁动态key
```java
/**
 * 通过@LockKeyParam 实现分布式锁动态key，如果对象中的id和book分别为 1 和 全职法师
 * <p>分布式锁key：/DISTRIBUTED_LOCK_books/1/全职法师</p>
 * @param baike 前端传入对象参数
 * @return
 * @throws InterruptedException
 */
@ZooLock(key = "books", timeout = 3000L)
@RequestMapping(value = "/getBaikes", method = RequestMethod.POST)
public MessageResult getBaikes(@LockKeyParam(fields = {"id","book"}) Baike baike) throws InterruptedException {
    System.out.println("我进来啦！！！");
    Thread.sleep(5000);
    System.out.println("我出来啦！！！");

    return MessageResult.ok(baike);
}
```
在我们**Baike**的对象中包含id和book属性，通过@LockKeyParam注解就可以把这两个属性动态添加到key中，实现我们自定义key的操作。

当然我们使用@LockKeyParam注解不一定要在对象中，普通参数属性也可以，例如：
```java
/**
 * 通过@LockKeyParam 实现分布式锁动态key，如果参数name为 全职法师
 * <p>分布式锁key：/DISTRIBUTED_LOCK_books/全职法师</p>
 * @param name 前端书名参数
 * @return
 * @throws InterruptedException
 */
@ZooLock(key = "books", timeout = 3000L)
@RequestMapping(value = "/getBaikeName", method = RequestMethod.POST)
public MessageResult getBaikes(@LockKeyParam String name) throws InterruptedException {
    System.out.println("我进来啦！！！");
    Thread.sleep(5000);
    System.out.println("我出来啦！！！");

    return MessageResult.ok(name);
}
```
是不是很简单，赶紧去试试吧

### 问题建议

- 联系我的邮箱：ilovey_hwy@163.com
- 我的博客：https://www.hwy.ac.cn
- GitHub：https://github.com/HWYWL
