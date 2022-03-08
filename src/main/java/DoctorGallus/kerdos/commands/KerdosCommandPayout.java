package doctorgallus.kerdos.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.ItemHandlerHelper;

import doctorgallus.kerdos.FundData;

public class KerdosCommandPayout extends CommandBase
{
	public static final String COMMAND_NAME = "payout";
	public static final String COMMAND_USAGE = "/kerdos payout <player>";


	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return COMMAND_NAME;
	}

	@Override
	public String getUsage(final ICommandSender sender)
	{
		return COMMAND_USAGE;
	}

	@Override
	public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException
	{
		if (args.length != 1)
		{
			sender.sendMessage(new TextComponentString(COMMAND_USAGE));
			return;
		}

		FundData fund_data = FundData.get(server.getEntityWorld());

		if (fund_data.getFunds(args[0]) < 0)
		{
			sender.sendMessage(new TextComponentString("Error: player \"" + args[0] + "\" has no funds!"));
			return;
		}

		boolean is_online = false;
		for (String name : server.getPlayerList().getOnlinePlayerNames())
		{
			if (args[0].equals(name))
			{
				is_online = true;
			}
		}

		if (!is_online)
		{
			sender.sendMessage(new TextComponentString("Error: player \"" + args[0] + "\" not online!"));
			return;
		}

		EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);
		List<ItemStack> payout = fund_data.payoutFunds(args[0]);
		for (ItemStack stack : payout)
		{
			ItemHandlerHelper.giveItemToPlayer(player, stack);
		}
	}
}

