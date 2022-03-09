package doctorgallus.kerdos;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;


public class FundData extends WorldSavedData
{
	public static final String DATA_NAME = "Kerdos";
	public static Logger logger = LogManager.getLogger(Kerdos.MODID);

	private Map<String, Integer> playerFunds = new HashMap();
	private List<ItemStack> preparedPayout;

	public FundData()
	{
		super(DATA_NAME);
	}

	public FundData(String str)
	{
		super(str);
	}

	public static FundData get(World world)
	{
		MapStorage storage = world.getMapStorage();
		FundData instance = (FundData) storage.getOrLoadData(FundData.class, DATA_NAME);

		if (instance == null)
		{
			instance = new FundData();
			storage.setData(DATA_NAME, instance);
		}

		return instance;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		playerFunds.clear();

		for (String key : nbt.getKeySet())
		{
		}
		NBTTagCompound funds = nbt.getCompoundTag("funds");
		for (String username : funds.getKeySet())
		{
			playerFunds.put(username, funds.getInteger(username));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound funds = new NBTTagCompound();
		playerFunds.forEach((username, amount) -> {
			funds.setInteger(username, amount);
		});

		nbt.setTag("funds", funds);
		return nbt;
	}

	/**
	 * Get current funds of a player
	 *
	 * @param username The players username
	 * @return The players funds
	 */
	public int getFunds(String username)
	{
		return playerFunds.getOrDefault(username, -1);
	}

	/**
	 * Set funds of a player
	 *
	 * @param username The players username
	 * @param funds The new amount of funds
	 */
	public void setFunds(String username, int funds)
	{
		playerFunds.put(username, funds);
		markDirty();
	}

	/**
	 * Change funds of a player
	 *
	 * Funds can be increased by passing a positive value for `funds`, and decreased by passing a negative value.
	 * A players funds can't go below 0, so a decrease in funds is only applied if the player has sufficient funds.
	 *
	 * @param username The players username
	 * @param funds The change in funds
	 * @return true if successsful, otherwise false
	 */
	public boolean changeFunds(String username, int funds)
	{
		int current_funds = getFunds(username);

		if (current_funds >= 0 && (current_funds + funds) >= 0)
		{
			setFunds(username, current_funds + funds);
			return true;
		}

		return false;
	}

	private List<ItemStack> generatePayout(String username)
	{
		ArrayList<ItemStack> payout = new ArrayList();

		int current_funds = getFunds(username);
		int payout_amount = (int) Math.round(Math.floor((double) current_funds * (KerdosConfig.general.payoutRateMin + Math.random() * (KerdosConfig.general.payoutRateMax - KerdosConfig.general.payoutRateMin))));
		if (payout_amount > current_funds)
		{
			payout_amount = current_funds;
		}

		boolean successful = changeFunds(username, 0 - payout_amount);
		if(!successful)
		{
			return payout;
		}


		Map.Entry<Integer, ItemStack> current_entry = KerdosConfig.Handler.currencyItems.lastEntry();
		while (current_entry != null)
		{
			if (payout_amount >= current_entry.getKey())
			{
				ItemStack payout_stack = current_entry.getValue().copy();
				payout_stack.setCount(payout_amount / current_entry.getKey());
				payout_amount %= current_entry.getKey();
				payout.add(payout_stack);
			}

			current_entry = KerdosConfig.Handler.currencyItems.lowerEntry(current_entry.getKey());
		}

		return payout;
	}

	/**
	 * Get a payout for a player
	 *
	 * @param username The players username
	 * @return The payout
	 */
	public List<ItemStack> getPayout(String username)
	{
		if (preparedPayout == null)
		{
			preparedPayout = generatePayout(username);
		}

		List<ItemStack> payout = preparedPayout;
		preparedPayout = null;

		return payout;
	}

	/**
	 * Get a single itemstack of the payout for a player
	 *
	 * @param username The players username
	 * @param item The item for which a stack should be returned
	 * @return a ItemStack, or `null` if the item isn't part of the payout
	 */
	public ItemStack getItemsFromPayout(String username, Item item)
	{
		if (preparedPayout == null)
		{
			preparedPayout = generatePayout(username);
		}

		ItemStack stack = null;
		for (int i = 0, n = preparedPayout.size(); i < n; ++i)
		{
			if (Item.getIdFromItem(preparedPayout.get(i).getItem()) == Item.getIdFromItem(item))
			{
				stack = preparedPayout.remove(i);
				break;
			}
		}

		if (preparedPayout.size() < 1)
		{
			preparedPayout = null;
		}

		return stack;
	}
}

