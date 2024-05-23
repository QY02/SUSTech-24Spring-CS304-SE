package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cs304.backend.entity.Favorite;
import org.cs304.backend.entity.UserFavoriteType;

import java.util.List;

public interface IUserFavoriteTypeService extends IService<UserFavoriteType> {
    JSONArray getAllType(String userId);

    JSONArray changeType(String userId, String newTypes);
}
