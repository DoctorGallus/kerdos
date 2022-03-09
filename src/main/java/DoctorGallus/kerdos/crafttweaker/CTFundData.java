package doctorgallus.kerdos.crafttweaker;


import java.util.ArrayList;
import java.util.List;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.world.IWorld;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.mc1120.item.MCItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import doctorgallus.kerdos.FundData;


@ZenRegister
@ZenClass("mods.kerdos.FundData")
@ModOnly("crafttweaker")
public class CTFundData
{
	@ZenMethod
	public static int getFunds(IWorld world, String username)
	{
		ActionFundsGet action = new ActionFundsGet(world, username);
		CraftTweakerAPI.apply(action);
		return action.getResult();
	}

	@ZenMethod
	public static void setFunds(IWorld world, String username, int funds)
	{
		CraftTweakerAPI.apply(new ActionFundsSet(world, username, funds));
	}

	@ZenMethod
	public static boolean changeFunds(IWorld world, String username, int funds)
	{
		ActionFundsChange action = new ActionFundsChange(world, username, funds);
		CraftTweakerAPI.apply(action);
		return action.getResult();
	}

	@ZenMethod
	public static List<IItemStack> getPayout(IWorld world, String username)
	{
		ActionFundsPayout action = new ActionFundsPayout(world, username);
		CraftTweakerAPI.apply(action);
		return action.getResult();
	}

	private static class ActionFundsGet implements IAction
	{
		private IWorld world;
		private String username;
		private int result;

		public ActionFundsGet(IWorld world, String username)
		{
			this.world = world;
			this.username = username;
		}

		@Override
		public void apply()
		{
			result = FundData.get((World) world.getInternal()).getFunds(username);
		}

		@Override
		public String describe()
		{
			return "Get a players funds";
		}

		public int getResult()
		{
			return result;
		}
	}

	private static class ActionFundsSet implements IAction
	{
		private IWorld world;
		private String username;
		int funds;

		public ActionFundsSet(IWorld world, String username, int funds)
		{
			this.world = world;
			this.username = username;
			this.funds = funds;
		}

		@Override
		public void apply()
		{
			FundData.get((World) world.getInternal()).setFunds(username, funds);
		}

		@Override
		public String describe()
		{
			return "Set a players funds";
		}
	}

	private static class ActionFundsChange implements IAction
	{
		private IWorld world;
		private String username;
		int funds;
		boolean result;

		public ActionFundsChange(IWorld world, String username, int funds)
		{
			this.world = world;
			this.username = username;
			this.funds = funds;
		}

		@Override
		public void apply()
		{
			result = FundData.get((World) world.getInternal()).changeFunds(username, funds);
		}

		@Override
		public String describe()
		{
			return "Change a players funds";
		}

		public boolean getResult()
		{
			return result;
		}
	}

	private static class ActionFundsPayout implements IAction
	{
		private IWorld world;
		private String username;
		private List<ItemStack> result;

		public ActionFundsPayout(IWorld world, String username)
		{
			this.world = world;
			this.username = username;
		}

		@Override
		public void apply()
		{
			result = FundData.get((World) world.getInternal()).getPayout(username);
		}

		@Override
		public String describe()
		{
			return "Get a players payout";
		}

		public List<IItemStack> getResult()
		{
			ArrayList<IItemStack> list = new ArrayList();
			result.forEach((stack) -> {
				list.add(new MCItemStack(stack));
			});
			return list;
		}
	}
}

