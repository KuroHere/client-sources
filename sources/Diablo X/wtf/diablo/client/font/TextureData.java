package wtf.diablo.client.font;

import java.nio.ByteBuffer;

public class TextureData {
    private final int textureId, width, height;
    private final ByteBuffer buffer;


    public TextureData(int textureId, int width, int height, ByteBuffer buffer) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }


    public int getTextureId() {
        return textureId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
