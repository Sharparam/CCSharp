/*
 * CCSharp.java
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

package com.sharparam.minecraft.ccsharp;

import com.sharparam.minecraft.ccsharp.blocks.CardReaderBlock;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import java.util.logging.Logger;

/**
 * User: Sharparam
 * Date: 2013-06-17
 * Time: 19:37
 */
@Mod(modid = CCSharp.ID, name = CCSharp.NAME, version = CCSharp.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class CCSharp {
    public static final String ID = "CCSharp";
    public static final String NAME = "CCSharp";
    public static final String VERSION = "0.0.2";

    @Mod.Instance(ID)
    public static CCSharp instance;

    private Logger log;
    private Configuration config;

    public Block cardReaderBlock;

    @SidedProxy(
            clientSide = "com.sharparam.minecraft.ccsharp.client.ClientProxy",
            serverSide = "com.sharparam.minecraft.ccsharp.server.ServerProxy")
    public static IProxy proxy;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
        log = Logger.getLogger(ID);
        log.setParent(FMLLog.getLogger());
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        config.getBlock("cardReader.id", 2050);
    }

    @Mod.Init
    public void init(FMLInitializationEvent event) {
        cardReaderBlock = new CardReaderBlock(config.getBlock("cardReader.id", 2050).getInt(), 0, Material.rock);
        LanguageRegistry.addName(cardReaderBlock, "Card Reader");
        MinecraftForge.setBlockHarvestLevel(cardReaderBlock, "pickaxe", 1);
        GameRegistry.registerBlock(cardReaderBlock, "cardReader");

        proxy.registerRenderers();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event) {

    }
}
