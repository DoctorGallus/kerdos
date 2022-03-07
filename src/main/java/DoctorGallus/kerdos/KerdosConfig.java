package doctorgallus.kerdos;


import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import doctorgallus.kerdos.Kerdos;


@Config(modid = Kerdos.MODID, category = "")
public class KerdosConfig
{
	public static final GeneralConfig general = new GeneralConfig();

	public static class GeneralConfig
	{
		@Comment({"Minimum percentage of funds to be released on payout"})
		@Config.RangeDouble(min = 0D, max = 1D)
		public double payoutRateMin = 0.3D;

		@Comment({"Maximum percentage of funds to be released on payout"})
		@Config.RangeDouble(min = 0D, max = 1D)
		public double payoutRateMax = 0.5D;

		@Comment({
			"Currency items used for payout",
			"Syntax: modid:itemname@value",
			"Syntax: modid:itemname:meta@value",
			"where \"value\" is the monetary value of that item"
		})
		public String[] currencyItems = {
			"jjcoin:copper_coin@1",
			"jjcoin:silver_coin@10",
			"jjcoin:gold_coin@100",
			"jjcoin:diamond_coin@1000"
		};

		@Comment({"Loot tables to use for payout"})
		public String[] payoutLootTables = {
			"minecraft:chests/simple_dungeon",
			"minecraft:entities/skeleton",
			"minecraft:entities/zombie"
		};

		@Comment({
			"Blocks which increase your fund when harvested",
			"Syntax: modid:itemname@value",
			"Syntax: modid:itemname:meta@value",
			"where \"value\" is the value by which a players fund should be increased"
		})
		public String[] fundSourceBlocks = {
			"minecraft:diamond_ore@10",
			"biomesoplenty:gem_ore:*@5"
		};

		@Comment({
			"If enabled, Fortune enchantments also apply to fund increases when harvesting a block",
			"Uses the formula `base_value * (1 / (fortune_lvl + 2) + (fortune_lvl + 1) / 2)` which corresponds to the average drops increase for ores in vanilla minecraft"
		})
		public boolean applyFortuneEnchantment = true;
	}

	public static class Handler
	{
		@SubscribeEvent
		public void onConfigChangedEvent(OnConfigChangedEvent event)
		{
			if (event.getModID().equals(Kerdos.MODID))
			{
				ConfigManager.sync(Kerdos.MODID, Config.Type.INSTANCE);
			}
		}
	}
}

