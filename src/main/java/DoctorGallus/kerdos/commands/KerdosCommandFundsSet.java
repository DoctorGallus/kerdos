package doctorgallus.kerdos.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import doctorgallus.kerdos.FundData;


public class KerdosCommandFundsSet extends CommandBase
{
	public static final String COMMAND_NAME = "set_funds";
	public static final String COMMAND_USAGE = "/kerdos set_funds <player> <amount>";


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
		if (args.length != 2)
		{
			sender.sendMessage(new TextComponentString(COMMAND_USAGE));
			return;
		}

		int amount;
		try
		{
			amount = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e)
		{
			amount = -1;
		}
		if (amount < 0)
		{
			sender.sendMessage(new TextComponentString("Error: invalid amount: " + args[1]));
			return;
		}

		FundData fund_data = FundData.get(server.getEntityWorld());
		int funds = fund_data.getFunds(args[0]);

		if (funds < 0)
		{
			sender.sendMessage(new TextComponentString("Error: player \"" + args[0] + "\" has no funds!"));
		}
		else
		{
			fund_data.setFunds(args[0], amount);
		}
	}
}

