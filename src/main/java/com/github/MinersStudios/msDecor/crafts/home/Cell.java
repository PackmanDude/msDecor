package com.github.MinersStudios.msDecor.crafts.home;

import com.github.MinersStudios.msDecor.Main;
import com.github.MinersStudios.msDecor.enums.CustomDecorMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import javax.annotation.Nonnull;

public class Cell {

	public static void addRecipes() {
		Bukkit.addRecipe(craftCell());
	}

	@Nonnull
	public static ShapedRecipe craftCell() {
		CustomDecorMaterial customDecorMaterial = CustomDecorMaterial.CELL;
		return new ShapedRecipe(new NamespacedKey(Main.plugin, customDecorMaterial.name()), customDecorMaterial.getItemStack())
				.shape(
						" C ",
						"BBB",
						"BBB"
				).setIngredient('B', Material.IRON_BARS)
				.setIngredient('C', Material.CHAIN);
	}
}