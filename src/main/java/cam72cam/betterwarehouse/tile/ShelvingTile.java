package cam72cam.betterwarehouse.tile;

import cam72cam.betterwarehouse.BetterWarehouse;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ShelvingTile extends TileEntity {
	public static ShelvingTile get(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof ShelvingTile) {
			return (ShelvingTile) te;
		}
		return null;
	}
	
	public static final int INV_ROWS = 6;
	public static final int INV_WIDTH_MULT = 2;

	private int size;
	private BlockPos center;
	private BlockPos offset;
	private IBlockState state;
	private boolean isLoaded = false;
	

	private ItemStackHandler container = new ItemStackHandler(0) {
        @Override
        protected void onContentsChanged(int slot) {
        	markDirty();
        }
    };
	private EnumFacing facing;
	
	public boolean isLoaded() {
		return this.hasWorld() && isLoaded;
	}
	
	public boolean isOrigin() {
		return this.isLoaded() && this.offset.equals(BlockPos.ORIGIN);
	}
	
	public ShelvingTile getOrigin() {
		if (!isOrigin()) {
			return ShelvingTile.get(world, center);
		}
		return this;
	}

	public void init(int size, BlockPos center, BlockPos offset, IBlockState state, EnumFacing facing) {
		this.size = size;
		this.center = center;
		this.offset = offset;
		this.state = state;
		this.facing = facing;
		this.isLoaded = true;
		
		if (isOrigin()) {
			container.setSize(size * INV_WIDTH_MULT * INV_ROWS);
		}
		
		this.markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		size = nbt.getInteger("size");
		center = NBTUtil.getPosFromTag(nbt.getCompoundTag("center"));
		offset = NBTUtil.getPosFromTag(nbt.getCompoundTag("offset"));
		state = NBTUtil.readBlockState(nbt.getCompoundTag("state"));
		facing = EnumFacing.values()[nbt.getInteger("facing")];
		container.deserializeNBT(nbt.getCompoundTag("container"));
		
		this.isLoaded = true;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		
		nbt.setInteger("size", size);
		nbt.setTag("center", NBTUtil.createPosTag(center));
		nbt.setTag("offset", NBTUtil.createPosTag(offset));
		nbt.setTag("state", NBTUtil.writeBlockState(new NBTTagCompound(), state));
		nbt.setInteger("facing", facing.ordinal());
		nbt.setTag("container", container.serializeNBT());
		
		return nbt;
	}

	public void onBreak() {
		world.setBlockState(getPos(), state);
		if (!this.isOrigin()) {
			if (this.getWorld().getBlockState(center).getBlock() == BetterWarehouse.SHELVING_BLOCK) {
				this.getWorld().destroyBlock(center, true);
				this.getWorld().destroyBlock(getPos(), true);
			}
		} else {
			for (int slot = 0; slot < container.getSlots(); slot++) {
				ItemStack itemstack = container.getStackInSlot(slot);
				if (itemstack.getCount() != 0) {
					world.spawnEntity(new EntityItem(this.world, this.center.getX(), this.center.getY()+1, this.center.getZ(), itemstack.copy()));
					itemstack.setCount(0);
				}
			}
		}
	}

	public void checkCenter() {
		if (this.getWorld().getBlockState(center).getBlock() != BetterWarehouse.SHELVING_BLOCK) {
			world.setBlockState(getPos(), state);
		}
	}

	public IBlockState getState() {
		return state;
	}

	public int getSize() {
		return size;
	}

	public ItemStackHandler getContainer() {
		return container;
	}

	public BlockPos getOffset() {
		return offset;
	}

	public EnumFacing getFacing() {
		return facing;
	}
	
	/*
	 * SYNC MAGIC
	 */
	
	@Override
	public void markDirty() {
		super.markDirty();
		if (!world.isRemote) {
			world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 1 + 2 + 8);
			world.notifyNeighborsOfStateChange(pos, this.getBlockType(), true);
		}
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		
		return new SPacketUpdateTileEntity(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
		world.markBlockRangeForRenderUpdate(getPos(), getPos());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		this.writeToNBT(tag);
		return tag;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
		super.handleUpdateTag(tag);
		world.markBlockRangeForRenderUpdate(getPos(), getPos());
	}
	
	/*
	 * Capabilities
	 */
	
	@Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (this.isOrigin()) {
			return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
		} else if (this.isLoaded()) {
			ShelvingTile origin = this.getOrigin();
        	if (origin != null) {
        		return origin.hasCapability(capability, facing);
        	}
		}
		return super.hasCapability(capability, facing);
    }

	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (this.isOrigin()) {
        	return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(container);
		} else if (this.isLoaded()) {
			ShelvingTile origin = this.getOrigin();
        	if (origin != null) {
        		return origin.getCapability(capability, facing);
        	}
		}
		return super.getCapability(capability, facing);
    }
}
