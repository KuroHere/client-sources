package net.shoreline.client.api.render.shader;

import com.mojang.blaze3d.platform.GlStateManager;

public class Uniform<T>
{
    private int id;
    private T value;
    private final String name;

    public Uniform(String name)
    {
        this.name = name;
    }

    public void init(int programId)
    {
        id = GlStateManager._glGetUniformLocation(programId, name);
    }

    public void set(T value)
    {
        this.value = value;
    }

    public T get()
    {
        return value;
    }

    public int getId()
    {
        return id;
    }
}
