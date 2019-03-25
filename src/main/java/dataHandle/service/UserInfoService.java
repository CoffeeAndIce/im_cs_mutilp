package dataHandle.service;

import pojo.UserInfo;

public interface UserInfoService {

    /**
    *  @Author:      Coffeeanice
    * @Description:   TODO: 通过userId获取用户信息
    * @Date:     2019/2/27 14:31
    */
    UserInfo findAccountByUserId(String userId);

}
