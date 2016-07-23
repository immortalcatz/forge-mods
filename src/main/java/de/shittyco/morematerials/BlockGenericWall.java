/**
 * Copyright (C) Jon Rowlett. All rights reserved.
 */

package de.shittyco.morematerials;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Customizable Wall block that supports different materials.
 * BlockWall in Minecraft is not extensible.
 * Fun fact: In 1.8, torch placement is hard-coded, so new wall types have to
 * derive from fences. Torch placement is kind of important because it keeps
 * monsters from spawning.
 * @author jrowlett
 */
public abstract class BlockGenericWall extends BlockFence {

    /**
     * The UP property to render the wall at full height.
     */
    private static final PropertyBool UP = PropertyBool.create("up");

    /**
     * Bounding boxes.
     */
    private static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {
        new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), 
        new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), 
        new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), 
        new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), 
        new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), 
        new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), 
        new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), 
        new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), 
        new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), 
        new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), 
        new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D),
        new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),
        new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), 
        new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), 
        new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), 
        new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
        };
    
    /**
     * Selected bounding boxes.
     */
    private static final AxisAlignedBB[] SELECTED_BOUNDING_BOXES = new AxisAlignedBB[] {
        BOUNDING_BOXES[0].setMaxY(1.5D),
        BOUNDING_BOXES[1].setMaxY(1.5D),
        BOUNDING_BOXES[2].setMaxY(1.5D),
        BOUNDING_BOXES[3].setMaxY(1.5D),
        BOUNDING_BOXES[4].setMaxY(1.5D),
        BOUNDING_BOXES[5].setMaxY(1.5D),
        BOUNDING_BOXES[6].setMaxY(1.5D),
        BOUNDING_BOXES[7].setMaxY(1.5D),
        BOUNDING_BOXES[8].setMaxY(1.5D),
        BOUNDING_BOXES[9].setMaxY(1.5D),
        BOUNDING_BOXES[10].setMaxY(1.5D),
        BOUNDING_BOXES[11].setMaxY(1.5D),
        BOUNDING_BOXES[12].setMaxY(1.5D), 
        BOUNDING_BOXES[13].setMaxY(1.5D), 
        BOUNDING_BOXES[14].setMaxY(1.5D), 
        BOUNDING_BOXES[15].setMaxY(1.5D)};
    
    /**
     * The block on which the wall is based.
     */
    private Block sourceBlock;

    /**
     * metadata on the source block.
     */
    private int sourceMetadata;

    /**
     * Initializes a new instance of the BlockGenericWall class.
     * @param source the material the wall is made of.
     * @param sourceMeta the metadata for the source block.
     */
    protected BlockGenericWall(
        final Block source,
        final int sourceMeta) {
        super(
            source.getMaterial(source.getDefaultState()),
            source.getMapColor(source.getDefaultState()));
        this.sourceBlock = source;
        this.sourceMetadata = sourceMeta;
        setStepSound(source.getStepSound());

        // Fence has north, south, east, west properties, but not up.
        IBlockState state = getDefaultState();
        setDefaultState(state.withProperty(UP, false));
    }

    /**
     * Gets the bounding box.
     * @param state the block state.
     * @param source the source access.
     * @param pos the block position.
     * @return the bounding box.
     */
    @Override
    public final AxisAlignedBB getBoundingBox(
        final IBlockState state, 
        final IBlockAccess source, 
        final BlockPos pos) {
        IBlockState actualState = this.getActualState(state, source, pos);
        return BOUNDING_BOXES[getBoundingBoxIndex(actualState)];
    }

    /**
     * Gets the selected bounding box.
     * @param blockState the block state
     * @param worldIn the world.
     * @param pos the block position.
     * @return the bounding box.
     */
    @Override
    public final AxisAlignedBB getSelectedBoundingBox(
        final IBlockState blockState, 
        final World worldIn, 
        final BlockPos pos) {
        IBlockState actualState = this.getActualState(blockState, worldIn, pos);
        return SELECTED_BOUNDING_BOXES[getBoundingBoxIndex(actualState)];
    }

    /**
     * Return whether an adjacent block can connect to a wall.
     * @param blockAccess the block access interface.
     * @param pos world coordinate.
     * @return whether a block can connect to the wall.
     */
    @Override
    public final boolean canConnectTo(
        final IBlockAccess blockAccess,
        final BlockPos pos) {
        IBlockState blockState = blockAccess.getBlockState(pos);
        Block block = blockState.getBlock();

        if (block instanceof BlockGenericWall
            || block instanceof net.minecraft.block.BlockFenceGate) {
            return true;
        }

        if (block.getMaterial(blockState).isOpaque()
            && block.isFullCube(blockState)) {
            return block.getMaterial(blockState) != Material.gourd;
        }

        return false;
    }

    /**
     * Gets the actual block state.
     * @param state block state object
     * @param blockAccess world
     * @param pos location in the world
     * @return the modified state.
     */
    @Override
    public final IBlockState getActualState(
        final IBlockState state,
        final IBlockAccess blockAccess,
        final BlockPos pos) {
        boolean isUp = this.canConnectUp(blockAccess, pos);
        boolean isNorth = this.canConnectTo(blockAccess, pos.north());
        boolean isSouth = this.canConnectTo(blockAccess, pos.south());
        boolean isWest = this.canConnectTo(blockAccess, pos.west());
        boolean isEast = this.canConnectTo(blockAccess, pos.east());
        boolean hasPost = isUp 
            || !(isNorth && isSouth && !isEast && !isWest 
                || isEast && isWest && !isNorth && !isSouth);
        return state
            .withProperty(
                BlockFence.NORTH,
                isNorth)
            .withProperty(
                BlockFence.SOUTH,
                isSouth)
            .withProperty(
                BlockFence.WEST,
                isWest)
            .withProperty(
                BlockFence.EAST,
                isEast)
            .withProperty(UP, hasPost);
    }

    /**
     * Creates the empty block state.
     * @return empty block state with properties.
     */
    protected final BlockStateContainer createBlockState() {
        IProperty[] props = new IProperty[] {
            BlockFence.NORTH,
            BlockFence.EAST,
            BlockFence.WEST,
            BlockFence.SOUTH,
            UP
        };

        return new BlockStateContainer(this, props);
    }
    
    /**
     * Gets the index into the bounding box table given the block state.
     * @param blockState the block state.
     * @return the index into the bounding box table.
     */
    private static int getBoundingBoxIndex(
        final IBlockState blockState) {
        int index = 0;

        if (((Boolean)blockState.getValue(NORTH)).booleanValue()) {
            index |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (((Boolean)blockState.getValue(EAST)).booleanValue()) {
            index |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (((Boolean)blockState.getValue(SOUTH)).booleanValue()) {
            index |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (((Boolean)blockState.getValue(WEST)).booleanValue()) {
            index |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return index;
    }
    

    /**
     * Helper for special casing torches that are on top of the wall.
     * @param blockAccess the world
     * @param pos the wall's position
     * @return whether or not the wall is full height.
     */
    private boolean canConnectUp(
        final IBlockAccess blockAccess,
        final BlockPos pos) {

        // draw the wall at full height if a torch is on top, but not if a
        // torch is anywhere else.
        BlockPos upPos = pos.up();
        IBlockState upState = blockAccess.getBlockState(upPos);
        Block upBlock = upState.getBlock();
        return upBlock instanceof net.minecraft.block.BlockTorch
            || this.canConnectTo(blockAccess, upPos);
    }
}
