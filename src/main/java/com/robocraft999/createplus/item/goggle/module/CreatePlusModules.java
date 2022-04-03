package com.robocraft999.createplus.item.goggle.module;

/*import mekanism.api.text.IHasTranslationKey;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.Modules;
import mekanism.common.content.gear.Modules.ModuleData;*/

@SuppressWarnings("unused")
public class CreatePlusModules{
	/*
	private CreatePlusModules() {
	}
	
	private static final Map<String, ModuleData<?>> MODULES = new Object2ObjectOpenHashMap<>();
    private static final Map<Item, Set<ModuleData<?>>> SUPPORTED_MODULES = new Object2ObjectOpenHashMap<>();
    private static final Map<ModuleData<?>, Set<Item>> SUPPORTED_CONTAINERS = new Object2ObjectOpenHashMap<>();
	
	public static final ModuleData<ModuleEngineersGogglesUnit> GOGGLE_UNIT = register("goggle_unit",
			MekanismLang.MODULE_ELECTROLYTIC_BREATHING_UNIT,MekanismLang.DESCRIPTION_ELECTROLYTIC_BREATHING_UNIT, ModuleEngineersGogglesUnit::new,4
			).rarity(Rarity.UNCOMMON);

	
	private static <M extends Module> CPModuleData<M> register(String name, ILangEntry langEntry, ILangEntry description, Supplier<M> moduleSupplier) {
        return register(name, langEntry, description, moduleSupplier, 1);
    }

    private static <M extends Module> CPModuleData<M> register(String name, ILangEntry langEntry, ILangEntry description, Supplier<M> moduleSupplier, int maxStackSize) {
        CPModuleData<M> data = new CPModuleData<>(name, langEntry, description, moduleSupplier, maxStackSize);
        MODULES.put(name, data);
        return data;
    }
    
    public static class CPModuleData<MODULE extends Module> implements IHasTranslationKey {

        private final String name;
        private final ILangEntry langEntry;
        private final ILangEntry description;
        private final Supplier<MODULE> supplier;
        private final int maxStackSize;
        private ItemStack stack;
        private Rarity rarity = Rarity.COMMON;

        /** Exclusive modules only work one-at-a-time; when one is enabled, others will be automatically disabled. */
	
		/*
        private boolean exclusive;
        private boolean handlesModeChange;
        private boolean rendersHUD;
        private boolean noDisable;
        private boolean disabledByDefault;

        private CPModuleData(String name, ILangEntry langEntry, ILangEntry description, Supplier<MODULE> supplier, int maxStackSize) {
            this.name = name;
            this.langEntry = langEntry;
            this.description = description;
            this.supplier = supplier;
            this.maxStackSize = maxStackSize;
        }

        public int getMaxStackSize() {
            return maxStackSize;
        }

        public Rarity getRarity() {
            return rarity;
        }

        public CPModuleData<MODULE> rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public MODULE get(ItemStack container) {
            MODULE module = supplier.get();
            module.init(this, container);
            return module;
        }

        public void setStack(Item item) {
            this.stack = new ItemStack(item);
        }

        public ItemStack getStack() {
            return stack;
        }

        public String getName() {
            return name;
        }

        public ITextComponent getDescription() {
            return new TranslationTextComponent(description.getTranslationKey());
        }

        public ILangEntry getLangEntry() {
            return langEntry;
        }

        public ModuleData<MODULE> setExclusive() {
            exclusive = true;
            return this;
        }

        public boolean isExclusive() {
            return exclusive;
        }

        public ModuleData<MODULE> setHandlesModeChange() {
            handlesModeChange = true;
            return this;
        }

        public boolean handlesModeChange() {
            return handlesModeChange;
        }

        public ModuleData<MODULE> setRendersHUD() {
            rendersHUD = true;
            return this;
        }

        public boolean rendersHUD() {
            return rendersHUD;
        }

        public ModuleData<MODULE> setNoDisable() {
            noDisable = true;
            return this;
        }

        public boolean isNoDisable() {
            return noDisable;
        }

        public ModuleData<MODULE> setDisabledByDefault() {
            disabledByDefault = true;
            return this;
        }

        public boolean isDisabledByDefault() {
            return disabledByDefault;
        }

        @Override
        public String getTranslationKey() {
            return langEntry.getTranslationKey();
        }
    }
    
    
    
  */  
    
}
