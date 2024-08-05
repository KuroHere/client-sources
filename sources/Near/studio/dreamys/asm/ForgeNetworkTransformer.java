package studio.dreamys.asm;

import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.*;
import studio.dreamys.util.ClassUtils;
import studio.dreamys.util.NodeUtils;

import static org.objectweb.asm.Opcodes.*;

/**
 * Transform bytecode of classes
 */
public class ForgeNetworkTransformer implements IClassTransformer {

    /**
     * Transform a class
     *
     * @param name            of target class
     * @param transformedName of target class
     * @param basicClass      bytecode of target class
     * @return new bytecode
     */
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(name.equals("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher")) {
            try {
                final ClassNode classNode = ClassUtils.INSTANCE.toClassNode(basicClass);

                classNode.methods.stream().filter(methodNode -> methodNode.name.equals("handleVanilla")).forEach(methodNode -> {
                    final LabelNode labelNode = new LabelNode();

                    methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(
                            new MethodInsnNode(INVOKESTATIC, "studio/dreamys/asm/ForgeNetworkTransformer", "returnMethod", "()Z", false),
                            new JumpInsnNode(IFEQ, labelNode),
                            new InsnNode(ICONST_0),
                            new InsnNode(IRETURN),
                            labelNode
                    ));
                });

                return ClassUtils.INSTANCE.toBytes(classNode);
            }catch(final Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        if(name.equals("net.minecraftforge.fml.common.network.handshake.HandshakeMessageHandler")) {
            try {
                final ClassNode classNode = ClassUtils.INSTANCE.toClassNode(basicClass);

                classNode.methods.stream().filter(method -> method.name.equals("channelRead0")).forEach(methodNode -> {
                    final LabelNode labelNode = new LabelNode();

                    methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), NodeUtils.INSTANCE.toNodes(
                            new MethodInsnNode(INVOKESTATIC,
                                    "studio/dreamys/asm/ForgeNetworkTransformer",
                                    "returnMethod", "()Z", false
                            ),
                            new JumpInsnNode(IFEQ, labelNode),
                            new InsnNode(RETURN),
                            labelNode
                    ));
                });

                return ClassUtils.INSTANCE.toBytes(classNode);
            }catch(final Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return basicClass;
    }

    public static boolean returnMethod() {
        return !Minecraft.getMinecraft().isIntegratedServerRunning();
    }
}