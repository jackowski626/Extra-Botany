package com.meteor.extrabotany.common.integration.crafttweaker;

import java.util.LinkedList;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.meteor.extrabotany.ExtraBotany;
import com.meteor.extrabotany.api.ExtraBotanyAPI;
import com.meteor.extrabotany.common.crafting.recipe.RecipeStonesia;
import com.meteor.extrabotany.common.lib.Reference;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.extrabotany.Stonesia")
@ModOnly(Reference.MOD_ID)
@ZenRegister
public class Stonesia {
	
	 	@ZenMethod
	    public static void add(int output, IItemStack input) {
	 		ExtraBotany.LATE_ADDITIONS.add(new AddShaped(output, input));
	    }
	    
	    @ZenMethod
	    public static void remove(IItemStack input) {
	    	ExtraBotany.LATE_REMOVALS.add(new RemoveShaped(input));
	    }

	    public static class AddShaped extends BaseAction {
	        
	        private final int output;
	        private final IItemStack input;
	        
	        public AddShaped(int output, IItemStack input) {
	            super("Add Stonesia Recipe");
	            this.output = output;
	            this.input = input;
	        }
	        
	        @Override
	        public void apply() {
	            ExtraBotanyAPI.registerStonesiaRecipe(output, InputHelper.toStack(input));
	        }
	        
	        @Override
	        protected String getRecipeInfo() {
	            return input.getDisplayName();
	        }
	    }
	    
	    public static class RemoveShaped extends BaseListRemoval<RecipeStonesia> {
	        
	        private final IItemStack input;
	        
	        protected RemoveShaped(IItemStack input) {
	            super("Remove Stonesia Recipe", ExtraBotanyAPI.stonesiaRecipes);
	            this.input = input;
	        }
	        
	        @Override
	        public void apply() {
	        	LinkedList<RecipeStonesia> recipes = new LinkedList<>();
	            
	            for(RecipeStonesia entry : ExtraBotanyAPI.stonesiaRecipes) {
	                if(entry != null && entry.getInput() != null) {
	                	if(entry.getInput() instanceof ItemStack && StackHelper.matches(input, InputHelper.toIItemStack((ItemStack)entry.getInput())))
	                		recipes.add(entry);
	                }
	            }
	            
	            // Check if we found the recipes and apply the action
	            if(!recipes.isEmpty()) {
	                this.recipes.addAll(recipes);
	                super.apply();
	            }
	            CraftTweakerAPI.getLogger().logInfo(super.describe());
	        }
	        
			@Override
			protected String getRecipeInfo(RecipeStonesia arg0) {
				return input.getDisplayName();
			}
	    }

}
