package com.sharparam.minecraft.ccsharp.utils;

/**
 * User: f16gaming
 * Date: 6/22/13
 * Time: 2:57 PM
 */
public final class TextureHelper {
    private static final int DEFAULT_TEXTURE_WIDTH = 256;
    private static final int DEFAULT_TEXTURE_HEIGHT = 256;
    private static final int BLOCK_TEXTURE_WIDTH = 16;
    private static final int BLOCK_TEXTURE_HEIGHT = 16;

    public static int getBlockTextureIndex(int col) throws IllegalArgumentException {
        return getBlockTextureIndex(col, 0);
    }

    public static int getBlockTextureIndex(int col, int row) throws IllegalArgumentException {
        return getBlockTextureIndex(col, row, DEFAULT_TEXTURE_WIDTH, DEFAULT_TEXTURE_HEIGHT);
    }

    public static int getBlockTextureIndex(int col, int row, int textureWidth, int textureHeight)
            throws IllegalArgumentException {
        int cols = textureWidth / BLOCK_TEXTURE_WIDTH;
        int rows = textureHeight / BLOCK_TEXTURE_HEIGHT;

        if (col > cols)
            throw new IllegalArgumentException("Argument #1 'col' can't be larger than number of columns on texture file.");
        else if (row > rows)
            throw new IllegalArgumentException("Argument #2 'row' can't be larger than number of rows on texture file.");

        return col + row * (textureHeight / BLOCK_TEXTURE_HEIGHT);
    }
}
