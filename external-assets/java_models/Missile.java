// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Missile<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "missile"), "main");
	private final ModelPart rocket;

	public Missile(ModelPart root) {
		this.rocket = root.getChild("rocket");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition rocket = partdefinition.addOrReplaceChild("rocket", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(12, 0).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(0.0F, -2.0F, -3.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		rocket.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}