package org.cs304.backend.service.impl;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.cs304.backend.entity.Favorite;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.FavoriteMapper;
import org.cs304.backend.service.IFavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;
    @Override
    public void deleteFavorite(@NotNull Favorite favorite) {
        if (favorite.getEventId() == null || favorite.getUserId()==null) {
            throw new ServiceException("400", "Invalid data");
        }
        favoriteMapper.deleteByUserIdAndEventId(favorite.getUserId(), favorite.getEventId());
    }
}
