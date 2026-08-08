// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class PlantPot<T extends PlantPot> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "plantpot"), "main");
	private final ModelPart body;
	private final ModelPart rim;
	private final ModelPart stem;
	private final ModelPart stem_2;
	private final ModelPart leaf_l;
	private final ModelPart leaf_r;

	public PlantPot(ModelPart root) {
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
	public void setupAnim(PlantPot entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}