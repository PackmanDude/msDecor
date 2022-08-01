package com.github.MinersStudios.msDecor.crafts.home.plushes;

import com.github.MinersStudios.msDecor.Main;
import com.github.MinersStudios.msDecor.enums.CustomDecorMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import javax.annotation.Nonnull;

public class RacoonPlush {

	public static void addRecipes() {
		Bukkit.addRecipe(craftRacoonPlush());
	}

	@Nonnull
	public static ShapedRecipe craftRacoonPlush() {
		CustomDecorMaterial customDecorMaterial = CustomDecorMaterial.RACOON_PLUSH;
		return new ShapedRecipe(new NamespacedKey(Main.plugin, customDecorMaterial.name()), customDecorMaterial.getItemStack())
				.shape("WWW", "WWW")
				.setIngredient('W', Material.GRAY_WOOL);
	}
}
