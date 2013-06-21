/*
 * ContainerBlock.java
 *
 * Copyright © 2013 by Adam Hellberg <adam.hellberg@sharparam.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.sharparam.minecraft.ccsharp.blocks;

import com.sharparam.minecraft.ccsharp.BaseProxy;
import com.sharparam.minecraft.ccsharp.CCSharp;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * User: Sharparam
 * Date: 2013-06-19
 * Time: 19:03
 */
public abstract class ContainerBlock extends BlockContainer {
    public ContainerBlock(String uid, String name, int id, int texture, Material material) {
        super(id, texture, material);
        setTextureFile(BaseProxy.BLOCK_TEXTURE);
        setBlockName(uid);
        LanguageRegistry.addName(this, name);
        GameRegistry.registerBlock(this, uid);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
                                    int par6, float par7, float par8, float par9) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile == null || player.isSneaking())
            return false;

        player.openGui(CCSharp.instance, 0, world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    protected void dropItems(World world, int x, int y, int z) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (!(tile instanceof IInventory))
            return;

        IInventory inventory = (IInventory) tile;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if (item == null || item.stackSize <= 0)
                continue;

            float rx = world.rand.nextFloat() * 0.8f + 0.1f;
            float ry = world.rand.nextFloat() * 0.8f + 0.1f;
            float rz = world.rand.nextFloat() * 0.8f + 0.1f;

            EntityItem entItem = new EntityItem(world, x + rx, y + ry, z + rz,
                    new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

            if (item.hasTagCompound())
                entItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());

            float factor = 0.05f;

            entItem.motionX = world.rand.nextGaussian() * factor;
            entItem.motionY = world.rand.nextGaussian() * factor + 0.2f;
            entItem.motionZ = world.rand.nextGaussian() * factor;
            world.spawnEntityInWorld(entItem);
            item.stackSize = 0;
        }
    }
}
