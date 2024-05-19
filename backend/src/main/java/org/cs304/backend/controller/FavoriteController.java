package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.entity.Favorite;
import org.cs304.backend.service.IFavoriteService;
import org.cs304.backend.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Resource
    private IFavoriteService favoriteService;

    @PostMapping("/isFavorite")
    @Operation(summary = "判断是否存在", description = "")
    public Result isFavouriteExists(HttpServletResponse response, @RequestBody Favorite favorite) {
        if (favoriteService.getOne(new QueryWrapper<Favorite>().eq("user_id", favorite.getUserId()).eq("event_id", favorite.getEventId())) != null) {
            return Result.success(response, 1); // 返回数字 1 代表存在
        }
        return Result.success(response, 0); // 返回数字 0 代表不存在
    }


    @PostMapping("/add")
    @Operation(summary = "添加收藏", description = "")
    public Result addFavorite(HttpServletResponse response, @RequestBody Favorite favorite) {
//        if (favoriteService.getOne(new QueryWrapper<Favorite>().eq("user_id", favorite.getUserId()).eq("event_id", favorite.getEventId())) != null) {
//            return Result.error(response, "400", "你已经收藏过此活动了"); //  代表存在
//        }
        favoriteService.save(favorite);
        return Result.success(response);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除喜欢", description = "")
    public Result delete(HttpServletResponse response, @RequestBody Favorite favorite) {
        favoriteService.deleteFavorite(favorite);
        return Result.success(response);
    }

    @PostMapping("/getByUserId")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "userId": "12110141"
            }""")))
    public Result getAllFavorite(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        String userId = requestBody.getString("userId");
        return Result.success(response, favoriteService.getAllFavorite(userId));
    }
}
