// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class lilypad<T extends LilyPad> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "lilypad"), "main");
	private final ModelPart pad;
	private final ModelPart wall;
	private final ModelPart eyes;
	private final ModelPart rightEye;
	private final ModelPart eyebrows;
	private final ModelPart eyebrow_R;
	private final ModelPart eyebrow_L;
	private final ModelPart leftEye;
	private final ModelPart flower;
	private final ModelPart center;
	private final ModelPart floor;
	private final ModelPart stem;

	public lilypad(ModelPart root) {
		this.pad = root.getChild("pad");
		this.wall = this.pad.getChild("wall");
		this.eyes = this.wall.getChild("eyes");
		this.rightEye = this.eyes.getChild("rightEye");
		this.eyebrows = this.eyes.getChild("eyebrows");
		this.eyebrow_R = this.eyebrows.getChild("eyebrow_R");
		this.eyebrow_L = this.eyebrows.getChild("eyebrow_L");
		this.leftEye = this.eyes.getChild("leftEye");
		this.flower = this.pad.getChild("flower");
		this.center = this.flower.getChild("center");
		this.floor = this.pad.getChild("floor");
		this.stem = this.pad.getChild("stem");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition pad = partdefinition.addOrReplaceChild("pad", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition wall = pad.addOrReplaceChild("wall", CubeListBuilder.create().texOffs(44, 7).addBox(-1.0F, -2.0F, -3.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(32, 45).addBox(-1.0F, -2.0F, 3.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 46).addBox(-14.0F, -2.0F, 3.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(46, 0).addBox(-14.0F, -2.0F, -3.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 35).addBox(0.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(12, 35).addBox(-14.0F, -2.0F, -3.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(8, 43).addBox(-1.0F, -2.0F, 3.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 42).addBox(-1.0F, -2.0F, -5.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(46, 1).addBox(-2.0F, -2.0F, -5.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(34, 44).addBox(-13.0F, -2.0F, 3.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 43).addBox(-13.0F, -2.0F, -5.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(2, 46).addBox(-13.0F, -2.0F, -5.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(38, 10).addBox(-4.0F, -2.0F, -6.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(24, 37).addBox(-2.0F, -2.0F, -6.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 38).addBox(-12.0F, -2.0F, -6.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 10).addBox(-12.0F, -2.0F, -6.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(35, 7).addBox(-7.0F, -2.0F, 7.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(35, 7).addBox(-10.0F, -2.0F, 7.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(20, 45).addBox(-10.0F, -2.0F, 6.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 45).addBox(-4.0F, -2.0F, 6.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 43).addBox(-5.0F, -2.0F, -7.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(30, 45).addBox(-4.0F, -2.0F, -7.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 35).addBox(-6.0F, -2.0F, -5.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 45).addBox(-7.0F, -2.0F, -4.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 45).addBox(-9.0F, -2.0F, -7.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(46, 2).addBox(-10.0F, -2.0F, -7.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(24, 39).addBox(-9.0F, -2.0F, -6.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(46, 5).addBox(-7.0F, -2.0F, -4.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(4, 46).addBox(-6.0F, -2.0F, -5.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(46, 4).addBox(-5.0F, -2.0F, -7.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(42, 42).addBox(-8.0F, -2.0F, -6.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(28, 45).addBox(-10.0F, -2.0F, -7.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 0.0F, 0.0F));

		PartDefinition eyes = wall.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(-11.0F, -1.0F, 0.0F));

		PartDefinition rightEye = eyes.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(42, 38).mirror().addBox(-2.0F, -2.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(9.0F, 0.0F, 0.0F));

		PartDefinition eyebrows = eyes.addOrReplaceChild("eyebrows", CubeListBuilder.create(), PartPose.offset(8.0F, 0.0F, 0.0F));

		PartDefinition eyebrow_R = eyebrows.addOrReplaceChild("eyebrow_R", CubeListBuilder.create(), PartPose.offset(0.5F, -0.5F, 6.0F));

		PartDefinition eyebrow_R_r1 = eyebrow_R.addOrReplaceChild("eyebrow_R_r1", CubeListBuilder.create().texOffs(39, 8).addBox(-2.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -0.5F, 0.0F, -0.6981F, 0.0F, 0.0F));

		PartDefinition eyebrow_L = eyebrows.addOrReplaceChild("eyebrow_L", CubeListBuilder.create(), PartPose.offset(-8.5F, -0.5F, 6.0F));

		PartDefinition eyebrow_L_r1 = eyebrow_L.addOrReplaceChild("eyebrow_L_r1", CubeListBuilder.create().texOffs(39, 9).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition leftEye = eyes.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(42, 38).addBox(-1.0F, -2.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

		PartDefinition flower = pad.addOrReplaceChild("flower", CubeListBuilder.create(), PartPose.offset(-4.7873F, -1.8887F, -1.9252F));

		PartDefinition cube_r1 = flower.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(38, 50).addBox(0.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0373F, 0.8887F, -0.0748F, -2.4848F, 0.8934F, -1.0299F));

		PartDefinition cube_r2 = flower.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(38, 50).mirror().addBox(-4.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0373F, 0.8887F, -0.0748F, -0.6254F, 0.8584F, 1.0706F));

		PartDefinition cube_r3 = flower.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(38, 50).mirror().addBox(-4.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0373F, 0.8887F, -0.0748F, -1.8041F, -0.3189F, 0.6485F));

		PartDefinition cube_r4 = flower.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(38, 50).addBox(0.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0373F, 0.8887F, -0.0748F, -1.3518F, -0.3286F, -0.6037F));

		PartDefinition center = flower.addOrReplaceChild("center", CubeListBuilder.create(), PartPose.offset(7.2873F, 5.8887F, -6.1747F));

		PartDefinition cube_r5 = center.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(37, 47).mirror().addBox(0.15F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.25F, -5.0F, 6.1F, -1.9638F, -0.0403F, -1.5541F));

		PartDefinition cube_r6 = center.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(37, 47).mirror().addBox(0.15F, -1.5F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.25F, -5.0F, 6.1F, 2.7486F, -0.0403F, -1.5541F));

		PartDefinition floor = pad.addOrReplaceChild("floor", CubeListBuilder.create().texOffs(26, 33).addBox(12.0F, -1.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(-16, 0).addBox(-2.0F, -0.25F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(3.0F, -1.0F, -3.0F, 6.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(24, 40).addBox(3.0F, -1.0F, -7.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 33).addBox(8.0F, -1.0F, -7.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(34, 40).addBox(4.0F, -1.0F, -6.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(38, 44).addBox(6.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 8).addBox(7.0F, -1.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(1.0F, -1.0F, -6.0F, 2.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 23).addBox(9.0F, -1.0F, -6.0F, 2.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(26, 11).addBox(11.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(26, 22).addBox(0.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-1.0F, -1.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition stem = pad.addOrReplaceChild("stem", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition cube_r7 = stem.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 47).mirror().addBox(-3.0F, -2.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r8 = stem.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 47).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(LilyPad entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		pad.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}