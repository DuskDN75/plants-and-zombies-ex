package duskdn.plantz.model.plants;

import duskdn.plantz.renderer.entity.PlantRenderState;
import duskdn.plantz.animation.plants.FlowerPotAnimation;
import duskdn.plantz.animation.plants.PotatoMineAnimation;
import duskdn.plantz.model.plants.init.PazPlantModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jetbrains.annotations.NotNull;
import static duskdn.plantz.util.UtilsKt.pazResource;

public class FlowerPotModel extends PazPlantModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(pazResource("flowerpot"), "main");
	private final ModelPart body;
	private final ModelPart rim;
	private final ModelPart stem;
	private final ModelPart stem_2;
	private final ModelPart leaf_l;
	private final ModelPart leaf_r;

	public FlowerPotModel(ModelPart root) {
		super(
			FlowerPotAnimation.init.bake(root),
			FlowerPotAnimation.idle.bake(root),
			null,
			FlowerPotAnimation.sleep.bake(root),
			null,
			root
		);
		this.body = root.getChild("body");
		this.rim = this.body.getChild("rim");
		this.stem = this.body.getChild("stem");
		this.stem_2 = this.stem.getChild("stem_2");
		this.leaf_l = this.stem_2.getChild("leaf_l");
		this.leaf_r = this.stem_2.getChild("leaf_r");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.875F, -6.0F, 12.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.875F, 0.0F));

		PartDefinition rim = body.addOrReplaceChild("rim", CubeListBuilder.create().texOffs(0, 18).addBox(-7.0F, -2.0F, -1.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).mirror().addBox(5.0F, -2.0F, -1.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 34).addBox(-5.0F, -2.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.875F, -6.0F));

		PartDefinition cube_r1 = rim.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-5.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0F, 12.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition stem = body.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(0, 2).addBox(-0.5F, -3.05F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -5.825F, 4.9F));

		PartDefinition stem_2 = stem.addOrReplaceChild("stem_2", CubeListBuilder.create(), PartPose.offset(0.0F, -0.05F, 0.0F));

		PartDefinition leaf_l = stem_2.addOrReplaceChild("leaf_l", CubeListBuilder.create().texOffs(-2, 0).addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.054F, -2.9591F, -0.1636F, 0.8542F, -0.1037F, -0.2755F));

		PartDefinition leaf_r = stem_2.addOrReplaceChild("leaf_r", CubeListBuilder.create().texOffs(-2, 0).addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0384F, -2.9628F, -0.1377F, 2.5311F, 0.6479F, -2.7953F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull PlantRenderState state) {
		super.setupAnim(state);
		this.body.yRot = state.yRot * (float) (Math.PI / 180.0);
	}
}