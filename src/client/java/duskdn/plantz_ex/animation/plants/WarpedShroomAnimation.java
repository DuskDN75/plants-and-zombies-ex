package duskdn.plantz_ex.animation.plants;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 5.1.6
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class WarpedShroomAnimation {
	public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(4.0F).looping()
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.1F, 0.9F, 1.1F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.96F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.92F, KeyframeAnimations.scaleVec(1.1F, 0.9F, 1.1F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.2889F, 5.0999F, -2.2889F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.96F, KeyframeAnimations.degreeVec(-7.5175F, -4.631F, 3.1319F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.92F, KeyframeAnimations.degreeVec(-2.7254F, 4.881F, -2.7253F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();

	public static final AnimationDefinition action = AnimationDefinition.Builder.withLength(0.68F)
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.24F, KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.52F, KeyframeAnimations.degreeVec(-2.71F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.24F, KeyframeAnimations.scaleVec(1.08F, 0.81F, 1.08F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.9025F, 1.1126F, 0.9125F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.52F, KeyframeAnimations.scaleVec(1.0125F, 0.9826F, 1.0125F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.12F, KeyframeAnimations.degreeVec(4.78F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.28F, KeyframeAnimations.degreeVec(15.96F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.degreeVec(7.02F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.52F, KeyframeAnimations.degreeVec(3.41F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.24F, KeyframeAnimations.posVec(0.0F, -0.7F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.24F, KeyframeAnimations.scaleVec(1.07F, 0.96F, 1.08F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.9619F, 1.1003F, 0.9693F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.52F, KeyframeAnimations.scaleVec(1.02F, 0.9789F, 1.0242F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.52F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.24F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 0.9914F, -0.1305F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.52F, KeyframeAnimations.posVec(0.0F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.24F, KeyframeAnimations.scaleVec(1.46F, 0.92F, 1.46F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.9611F, 1.2207F, 0.9611F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.52F, KeyframeAnimations.scaleVec(1.15F, 0.88F, 1.15F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.68F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();

	public static final AnimationDefinition sleep = AnimationDefinition.Builder.withLength(5.0F).looping()
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.4811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.52F, KeyframeAnimations.degreeVec(4.9953F, 0.2178F, -2.4905F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.6F, KeyframeAnimations.degreeVec(11.5719F, 0.8601F, -6.5454F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(5.0F, KeyframeAnimations.degreeVec(7.4811F, 0.4352F, -4.9811F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(11.6175F, 1.9438F, 2.7712F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.52F, KeyframeAnimations.degreeVec(6.5988F, 4.6909F, 3.8972F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.6F, KeyframeAnimations.degreeVec(16.4309F, 7.4435F, 1.7939F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(5.0F, KeyframeAnimations.degreeVec(11.6175F, 1.9438F, 2.7712F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(5.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.52F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.6F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(5.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.52F, KeyframeAnimations.scaleVec(1.23F, 0.76F, 1.35F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.6F, KeyframeAnimations.scaleVec(0.89F, 1.95F, 0.89F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(5.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();

	public static final AnimationDefinition init = AnimationDefinition.Builder.withLength(0.72F)
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.72F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.2F, KeyframeAnimations.scaleVec(0.82F, 0.0F, 0.86F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.93F, 1.06F, 0.92F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.76F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.4F, KeyframeAnimations.degreeVec(-16.57F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.6F, KeyframeAnimations.degreeVec(7.71F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.72F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.2F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.44F, KeyframeAnimations.scaleVec(0.5743F, 1.1543F, 0.5743F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.6F, KeyframeAnimations.scaleVec(1.0481F, 0.9741F, 1.0481F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.76F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.44F, KeyframeAnimations.scaleVec(0.83F, 1.85F, 0.81F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.6F, KeyframeAnimations.scaleVec(1.24F, 0.91F, 1.24F), AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(0.72F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
			))
			.build();
}