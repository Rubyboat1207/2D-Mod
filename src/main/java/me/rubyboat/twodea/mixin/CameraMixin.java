package me.rubyboat.twodea.mixin;

import me.rubyboat.twodea.ExampleMod;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow protected abstract void setPos(Vec3d pos);

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow private float yaw;
    @Shadow private float pitch;

    @Shadow protected abstract void moveBy(double x, double y, double z);

    @Shadow protected abstract double clipToSpace(double desiredCameraDistance);

    @Shadow private Vec3d pos;
    @Shadow private boolean ready;
    @Shadow private BlockView area;
    @Shadow private Entity focusedEntity;
    @Shadow private boolean thirdPerson;
    @Shadow private float lastCameraY;
    @Shadow private float cameraY;
    public Boolean Is2D = false;
    public int cameradistance = 6;
    

    @Inject(at = @At("HEAD"), method = "update", cancellable = true)
    private void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci)
    {
        this.ready = true;
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson;
        this.setRotation(focusedEntity.getYaw(tickDelta), focusedEntity.getPitch(tickDelta));
        this.setPos(new Vec3d(MathHelper.lerp((double)tickDelta, focusedEntity.prevX, focusedEntity.getX()), MathHelper.lerp((double)tickDelta, focusedEntity.prevY, focusedEntity.getY()) + (double)MathHelper.lerp(tickDelta, this.lastCameraY, this.cameraY), MathHelper.lerp((double)tickDelta, focusedEntity.prevZ, focusedEntity.getZ())));
        if(ExampleMod.firstpersonkey.wasPressed())
        {
            Is2D = !Is2D;
        }
        if (thirdPerson) {
            if(ExampleMod.changedistancekey.wasPressed())
            {

                cameradistance++;
                if(cameradistance > 10)
                {
                    cameradistance = 4;
                }
            }
            this.setPos(new Vec3d(cameradistance, this.pos.y, this.pos.z));
            this.setRotation(90, 0);
            if (inverseView)
            {
                this.setPos(new Vec3d(-cameradistance, this.pos.y, this.pos.z));
                this.setRotation(-90, 0);
            }

        }else if (focusedEntity instanceof LivingEntity && ((LivingEntity)focusedEntity).isSleeping()) {
            if(Is2D)
            {
                Direction direction = ((LivingEntity)focusedEntity).getSleepingDirection();
                this.setRotation(direction != null ? direction.asRotation() - 180.0F : 0.0F, 0.0F);
                this.moveBy(0.0D, 0.3D, 0.0D);
            }
            this.setPos(new Vec3d(this.pos.x, cameradistance, this.pos.z));
            this.setRotation(0, -90);
        }
        ci.cancel();
    }
}
