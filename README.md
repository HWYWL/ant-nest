# ant-nest
蚂蚁巢,专注于方便的注解

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu) [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE) [![Maven Central](https://img.shields.io/badge/ant--nest-1.0.9--RELEASE-ff69b4.svg)](https://search.maven.org/artifact/com.github.hwywl/ant-nest/1.0.9-RELEASE/jar)
### 说明
我们通过注解可以干很多的事，也很方便，大大提高我的开发效率，至此开发常用的注解方便我们使用。

### 版本
现在最新的版本：
```xml
<dependency>
  <groupId>com.github.hwywl</groupId>
  <artifactId>ant-nest</artifactId>
  <version>1.0.9-RELEASE</version>
</dependency>
```

**注解描述：**
[@WebLog 请求日志拦截](#WebLog)

[@SqlStatement mybatis SQL拦截](#SqlStatement)

[@OperatingTime 方法耗时计算](#OperatingTime)

[@AESEncryptBody @DESEncryptBody接口返回数据加密](#Encrypt)


### <span id="WebLog">@WebLog 请求日志拦截</span>
我们只需要在controller接口中加入注解即可
![](https://i.imgur.com/uanFdPX.png)

加入注解之后会拦截请求的日志，日志被安排的整整齐齐，方便我们后端人员的调试工作。
![](https://i.imgur.com/9Zmeyuu.png)

### <span id="SqlStatement">@SqlStatement mybatis SQL拦截</span>
我们只需要在我们操作的接口实现中加入注解即可
![](https://i.imgur.com/3PR2m1J.png)

加入注解之后会拦截mybatis的SQL，日志被安排的整整齐齐，方便我们的调试查看和测试SQL性能。
![](https://i.imgur.com/3YNBBZs.png)

**注意：此注解不能在接口上使用，只能在方法上**

### <span id="OperatingTime">@OperatingTime 方法耗时计算</span>
我们只需要在我们操作的接口/方法实现中加入注解即可
![](https://i.imgur.com/0ciWYkr.png)

加入注解之后我们就能看到方法耗时，方便我们的测试方法编写的性能。
![](https://i.imgur.com/N0hSuUH.png)

### <span id="Encrypt">@AESEncryptBody & @DESEncryptBody 接口返回数据加密</span>
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


- AES加密方式：加密模式:ECB、填充:pkcs5padding、数据块:128位、密码: 配置文件中的key、偏移量: ECB模式不需要、输出:hex、字符集：UTF-8

- DES加密方式：加密模式:ECB、填充:pkcs5padding、数据块:128位、密码: 配置文件中的key、偏移量: ECB模式不需要、输出:hex、字符集：UTF-8

返回的数据可以在这个网站上解密测试：http://tool.chacuo.net/cryptaes
![](https://i.imgur.com/6RQTVG8.png)

### 问题建议

- 联系我的邮箱：ilovey_hwy@163.com
- 我的博客：https://www.hwy.ac.cn
- GitHub：https://github.com/HWYWL
