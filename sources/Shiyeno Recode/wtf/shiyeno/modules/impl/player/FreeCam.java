package wtf.shiyeno.modules.impl.player;

import baritone.api.BaritoneAPI;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.vector.Vector3d;
import wtf.shiyeno.events.Event;
import wtf.shiyeno.events.impl.packet.EventPacket;
import wtf.shiyeno.events.impl.player.EventLivingUpdate;
import wtf.shiyeno.events.impl.player.EventMotion;
import wtf.shiyeno.events.impl.render.EventRender;
import wtf.shiyeno.modules.Function;
import wtf.shiyeno.modules.FunctionAnnotation;
import wtf.shiyeno.modules.Type;
import wtf.shiyeno.modules.settings.imp.SliderSetting;
import wtf.shiyeno.util.ClientUtil;
import wtf.shiyeno.util.FreeCamera;
import wtf.shiyeno.util.font.Fonts;
import wtf.shiyeno.util.movement.MoveUtil;

@SuppressWarnings("all")
@FunctionAnnotation(name = "FreeCam", type = Type.Player)
public class FreeCam extends Function {
    private final SliderSetting speed = new SliderSetting(
            "�������� �� XZ",
            1.0f,
            0.1f,
            5.0f,
            0.05f
    );
    private final SliderSetting motionY = new SliderSetting(
            "�������� Y",
            0.5f,
            0.1f,
            1,
            0.05f
    );
    private Vector3d clientPosition = null;
    public FreeCamera player = null;
    boolean oldIsFlying;

    public FreeCam() {
        addSettings(speed, motionY);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventLivingUpdate livingUpdateEvent) {
            if (player != null) {
                player.noClip = true;
                player.setOnGround(false);
                MoveUtil.setMotion(speed.getValue().floatValue(), player);
                if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
                    player.setPosition(player.getPosX(), player.getPosY() + motionY.getValue().floatValue(), player.getPosZ());
                }
                if (Minecraft.getInstance().gameSettings.keyBindSneak.isKeyDown()) {
                    player.setPosition(player.getPosX(), player.getPosY() - motionY.getValue().floatValue(), player.getPosZ());
                }
                player.abilities.isFlying = true;

            }
       }

        if (event instanceof EventPacket e) {
            if (e.getPacket() instanceof CPlayerPacket p) {
                if (p.moving) {
                    p.x = player.getPosX();
                    p.y = player.getPosY();
                    p.z = player.getPosZ();
                }
                p.onGround = player.isOnGround();
                if (p.rotating) {
                    p.yaw = player.rotationYaw;
                    p.pitch = player.rotationPitch;
                }
            }
        }
        if (event instanceof EventMotion motionEvent) {

            if (!(BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing() || BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().isActive())) {
                mc.player.motion = Vector3d.ZERO;
                motionEvent.setCancel(true);
            }

          //handleMotionEvent(motionEvent);
        }
        if (event instanceof EventRender && ((EventRender) event).isRender2D()) {
            handleRender2DEvent((EventRender) event);
        }
    }

    /**
     * ���������� ������� onEnable.
     * �������������� ��������� ������ � ��������� ��� � ���.
     */
    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player == null) {
            return;
        }
        mc.player.setJumping(false);
        initializeFakePlayer();
        addFakePlayer();
        player.spawn();
        mc.player.movementInput = new MovementInput();
        mc.player.moveForward = 0;
        mc.player.moveStrafing = 0;
        mc.setRenderViewEntity(player);
    }

    /**
     * ���������� ������� onDisable.
     * ������� ��������� ������ �� ����.
     */
    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player == null) {
            return;
        }
        removeFakePlayer();
        mc.setRenderViewEntity(null);
        mc.player.movementInput = new MovementInputFromOptions(mc.gameSettings);

    }

    /**
     * ���������� ������� EventLivingUpdate.
     * ������������� ����������� �������� � ��������� ��� ������.
     */
    private void handleLivingUpdate() {
        player.noClip = true;
        player.setOnGround(false);
        MoveUtil.setMotion(speed.getValue().floatValue(), player);

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            player.motion.y = motionY.getValue().floatValue();
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            player.motion.y = -motionY.getValue().floatValue();
        }

        oldIsFlying = player.abilities.isFlying;
        player.abilities.isFlying = true;
    }

    /**
     * ���������� ������� EventMotion.
     * ���������� ����� CPlayerPacket �� ������(���� ����� ��������� �� Sunrise) � �������� �������.
     */
    private void handleMotionEvent(EventMotion motionEvent) {
        if (ClientUtil.isConnectedToServer("sunrise")) {
            if (mc.player.ticksExisted % 10 == 0) {
                mc.player.connection.sendPacket(new CPlayerPacket(mc.player.isOnGround()));
            }
        }
        motionEvent.setCancel(true);
    }

    /**
     * ���������� ������� EventRender.
     * ���������� ���������� � ����������� ������ � 2D �������.
     */
    private void handleRender2DEvent(EventRender renderEvent) {
        MainWindow resolution = mc.getMainWindow();

        if (clientPosition == null) {
            return;
        }

        int xPosition = (int) (player.getPosX() - mc.player.getPosX());
        int yPosition = (int) (player.getPosY() - mc.player.getPosY());
        int zPosition = (int) (player.getPosZ() - mc.player.getPosZ());

        String position = "X:" + xPosition + " Y:" + yPosition + " Z:" + zPosition;


        Fonts.gilroyBold[16].drawCenteredStringWithOutline(renderEvent.matrixStack,
                position,
                resolution.getScaledWidth() / 2F,
                resolution.getScaledHeight() / 2F + 10,
                -1);
    }

    /**
     * �������������� ��������� ������.
     * ������������� ��������� �������� ������� � ����� ��������.
     */
    private void initializeFakePlayer() {
        clientPosition = mc.player.getPositionVec();
        player = new FreeCamera(1337228);
        player.copyLocationAndAnglesFrom(mc.player);
        player.rotationYawHead = mc.player.rotationYawHead;
    }

    /**
     * ��������� ��������� ������ � ��� � ��������� ������� ������� ������.
     */
    private void addFakePlayer() {
        clientPosition = mc.player.getPositionVec();
        mc.world.addEntity(1337228, player);
    }

    /**
     * ������� ��������� ������ �� ����.
     * ��������������� ��������� � ������� ������.
     */
    private void removeFakePlayer() {
        resetFlying();
        mc.world.removeEntityFromWorld(1337228);
        player = null;
        clientPosition = null;
    }

    /**
     * ���������� ��������� ������ ������, ���� ��� ���� ��������� �� ������ ������.
     */
    private void resetFlying() {
        if (oldIsFlying) {
            mc.player.abilities.isFlying = false;
            oldIsFlying = false;
        }
    }
}