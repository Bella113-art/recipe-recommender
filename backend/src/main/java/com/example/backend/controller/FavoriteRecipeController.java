package com.example.backend.controller;

import com.example.backend.entity.FavoriteRecipe;
import com.example.backend.service.FavoriteRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteRecipeController {
    @Autowired
    private FavoriteRecipeService service;

    // 레시피 즐겨찾기 추가 (Spoonacular API 데이터 저장)
    @PostMapping
    public FavoriteRecipe addFavorite(@RequestBody FavoriteRecipe recipe) {
        return service.addFavoriteRecipe(recipe);
    }

    // 저장된 즐겨찾기 레시피 목록 조회
    @GetMapping
    public List<FavoriteRecipe> getAllFavorites() {
        return service.getAllFavoriteRecipes();
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{id}")
    public String deleteFavorite(@PathVariable Long id) {
        service.deleteFavoriteRecipe(id);
        return "Deleted successfully!";
    }
}
