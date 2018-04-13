package cam72cam.betterwarehouse.render;

import java.util.ArrayList;
import java.util.List;

import cam72cam.betterwarehouse.block.ShelvingBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ShelvingBlockRender implements IBakedModel {

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		if (state instanceof IExtendedBlockState) {
			IBlockState inner_state = ((IExtendedBlockState)state).getValue(ShelvingBlock.STATE);
			IBlockState adtl = ((IExtendedBlockState)state).getValue(ShelvingBlock.ADTL_STATE);
			if (inner_state != null && inner_state.getBlock() != Blocks.AIR) {
				quads.addAll(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(inner_state).getQuads(inner_state, side, rand));
				if (adtl != null && adtl.getBlock() != Blocks.AIR) {
					quads.addAll(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(adtl).getQuads(adtl, side, rand));
				}
			}
		}
		return quads;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.WOODEN_SLAB.getDefaultState()).getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

}
