# ant-nest
蚂蚁巢,专注于方便的注解

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu) [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE) [![Maven Central](https://img.shields.io/badge/ant--nest-1.0.7--RELEASE-ff69b4.svg)](https://search.maven.org/artifact/com.github.hwywl/ant-nest/1.0.7-RELEASE/jar)
### 说明
我们通过注解可以干很多的事，也很方便，大大提高我的开发效率，至此开发常用的注解方便我们使用。

### 版本
现在最新的版本：
```xml
<dependency>
  <groupId>com.github.hwywl</groupId>
  <artifactId>ant-nest</artifactId>
  <version>1.0.7-RELEASE</version>
</dependency>
```

### @WebLog 请求日志拦截
我们只需要在controller接口中加入注解即可
![](https://i.imgur.com/uanFdPX.png)

加入注解之后会拦截请求的日志，日志被安排的整整齐齐，方便我们后端人员的调试工作。
![](https://i.imgur.com/9Zmeyuu.png)

### @SqlStatement mybatis SQL拦截
我们只需要在我们操作的接口实现中加入注解即可
![](https://i.imgur.com/3PR2m1J.png)

加入注解之后会拦截mybatis的SQL，日志被安排的整整齐齐，方便我们的调试查看和测试SQL性能。
![](https://i.imgur.com/3YNBBZs.png)

**注意：此注解不能在接口上使用，只能在方法上**

### 问题建议

- 联系我的邮箱：ilovey_hwy@163.com
- 我的博客：https://www.hwy.ac.cn
- GitHub：https://github.com/HWYWL
