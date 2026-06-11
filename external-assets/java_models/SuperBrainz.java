// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class SuperBrainz<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "superbrainz"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart electro_brainz;
	private final ModelPart toxic_brainz;
	private final ModelPart body;
	private final ModelPart cape;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public SuperBrainz(ModelPart root) {
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.hat = this.head.getChild("hat");
		this.electro_brainz = this.head.getChild("electro_brainz");
		this.toxic_brainz = this.head.getChild("toxic_brainz");
		this.body = this.root.getChild("body");
		this.cape = this.body.getChild("cape");
		this.right_arm = this.root.getChild("right_arm");
		this.left_arm = this.root.getChild("left_arm");
		this.right_leg = this.root.getChild("right_leg");
		this.left_leg = this.root.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 44).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 61).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -17.0F, -1.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(66, 68).addBox(-3.0F, -2.0F, -1.0F, 6.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.75F, -4.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 29.0F, 1.0F));

		PartDefinition electro_brainz = head.addOrReplaceChild("electro_brainz", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition cube_r2 = electro_brainz.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(112, 44).addBox(-8.0F, -3.5F, 0.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -1.5F, -1.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r3 = electro_brainz.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(112, 35).addBox(0.0F, -3.5F, 0.0F, 8.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -1.5F, -1.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition toxic_brainz = head.addOrReplaceChild("toxic_brainz", CubeListBuilder.create().texOffs(88, 11).addBox(-5.0F, 3.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(88, 23).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(56, 12).addBox(-5.0F, -4.0F, -1.0F, 10.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(100, 0).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(88, 9).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(91, 13).addBox(-0.5F, -9.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(82, 4).addBox(-2.5F, -8.0F, -2.5F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(88, 9).addBox(6.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(91, 16).addBox(5.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(91, 16).addBox(-6.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(88, 9).addBox(-8.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(57, 25).addBox(-5.0F, -6.0F, -2.0F, 10.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-8.0F, -17.0F, -3.0F, 16.0F, 11.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(39, 1).addBox(-2.0F, -15.0F, -5.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cape = body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(0, 77).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 17.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, 4.0F));

		PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 44).addBox(0.0F, -2.0F, -3.0F, 6.0F, 18.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 18).addBox(-0.55F, -2.6F, -3.45F, 7.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -15.0F, 0.0F));

		PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 44).addBox(-6.0F, -2.0F, -3.0F, 6.0F, 18.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-6.55F, -2.6F, -3.45F, 7.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -15.0F, 0.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(50, 68).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(34, 68).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(54, 84).addBox(-2.0F, 9.0F, -4.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(34, 84).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}