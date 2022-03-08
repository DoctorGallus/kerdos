package doctorgallus.kerdos.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import doctorgallus.kerdos.FundData;


public class KerdosCommandFundsGet extends CommandBase
{
	public static final String COMMAND_NAME = "get_funds";
	public static final String COMMAND_USAGE = "/kerdos get_funds <player>";


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
		if (args.length == 1)
		{
			int funds = FundData.get(server.getEntityWorld()).getFunds(args[0]);
			if (funds >= 0)
			{
				sender.sendMessage(new TextComponentString(Integer.toString(funds)));
			}
			else
			{
				sender.sendMessage(new TextComponentString("Error: player \"" + args[0] + "\" has no funds!"));
			}
		}
		else
		{
			sender.sendMessage(new TextComponentString(COMMAND_USAGE));
		}
	}
}

