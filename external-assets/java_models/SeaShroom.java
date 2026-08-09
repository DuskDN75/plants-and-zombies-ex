// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class SeaShroom<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "seashroom"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart barrel;
	private final ModelPart cap;
	private final ModelPart roots;
	private final ModelPart rootTR;
	private final ModelPart rootTL;
	private final ModelPart rootTBR;
	private final ModelPart rootBL;
	private final ModelPart rootML;
	private final ModelPart rootMR;

	public SeaShroom(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.barrel = this.head.getChild("barrel");
		this.cap = this.head.getChild("cap");
		this.roots = this.body.getChild("roots");
		this.rootTR = this.roots.getChild("rootTR");
		this.rootTL = this.roots.getChild("rootTL");
		this.rootTBR = this.roots.getChild("rootTBR");
		this.rootBL = this.roots.getChild("rootBL");
		this.rootML = this.roots.getChild("rootML");
		this.rootMR = this.roots.getChild("rootMR");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -5.9826F, -3.0133F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 17).addBox(-1.0F, -2.4826F, -4.0133F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.0174F, 0.0133F));

		PartDefinition barrel = head.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(24, 13).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.4826F, -4.0133F));

		PartDefinition cap = head.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(2, 1).addBox(-3.5F, -3.3108F, -3.1947F, 7.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.1946F, -0.0719F, -0.0873F, 0.0F, 0.0F));

		PartDefinition roots = body.addOrReplaceChild("roots", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition rootTR = roots.addOrReplaceChild("rootTR", CubeListBuilder.create().texOffs(26, 24).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, -1.5F));

		PartDefinition rootTL = roots.addOrReplaceChild("rootTL", CubeListBuilder.create().texOffs(25, 24).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -1.5F));

		PartDefinition rootTBR = roots.addOrReplaceChild("rootTBR", CubeListBuilder.create().texOffs(26, 24).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 1.5F));

		PartDefinition rootBL = roots.addOrReplaceChild("rootBL", CubeListBuilder.create().texOffs(25, 24).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 1.5F));

		PartDefinition rootML = roots.addOrReplaceChild("rootML", CubeListBuilder.create().texOffs(25, 24).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, 0.0F, 0.0F));

		PartDefinition rootMR = roots.addOrReplaceChild("rootMR", CubeListBuilder.create().texOffs(26, 24).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.75F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}