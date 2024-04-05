package org.cs304.backend.mapper;

import io.lettuce.core.dynamic.annotation.Param;
import org.cs304.backend.entity.Favorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface FavoriteMapper extends BaseMapper<Favorite> {
    void deleteByUserIdAndEventId(@Param("user_id") String userId, @Param("event_id") Integer eventId);
}
