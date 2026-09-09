package duskdn.plantz_ex.animation.plants;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationDefinition;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 5.1.6
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class CrimsonShroomAnimation {
	public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(2.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.05F, 0.95F, 1.05F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.56F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.48F, KeyframeAnimations.degreeVec(6.37F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("top", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.05F, 0.94F, 1.05F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition action = AnimationDefinition.Builder.withLength(0.88F)
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.08F, KeyframeAnimations.scaleVec(0.975F, 1.025F, 0.975F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.16F, KeyframeAnimations.scaleVec(1.02F, 0.98F, 1.02F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.36F, KeyframeAnimations.scaleVec(1.01F, 0.99F, 1.01F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.44F, KeyframeAnimations.scaleVec(0.975F, 1.025F, 0.975F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.68F, KeyframeAnimations.scaleVec(1.01F, 0.99F, 1.01F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.08F, KeyframeAnimations.degreeVec(-8.4F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.16F, KeyframeAnimations.degreeVec(-6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.36F, KeyframeAnimations.degreeVec(6.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.44F, KeyframeAnimations.degreeVec(12.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.degreeVec(-2.4F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.28F, KeyframeAnimations.scaleVec(1.045F, 0.985F, 1.025F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.44F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.08F, KeyframeAnimations.degreeVec(-1.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.16F, KeyframeAnimations.degreeVec(-1.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.36F, KeyframeAnimations.degreeVec(8.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.44F, KeyframeAnimations.degreeVec(1.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.68F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.08F, KeyframeAnimations.scaleVec(0.985F, 1.015F, 0.985F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.16F, KeyframeAnimations.scaleVec(1.035F, 0.965F, 1.035F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.36F, KeyframeAnimations.scaleVec(1.035F, 0.965F, 1.035F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.44F, KeyframeAnimations.scaleVec(0.98F, 1.02F, 0.98F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.68F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.16F, KeyframeAnimations.degreeVec(-4.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.36F, KeyframeAnimations.degreeVec(6.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.56F, KeyframeAnimations.degreeVec(7.326F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.68F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.08F, KeyframeAnimations.scaleVec(0.955F, 1.045F, 0.955F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.16F, KeyframeAnimations.scaleVec(1.162F, 0.838F, 1.162F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.36F, KeyframeAnimations.scaleVec(1.162F, 0.838F, 1.162F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.48F, KeyframeAnimations.scaleVec(0.748F, 1.252F, 0.748F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.68F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("top", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.08F, KeyframeAnimations.scaleVec(0.98F, 1.02F, 0.98F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.16F, KeyframeAnimations.scaleVec(1.045F, 0.955F, 1.045F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.36F, KeyframeAnimations.scaleVec(1.045F, 0.955F, 1.045F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.48F, KeyframeAnimations.scaleVec(0.98F, 1.02F, 0.98F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.68F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition sleep = AnimationDefinition.Builder.withLength(5.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.96F, KeyframeAnimations.scaleVec(0.95F, 1.03F, 0.96F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.32F, KeyframeAnimations.scaleVec(0.95F, 1.03F, 0.96F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.84F, KeyframeAnimations.scaleVec(1.0737F, 0.9298F, 1.087F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.9953F, 0.2178F, -2.4905F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(4.9953F, 0.2178F, -2.4905F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.6669F, 16.309F, -4.3301F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.32F, KeyframeAnimations.degreeVec(7.0356F, 15.1818F, -4.5441F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.08F, KeyframeAnimations.degreeVec(3.3732F, 16.0153F, -2.7619F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.degreeVec(12.6669F, 16.309F, -4.3301F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.06F, 0.9F, 1.07F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.84F, KeyframeAnimations.scaleVec(1.17F, 0.68F, 1.12F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(3.8F, KeyframeAnimations.scaleVec(0.93F, 1.13F, 0.98F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(5.0F, KeyframeAnimations.scaleVec(1.06F, 0.9F, 1.07F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition init = AnimationDefinition.Builder.withLength(1.0F)
		.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.degreeVec(-6.81F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, -2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.68F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("stem", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.119F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.12F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.28F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.6F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.72F, KeyframeAnimations.scaleVec(0.94F, 1.05F, 0.94F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2F, KeyframeAnimations.degreeVec(-5.32F, -0.866F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.48F, KeyframeAnimations.degreeVec(22.18F, -0.866F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.degreeVec(-11.81F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.16F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.32F, KeyframeAnimations.scaleVec(0.95F, 1.1F, 0.91F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.52F, KeyframeAnimations.scaleVec(1.0144F, 0.9912F, 1.0279F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.72F, KeyframeAnimations.scaleVec(0.9333F, 1.057F, 0.925F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("barrel", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.48F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.6F, KeyframeAnimations.scaleVec(0.53F, 1.61F, 0.55F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.8F, KeyframeAnimations.scaleVec(1.16F, 0.89F, 1.2F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("top", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.12F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.28F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.52F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.68F, KeyframeAnimations.scaleVec(0.88F, 1.2449F, 0.94F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();
}