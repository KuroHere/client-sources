package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class Tessellator {
	private WorldRenderer worldRenderer;

	private WorldVertexBufferUploader field_178182_b = new WorldVertexBufferUploader();

	private static final Tessellator instance = new Tessellator(2097152);

	private static final String __OBFID = "CL_00000960";

	public static Tessellator getInstance() {
		return instance;
	}

	public Tessellator(int p_i1250_1_) {
		this.worldRenderer = new WorldRenderer(p_i1250_1_);
	}

	public int draw() {
		return this.field_178182_b.draw(this.worldRenderer, this.worldRenderer.draw());
	}

	public WorldRenderer getWorldRenderer() {
		return this.worldRenderer;
	}

	public WorldRenderer getBuffer()
	{
		return this.worldRenderer;
	}
}
