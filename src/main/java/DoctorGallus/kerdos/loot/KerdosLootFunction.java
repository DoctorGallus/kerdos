package doctorgallus.kerdos.loot;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.functions.LootFunction;

import doctorgallus.kerdos.Kerdos;
import doctorgallus.kerdos.FundData;


public class KerdosLootFunction extends LootFunction
{
	public static Logger logger = LogManager.getLogger(Kerdos.MODID);

	public KerdosLootFunction()
	{
		super(new LootCondition[0]);
	}

	@Override
	public ItemStack apply(ItemStack stack, java.util.Random rand, LootContext context)
	{
		if (context.getKillerPlayer() != null)
		{
			String username = context.getKillerPlayer().getName();
			ItemStack new_stack = FundData.get(context.getWorld()).getItemsFromPayout(username, stack.getItem());

			if (new_stack == null)
			{
				stack.setCount(0);
			}
			else
			{
				stack = new_stack;
			}
		}
		else
		{
			stack.setCount(0);
		}

		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<KerdosLootFunction>
	{
		public Serializer()
		{
			super(new ResourceLocation(Kerdos.MODID, "payout_lootfunction"), KerdosLootFunction.class);
		}

		@Override
		public void serialize(JsonObject object, KerdosLootFunction function, JsonSerializationContext context)
		{}

		@Override
		public KerdosLootFunction deserialize(JsonObject object, JsonDeserializationContext context, LootCondition[] conditions)
		{
			return new KerdosLootFunction();
		}
	}
}
