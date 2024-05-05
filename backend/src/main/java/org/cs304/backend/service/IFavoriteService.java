package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONArray;
import org.cs304.backend.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IFavoriteService extends IService<Favorite> {
    void deleteFavorite(Favorite favorite);

    JSONArray getAllFavorite(String userId);
}
