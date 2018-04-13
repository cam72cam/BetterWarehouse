package cam72cam.betterwarehouse.render;

import org.lwjgl.opengl.GL11;

import cam72cam.betterwarehouse.tile.ShelvingTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.items.ItemStackHandler;

public class ShelvingTileRender extends TileEntitySpecialRenderer<ShelvingTile> {
	public void render(ShelvingTile te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.isOrigin()) {
			ItemStackHandler container = te.getContainer();
            RenderHelper.enableStandardItemLighting();
            GL11.glPushMatrix();
            {
            	GL11.glTranslated(x+0.5, y+0.5, z+0.5);
            	
				double scale = 0.4;
				GL11.glScaled(scale, scale, scale);
				GL11.glTranslated(0.5, 0.5, 0.5);
				
				double width = te.getSize() * ShelvingTile.INV_WIDTH_MULT;
				double depth = ShelvingTile.INV_ROWS;
				GL11.glTranslated(-width/2, 0, -depth/2);
	            for (int i = 0; i < container.getSlots(); i++) {
	            	GL11.glPushMatrix();
	            	{
		            	GL11.glTranslated(i % (width), 0, Math.floor(i / width));
		            	Minecraft.getMinecraft().getRenderItem().renderItem(container.getStackInSlot(i), ItemCameraTransforms.TransformType.NONE);
		            }
	            	GL11.glPopMatrix();
	            }
            }
            GL11.glPopMatrix();
		}
	}
}
