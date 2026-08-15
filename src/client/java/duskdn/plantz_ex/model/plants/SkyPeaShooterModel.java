package duskdn.plantz_ex.model.plants;

import duskdn.plantz_ex.renderer.entity.PlantRenderState;
import duskdn.plantz_ex.animation.plants.SkyPeaShooterAnimation;
import duskdn.plantz_ex.model.plants.init.PazPlantModel;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.NotNull;
import static duskdn.plantz_ex.util.UtilsKt.pazResource;

public class SkyPeaShooterModel extends PazPlantModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(pazResource("sky_peashooter"), "main");
	private final ModelPart body;
	private final ModelPart stem;
	private final ModelPart stem_2;
	private final ModelPart scarf;
	private final ModelPart scarf_end;
	private final ModelPart scarf_tip;
	private final ModelPart scarf_tip_2;
	private final ModelPart scarf_tip_3;
	private final ModelPart head;
	private final ModelPart hat_layer;
	private final ModelPart barrel;
	private final ModelPart head_leaf;
	private final ModelPart head_leaf_2;
	private final ModelPart head_leaf_tip;
	private final ModelPart propeller;
	private final ModelPart propeller_leaf1;
	private final ModelPart propeller_leaf2;
	private final ModelPart propeller_leaf3;
	private final ModelPart leaves;
	private final ModelPart leaf_1;
	private final ModelPart leaf_tip_1;
	private final ModelPart leaf_2;
	private final ModelPart leaf_tip_2;
	private final ModelPart leaf_3;
	private final ModelPart leaf_tip_3;
	private final ModelPart leaf_4;
	private final ModelPart leaf_tip_4;
	private final KeyframeAnimation initAirAnimation;
	private final KeyframeAnimation idleAirAnimation;
	private final KeyframeAnimation actionAirAnimation;

	public SkyPeaShooterModel(ModelPart root) {
		super(
			SkyPeaShooterAnimation.init_land.bake(root),
			SkyPeaShooterAnimation.idle_land.bake(root),
			SkyPeaShooterAnimation.action_land.bake(root),
			SkyPeaShooterAnimation.sleep.bake(root),
			null,
			root
		);
		this.body = root.getChild("body");
		this.stem = this.body.getChild("stem");
		this.stem_2 = this.stem.getChild("stem_2");
		this.scarf = this.stem_2.getChild("scarf");
		this.scarf_end = this.scarf.getChild("scarf_end");
		this.scarf_tip = this.scarf_end.getChild("scarf_tip");
		this.scarf_tip_2 = this.scarf_tip.getChild("scarf_tip_2");
		this.scarf_tip_3 = this.scarf_tip_2.getChild("scarf_tip_3");
		this.head = this.stem_2.getChild("head");
		this.hat_layer = this.head.getChild("hat_layer");
		this.barrel = this.head.getChild("barrel");
		this.head_leaf = this.head.getChild("head_leaf");
		this.head_leaf_2 = this.head_leaf.getChild("head_leaf_2");
		this.head_leaf_tip = this.head_leaf_2.getChild("head_leaf_tip");
		this.propeller = this.head_leaf_2.getChild("propeller");
		this.propeller_leaf1 = this.propeller.getChild("propeller_leaf1");
		this.propeller_leaf2 = this.propeller.getChild("propeller_leaf2");
		this.propeller_leaf3 = this.propeller.getChild("propeller_leaf3");
		this.leaves = this.body.getChild("leaves");
		this.leaf_1 = this.leaves.getChild("leaf_1");
		this.leaf_tip_1 = this.leaf_1.getChild("leaf_tip_1");
		this.leaf_2 = this.leaves.getChild("leaf_2");
		this.leaf_tip_2 = this.leaf_2.getChild("leaf_tip_2");
		this.leaf_3 = this.leaves.getChild("leaf_3");
		this.leaf_tip_3 = this.leaf_3.getChild("leaf_tip_3");
		this.leaf_4 = this.leaves.getChild("leaf_4");
		this.leaf_tip_4 = this.leaf_4.getChild("leaf_tip_4");
		this.initAirAnimation = SkyPeaShooterAnimation.init_air.bake(root);
		this.idleAirAnimation = SkyPeaShooterAnimation.idle_air.bake(root);
		this.actionAirAnimation = SkyPeaShooterAnimation.action_air.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition stem = body.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(4, 25).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition stem_2 = stem.addOrReplaceChild("stem_2", CubeListBuilder.create().texOffs(4, 18).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(2, 12).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition scarf = stem_2.addOrReplaceChild("scarf", CubeListBuilder.create().texOffs(3, 44).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 0.0F));

		PartDefinition scarf_end = scarf.addOrReplaceChild("scarf_end", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition scarf_tip = scarf_end.addOrReplaceChild("scarf_tip", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition scarf_tip_r1 = scarf_tip.addOrReplaceChild("scarf_tip_r1", CubeListBuilder.create().texOffs(15, 60).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition scarf_tip_2 = scarf_tip.addOrReplaceChild("scarf_tip_2", CubeListBuilder.create(), PartPose.offset(-2.5F, 0.0F, 0.0F));

		PartDefinition scarf_tip_2_r1 = scarf_tip_2.addOrReplaceChild("scarf_tip_2_r1", CubeListBuilder.create().texOffs(15, 56).addBox(-2.5F, -2.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition scarf_tip_3 = scarf_tip_2.addOrReplaceChild("scarf_tip_3", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 0.0F));

		PartDefinition scarf_tip_3_r1 = scarf_tip_3.addOrReplaceChild("scarf_tip_3_r1", CubeListBuilder.create().texOffs(15, 47).addBox(-2.5F, -7.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition head = stem_2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(19, 19).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition hat_layer = head.addOrReplaceChild("hat_layer", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -5.0F, -1.0F));

		PartDefinition barrel = head.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(18, 25).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -5.0F));

		PartDefinition head_leaf = head.addOrReplaceChild("head_leaf", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 2.9167F));

		PartDefinition cube_r1 = head_leaf.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(31, 3).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0833F, 0.0F, 0.0F, 1.5708F));

		PartDefinition head_leaf_2 = head_leaf.addOrReplaceChild("head_leaf_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.0833F));

		PartDefinition cube_r2 = head_leaf_2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(26, -2).mirror().addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition head_leaf_tip = head_leaf_2.addOrReplaceChild("head_leaf_tip", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.0F));

		PartDefinition cube_r3 = head_leaf_tip.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(31, -2).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r4 = head_leaf_tip.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(31, -2).addBox(0.0F, -2.0F, -0.5F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 2.3562F));

		PartDefinition propeller = head_leaf_2.addOrReplaceChild("propeller", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.9167F));

		PartDefinition propeller_leaf1 = propeller.addOrReplaceChild("propeller_leaf1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = propeller_leaf1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(38, 3).addBox(-3.0F, -6.0F, 0.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition propeller_leaf2 = propeller.addOrReplaceChild("propeller_leaf2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.0944F));

		PartDefinition cube_r6 = propeller_leaf2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(38, 3).addBox(-3.0F, -6.0F, 0.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition propeller_leaf3 = propeller.addOrReplaceChild("propeller_leaf3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0436F, 0.0F, 2.0944F));

		PartDefinition cube_r7 = propeller_leaf3.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(38, 3).addBox(-3.0F, -6.0F, 0.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition leaves = body.addOrReplaceChild("leaves", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leaf_1 = leaves.addOrReplaceChild("leaf_1", CubeListBuilder.create().texOffs(30, 10).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7071F, 0.0F, -0.6464F, 0.0F, -0.7854F, 0.0F));

		PartDefinition leaf_tip_1 = leaf_1.addOrReplaceChild("leaf_tip_1", CubeListBuilder.create().texOffs(31, 16).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition leaf_2 = leaves.addOrReplaceChild("leaf_2", CubeListBuilder.create().texOffs(30, 10).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7071F, 0.0F, 0.7678F, 0.0F, -2.3562F, 0.0F));

		PartDefinition leaf_tip_2 = leaf_2.addOrReplaceChild("leaf_tip_2", CubeListBuilder.create().texOffs(31, 16).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition leaf_3 = leaves.addOrReplaceChild("leaf_3", CubeListBuilder.create().texOffs(30, 10).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7071F, 0.0F, -0.6464F, 0.0F, 0.7854F, 0.0F));

		PartDefinition leaf_tip_3 = leaf_3.addOrReplaceChild("leaf_tip_3", CubeListBuilder.create().texOffs(31, 16).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition leaf_4 = leaves.addOrReplaceChild("leaf_4", CubeListBuilder.create().texOffs(30, 10).addBox(-4.0F, 0.0F, -6.0F, 8.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7071F, 0.0F, 0.7678F, 0.0F, 2.3562F, 0.0F));

		PartDefinition leaf_tip_4 = leaf_4.addOrReplaceChild("leaf_tip_4", CubeListBuilder.create().texOffs(31, 16).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public KeyframeAnimation getProcessedInit(PlantRenderState state) {
		return state.isInAir() ? this.initAirAnimation :
				super.getProcessedInit(state);
	}

	@Override
	public KeyframeAnimation getProcessedIdle(PlantRenderState state) {
		return state.isInAir() ? this.idleAirAnimation :
				super.getProcessedIdle(state);
	}

	@Override
	public KeyframeAnimation getProcessedAction(PlantRenderState state) {
		return state.isInAir() ? this.actionAirAnimation :
				super.getProcessedAction(state);
	}

	@Override
	public void setupAnim(@NotNull PlantRenderState state) {
		this.head.xRot = state.xRot * (float) (Math.PI / 180.0);
		super.setupAnim(state);
		this.stem.yRot = state.yRot * (float) (Math.PI / 180.0);

		float age = state.ageInTicks;

		AnimationState actionAnimState = state.getActionAnimationState();

		double mult = actionAnimState.isStarted() ? 1.52-actionAnimState.getTimeInMillis(age) : 1.0;

		if (state.isInAir()) {
			mult *= 4;
		}

		this.propeller.zRot = (float) (age * mult);
	}
}