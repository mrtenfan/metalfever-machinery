package ru.mrtenfan.metalfevermachinery.packages;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import ru.mrtenfan.metalfevermachinery.container.ContainerMetalProcessor;
import ru.mrtenfan.metalfevermachinery.crafting.MetalProcessorRecipe.MetalProcessorModes;
import ru.mrtenfan.metalfevermachinery.packages.PacketMetalProcessor.MetalProcessorHandler;
import ru.mrtenfan.metalfevermachinery.tileentity.TileEntityMetalProcessor;

public class PacketMetalProcessor implements IMessageHandler<MetalProcessorHandler, IMessage> {

	@Override
	public IMessage onMessage(MetalProcessorHandler message, MessageContext ctx) {
		EntityPlayer player = getPlayer(ctx);

		if(player.openContainer instanceof ContainerMetalProcessor) {
			ContainerMetalProcessor cmp = (ContainerMetalProcessor)player.openContainer;
			TileEntityMetalProcessor temp = cmp.getEntity();
			temp.setMode(message.mode);
		}
		return null;
	}
	
	public static class MetalProcessorHandler implements IMessage {
		
		public MetalProcessorModes mode;
		
		public MetalProcessorHandler() {}

		public MetalProcessorHandler(MetalProcessorModes mode) {
			this.mode = mode;
		}
		
		@Override
		public void fromBytes(ByteBuf buf) {
			mode = MetalProcessorModes.values()[buf.readInt()];
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(mode.ordinal());
		}
	}

	public EntityPlayer getPlayer(MessageContext context)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			return context.getServerHandler().playerEntity;
		}
		else {
			return Minecraft.getMinecraft().thePlayer;
		}
	}
}
