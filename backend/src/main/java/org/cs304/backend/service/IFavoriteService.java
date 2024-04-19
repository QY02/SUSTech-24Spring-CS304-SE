package org.cs304.backend.service;

import org.cs304.backend.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IFavoriteService extends IService<Favorite> {
    void deleteFavorite(Favorite favorite);
}
