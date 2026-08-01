package duskdn.plantz.model.plants;

import duskdn.plantz.PlantRenderState;
import duskdn.plantz.animation.plants.FlowerPotAnimation;
import duskdn.plantz.animation.plants.WaterPotAnimation;
import duskdn.plantz.model.plants.init.PazPlantModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jetbrains.annotations.NotNull;
import static duskdn.plantz.util.UtilsKt.pazResource;

public class WaterPotModel extends PazPlantModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(pazResource("waterpot"), "main");
	private final ModelPart body;
	private final ModelPart flower;
	private final ModelPart center;
	private final ModelPart bottom;
	private final ModelPart top;
	private final ModelPart rim;
	private final ModelPart water;

	public WaterPotModel(ModelPart root) {
		super(
			WaterPotAnimation.init.bake(root),
			WaterPotAnimation.idle.bake(root),
			null,
			WaterPotAnimation.sleep.bake(root),
			null,
			root
		);
		this.body = root.getChild("body");
		this.flower = this.body.getChild("flower");
		this.center = this.flower.getChild("center");
		this.bottom = this.body.getChild("bottom");
		this.top = this.body.getChild("top");
		this.rim = this.top.getChild("rim");
		this.water = this.body.getChild("water");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -7.0F, -8.0F, 16.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition flower = body.addOrReplaceChild("flower", CubeListBuilder.create(), PartPose.offset(-2.7308F, -11.0F, 3.0463F));

		PartDefinition cube_r1 = flower.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 68).addBox(0.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0192F, 0.0F, -0.0463F, -2.4848F, 0.8934F, -1.0299F));

		PartDefinition cube_r2 = flower.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 68).mirror().addBox(-4.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0192F, 0.0F, -0.0463F, -0.6254F, 0.8584F, 1.0706F));

		PartDefinition cube_r3 = flower.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 68).mirror().addBox(-4.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0192F, 0.0F, -0.0463F, -1.8041F, -0.3189F, 0.6485F));

		PartDefinition cube_r4 = flower.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 68).addBox(0.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0192F, 0.0F, -0.0463F, -1.3518F, -0.3286F, -0.6037F));

		PartDefinition center = flower.addOrReplaceChild("center", CubeListBuilder.create(), PartPose.offset(0.0F, -1.1489F, 0.0F));

		PartDefinition cube_r5 = center.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(68, 17).addBox(0.15F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0192F, 1.1489F, -0.0463F, -1.9638F, -0.0403F, -1.5541F));

		PartDefinition cube_r6 = center.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(68, 17).addBox(0.15F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0192F, 1.1489F, -0.0463F, 2.7486F, -0.0403F, -1.5541F));

		PartDefinition bottom = body.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(56, 21).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition cube_r7 = bottom.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 36).addBox(-7.0F, -0.5F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition top = body.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 36).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(48, 51).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition rim = top.addOrReplaceChild("rim", CubeListBuilder.create().texOffs(64, 14).addBox(-5.0F, -1.0F, -7.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 68).addBox(-7.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 68).mirror().addBox(5.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition cube_r8 = rim.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(64, 14).mirror().addBox(-5.0F, -1.0F, -1.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition water = body.addOrReplaceChild("water", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition water2_r1 = water.addOrReplaceChild("water2_r1", CubeListBuilder.create().texOffs(0, 51).addBox(-6.0F, -3.0F, -2.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(56, 34).addBox(-5.0F, -7.0F, -1.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(64, 0).addBox(-5.0F, -1.0F, -1.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 4.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(@NotNull PlantRenderState state) {
		super.setupAnim(state);
		this.body.yRot = state.yRot * (float) (Math.PI / 180.0);
	}
}