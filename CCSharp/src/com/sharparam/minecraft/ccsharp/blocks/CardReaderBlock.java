/*
 * CardReaderBlock.java
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

import com.sharparam.minecraft.ccsharp.entities.CardReaderEntity;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * User: Sharparam
 * Date: 2013-06-18
 * Time: 17:39
 */
public class CardReaderBlock extends BaseBlock {
    public CardReaderBlock(int id) {
        super("cardReader", "Card Reader", id, 0, Material.rock);
        setHardness(2.0f);
        setStepSound(soundStoneFootstep);
        setCreativeTab(CreativeTabs.tabBlock);
        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 1);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new CardReaderEntity();
    }
}
