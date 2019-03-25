package dataHandle.service.impl;

import dataHandle.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pojo.UserInfo;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Coffeeanice
 * @Description: TODO: 实现类
 * @Date: 2019/3/5 10:53
 */
@Component
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static UserInfoServiceImpl userInfoService;

    public UserInfoServiceImpl() {

    }

    @PostConstruct
    public void init() {
        userInfoService = this;
        userInfoService.jdbcTemplate = this.jdbcTemplate;
    }

    @Override
    public UserInfo findAccountByUserId(String userId) {
        UserInfo user = null;
        try {
            user = jdbcTemplate.queryForObject("SELECT tcu.`user_id`, tcu.`user_nick`, tcu.`user_avar`, tcu.`is_enter`, tcu.`state`, ts.`company_name`, ts.`seller_image`,ts.seller_name FROM tb_common_user tcu LEFT JOIN `tb_seller` ts ON tcu.`seller_id` = ts.`sellerId` WHERE tcu.`user_id` = ? ", new Object[]{userId}, (rs, rowNum) -> {
                UserInfo userInfo = new UserInfo();

                userCompent(rs, userInfo);

                System.out.println(userInfo);
                return userInfo;
            });
        } catch (RuntimeException e) {
            try {
                user = jdbcTemplate.queryForObject("SELECT tcu.`user_id`, tcu.`user_nick`, tcu.`user_avar`, tcu.`is_enter`, tcu.`state`, ts.`company_name`, ts.`seller_image`,ts.seller_name FROM tb_common_user tcu LEFT JOIN `tb_seller` ts ON tcu.`seller_id` = ts.`sellerId` WHERE tcu.seller_id = ? and is_enter = 1", new Object[]{userId}, (rs, rowNum) -> {
                    UserInfo userInfo = new UserInfo();
                    userCompent(rs, userInfo);

                    System.out.println(userInfo);
                    return userInfo;
                });
            }catch (RuntimeException re){
                re.getCause();
            }
        }
        return user;
    }

    private void userCompent(ResultSet rs, UserInfo userInfo) throws SQLException {
        userInfo.setUserId(rs.getString("user_id"));
        userInfo.setUserNick(rs.getString("user_nick"));
        userInfo.setUserAvar(rs.getString("user_avar"));
        userInfo.setIsEnter(rs.getInt("is_enter"));
        userInfo.setState(rs.getInt("state"));
        String company_name = rs.getString("company_name");
        String seller_image = rs.getString("seller_image");
        String seller_name = rs.getString("seller_name");
        if(StringUtils.isNotBlank(company_name)){
            userInfo.setCompanyName(company_name);
        }
        if(StringUtils.isNotBlank(seller_image)){
            userInfo.setSellerImg(seller_image);
        }
        if(StringUtils.isNotBlank(seller_image)){
            userInfo.setSellerName(seller_name);
        }
    }
}
