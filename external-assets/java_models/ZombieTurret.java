// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ZombieTurret<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "zombieturret"), "main");
	private final ModelPart body;
	private final ModelPart neck;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart turrent;

	public ZombieTurret(ModelPart root) {
		this.body = root.getChild("body");
		this.neck = this.body.getChild("neck");
		this.head = this.neck.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.turrent = this.head.getChild("turrent");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 17).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.5F, -3.5F, -6.5F, 13.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(32, 57).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(52, 7).addBox(-4.0F, -2.0F, 1.0F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(48, 31).addBox(4.0F, -5.5F, -1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(48, 57).addBox(-7.0F, -6.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 31).addBox(-5.0F, -5.5F, -1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(83, 4).addBox(-3.5F, -2.0F, -3.75F, 7.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -5.0F, -1.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 57).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -5.0F, -1.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(83, 5).addBox(-3.5F, -1.0F, -4.75F, 7.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(52, 0).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 64).addBox(-4.0F, -6.0F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -2.0F, 1.0F));

		PartDefinition turrent = head.addOrReplaceChild("turrent", CubeListBuilder.create().texOffs(48, 16).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 4.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(32, 32).addBox(-2.5F, -4.5F, -5.5F, 5.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}