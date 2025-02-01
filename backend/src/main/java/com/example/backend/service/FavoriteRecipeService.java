package com.example.backend.service;

import com.example.backend.entity.FavoriteRecipe;
import com.example.backend.repository.FavoriteRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteRecipeService {
    @Autowired
    private FavoriteRecipeRepository repository;

    public FavoriteRecipe addFavoriteRecipe(FavoriteRecipe recipe) {
        return repository.save(recipe);
    }

    public List<FavoriteRecipe> getAllFavoriteRecipes() {
        return repository.findAll();
    }

    public void deleteFavoriteRecipe(Long id) {
        repository.deleteById(id);
    }
}
