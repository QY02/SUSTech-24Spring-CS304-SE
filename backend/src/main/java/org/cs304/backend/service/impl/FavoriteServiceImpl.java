package org.cs304.backend.service.impl;

import org.cs304.backend.entity.Favorite;
import org.cs304.backend.mapper.FavoriteMapper;
import org.cs304.backend.service.IFavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {

}
