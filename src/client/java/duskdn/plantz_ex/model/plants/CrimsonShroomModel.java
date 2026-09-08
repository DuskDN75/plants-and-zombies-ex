package duskdn.plantz_ex.model.plants;

import duskdn.plantz_ex.animation.plants.PuffShroomAnimation;
import duskdn.plantz_ex.animation.plants.init.CrimsonShroomAnimation;
import duskdn.plantz_ex.model.plants.init.PazPlantModel;
import duskdn.plantz_ex.renderer.entity.PlantRenderState;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jetbrains.annotations.NotNull;

import static duskdn.plantz_ex.util.UtilsKt.pazResource;


public class CrimsonShroomModel extends PazPlantModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(pazResource("crimsonshroom"), "main");
	private final ModelPart body;
	private final ModelPart stem;
	private final ModelPart head;
	private final ModelPart top;
	private final ModelPart barrel;

	public CrimsonShroomModel(ModelPart root) {
		super(
				CrimsonShroomAnimation.init.bake(root),
				CrimsonShroomAnimation.idle.bake(root),
				CrimsonShroomAnimation.action.bake(root),
				CrimsonShroomAnimation.sleep.bake(root),
				null,
				root
		);
		this.body = root.getChild("body");
		this.stem = this.body.getChild("stem");
		this.head = this.stem.getChild("head");
		this.top = this.head.getChild("top");
		this.barrel = this.top.getChild("barrel");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 25).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(24, 29).addBox(-1.0F, -3.0F, -4.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition barrel = head.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(24, 25).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));

		PartDefinition cap = head.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 6.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, 0.0F, -0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull PlantRenderState state) {
		super.setupAnim(state);
		this.head.yRot = state.yRot * (float) (Math.PI / 180.0);
	}
}