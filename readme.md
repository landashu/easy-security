# easy-security
easy-security从名字上可以看出是一款简单的安全认证框架，核心思想就是希望通过简单的配置，并且实现核心的方法就达到认证和鉴权的目的。
easy-security不止做了简单的认证和鉴权，还做到了帮助获取当前操作用，只需要知道token，并提供获取方式，在每次请求的时候便可以获取到。

### 使用
1. 配置
```
security:
  # 开启认证
  auth-enable: true
  # 开启鉴权
  authorize-enable: true
  # 开启只有装成RequestData请求类
  request-data-enable: true
  # token的前缀
  auth-key: "LUCKY:AUTH:"
  # 鉴权的前缀
  authorize-key: "LUCKY:AUTHOR:"
  # 项目路径，不会被认证
  project-url: "/sysUser/login,/goods/getIndex,/goods/getGoodsInfo"
  # 解密路径
  decrypt-url: ""
  # 特殊路径，不受认证鉴权以及RequestData的影响
  special-url: "/oss/**"
```
2. 实现获取方式
```
@Service
@Slf4j
public class AuthConfig implements AuthSecurity, AuthorizeSecurity {
    
    // 描述用户获取的方式，可以用token从redis获取，自己实现
    @Override
    public Object getAuthUser(String token) {
        return null;
    }

    // 描述用户更新认证（续期），可以用token从redis获取，自己实现
    @Override
    public void renewAuth(String token) {

    }

    // 描述用户更获取权限集，可以用token从redis获取，自己实现
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
public ResponseData<SysUserVO> Login(@RequestBody RequestData<SysUserLoginDTO, SysUserVO> requestData, HttpServletRequest req) {
    return sysUserService.Login(requestData,req);
}

requestData.getData() // 获取前端传参
requestData.getUser() // 获取操作用户
```