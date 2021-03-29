package com.xiaoma.im.service;

import com.xiaoma.im.entity.RedPackage;
import com.xiaoma.im.entity.UserMoney;

import java.math.BigDecimal;

public interface PackageService {


    boolean updateMoney(UserMoney userMoney, BigDecimal bigDecimal);

    boolean generatorSinglePackage(RedPackage redPackage, Integer id);

    boolean generatorGroupPackage(RedPackage redPackage, Integer id);

    Integer verificationData(UserMoney userMoney, RedPackage redPackage);

    boolean getSinglePackage(String redPackageId, String account);

    boolean getGroupPackage(String redPackageId, String account);
}
