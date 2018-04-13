package cam72cam.betterwarehouse.proxy;

import cam72cam.betterwarehouse.BetterWarehouse;
import cam72cam.betterwarehouse.gui.ShelvingContainer;
import cam72cam.betterwarehouse.gui.ShelvingContainerGui;
import cam72cam.betterwarehouse.render.ShelvingBlockRender;
import cam72cam.betterwarehouse.render.ShelvingTileRender;
import cam72cam.betterwarehouse.tile.ShelvingTile;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = BetterWarehouse.MODID, value=Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(ShelvingTile.class, new ShelvingTileRender());
	}
	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		event.getModelRegistry().putObject(new ModelResourceLocation(BetterWarehouse.SHELVING_BLOCK.getRegistryName(), ""), new ShelvingBlockRender());
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ShelvingTile shelving = ShelvingTile.get(world, new BlockPos(x, y, z));
		if (shelving != null && shelving.isLoaded()) {
			return new ShelvingContainerGui(new ShelvingContainer(player.inventory, shelving));
		}
		return null;
	}
}
