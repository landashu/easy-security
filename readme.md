# easy-security
easy-security从名字上可以看出是一款简单的安全认证框架，思想就是希望通过简单的配置，并且实现核心的方法就达到认证和鉴权的目的。

easy-security 不限制存储token方式，无论是保存到服务端还是使用JWT等都可以，因为这部分是由开发者自己来定义的，只需要告诉
easy-security 该如何获取用户信息即可。

如果你使用了 easy-security 自身所带的 RequestData 请求封装，那么所有的接口请求均以POST方式。

easy-security 结合了Yapi的使用，如果你使用Yapi需要在自己的项目中描述规则
 
### 功能列举
* 认证拦截
* 权限校验
* 用户获取
* 密文传输(内置AES加密算法)

### 使用
1. 配置
```
easy
  security:
    # 开启认证
    auth-enable: true
    # 开启鉴权
    authorize-enable: true
    # 开启只有装成RequestData请求类
    request-data-enable: true
    # 项目路径，不会被认证
    project-url: 
      - "/sysUser/login"
      - "/goods/getIndex"
      - "/goods/getGoodsInfo"
    # 需要解密的路径
    decrypt-url: ""
    # 特殊路径，不受认证鉴权以及RequestData的影响
    special-url: "/oss/**"
```

2. 实现获取方式
```
@Service
@Slf4j
public class AuthConfig implements EasySecurityServer {
    
    // 描述用户获取的方式，可以用token从redis获取，自己实现，也可以是JWT自己解析
    @Override
    public Object getAuthUser(String token) {
        return null;
    }

    // 描述用户更获取权限集，可以用token从redis获取，自己实现
    // 也可以使用JWT自己解析数据
    @Override
    public List<String> getAuthorizeUrl(String token) {
        return null;
    }

}
```
3. 获取用户
```
// RequestData<T,U> 第一个参数为前端所传参数，第二个为后端会获取到的用户数据
@PostMapping("/login")
public ResponseData<SysUserVO> Login(@RequestBody RequestData<SysUserLoginDTO, SysUserVO> request) {
    return sysUserService.Login(request);
}

requestData.getData() // 获取前端传参
requestData.getUser() // 获取操作用户
```

### Yapi规则描述
在项目中的 application.yml 文件里面添加如下：
```
field:
  required: "@com.wt.security.annotation.yapi.YApiRule#required"
  default:
    value: "#default"
json:
  rule:
    field:
      ignore: "@com.wt.security.annotation.yapi.YApiRule#hide"

method:
  additional:
    header[!@com.wt.security.annotation.yapi.YApiRule]: '{name: "token",value: "fds435434322543dfsgfds4535435432543",required: true}'

```
yapi 官方文档 https://hellosean1025.github.io/