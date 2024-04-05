package org.cs304.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.entity.Favorite;
import org.cs304.backend.service.IFavoriteService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    private IFavoriteService favoriteService;

    @PostMapping("/isFavorite")
    @Operation(summary = "判断是否存在",description = "")
    public Result isFavouriteExists(HttpServletResponse response, Favorite favorite) {
        if (favoriteService.getOne(new QueryWrapper<Favorite>().eq("user_id", favorite.getUserId()).eq("event_id",favorite.getEventId()))==null) {
            return Result.success(response, 1); // 返回数字 1 代表存在
        }
        return Result.success(response, 0); // 返回数字 0 代表不存在
    }


    @PostMapping("/add")
    @Operation(summary = "添加收藏",description = "")
    public Result addFavorite(HttpServletResponse response, @RequestParam Favorite favorite) {
        favoriteService.save(favorite);
        return Result.success(response);
    }

    @GetMapping("/delete")
    @Operation(summary = "删除喜欢",description = "")
    public Result delete(HttpServletResponse response, @RequestParam Favorite favorite) {
        favoriteService.deleteFavorite(favorite);
        return Result.success(response);
    }

}
