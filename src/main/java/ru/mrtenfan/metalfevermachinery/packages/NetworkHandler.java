package ru.mrtenfan.metalfevermachinery.packages;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import ru.mrtenfan.metalfevermachinery.packages.PacketMetalProcessor.MetalProcessorHandler;

public class NetworkHandler {
    private short id;

    /**
     *  ChannelName - название канала. Канал должен быть один на весь мод, создавать дополнительные каналы не нужно!
     */
//    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("MFM");
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("MFM");

    public NetworkHandler() {
//    	register(new CPacketMetalHandler().getClass(), Side.SERVER);
    	NETWORK.registerMessage(PacketMetalProcessor.class, MetalProcessorHandler.class, id++, Side.SERVER);
    }

    /**
     * Получение дистанции от определённой точки в мире.
     * @param world - мир
     * @param updateDistance - радиус в котором будет действовать пакет
     * @return позиция
     */
//    public static void sendToAllAround(SimplePacket packet, World world, double x, double y, double z, double distance) {
//        NETWORK.sendToAllAround(packet, new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, distance));
//    }

    /**
     * Регистрация пакета через один метод.
     * @param packet - класс пакета
     * @param side - сторона (клиент/сервер)
     */
//    private void register(Class<? extends SimplePacket> packet, Side side) {
//        try {
//            NETWORK.registerMessage(packet.newInstance(), packet, id++, side);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//			e.printStackTrace();
//		}
//    }
}
