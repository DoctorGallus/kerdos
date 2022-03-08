package doctorgallus.kerdos;

import java.util.Map;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;


public class FundData extends WorldSavedData
{
	public static final String DATA_NAME = "Kerdos";

	private Map<String, Integer> player_funds;

	public FundData()
	{
		super(DATA_NAME);
		player_funds = new HashMap();
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
		player_funds.clear();

		NBTTagCompound funds = nbt.getCompoundTag("funds");
		for (String username : funds.getKeySet())
		{
			player_funds.put(username, funds.getInteger(username));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound funds = new NBTTagCompound();
		player_funds.forEach((username, amount) -> {
			funds.setInteger(username, amount);
		});

		nbt.setTag("kerdos_funds", funds);
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
		return player_funds.getOrDefault(username, -1);
	}

	/**
	 * Set funds of a player
	 *
	 * @param username The players username
	 * @param funds The new amount of funds
	 */
	public void setFunds(String username, int funds)
	{
		player_funds.put(username, funds);
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

		if (current_funds < 0 && funds >= 0)
		{
			setFunds(username, funds);
			return true;
		}
		else if ((current_funds + funds) >= 0)
		{
			setFunds(username, current_funds + funds);
			return true;
		}

		return false;
	}
}

