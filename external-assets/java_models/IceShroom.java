// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class IceShroom<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "iceshroom"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart bottom_ice;
	private final ModelPart cap;
	private final ModelPart spikes;
	private final ModelPart front_spikes;
	private final ModelPart back_spikes;
	private final ModelPart spike;
	private final ModelPart spike2;

	public IceShroom(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.bottom_ice = this.head.getChild("bottom_ice");
		this.cap = this.head.getChild("cap");
		this.spikes = this.cap.getChild("spikes");
		this.front_spikes = this.spikes.getChild("front_spikes");
		this.back_spikes = this.spikes.getChild("back_spikes");
		this.spike = this.spikes.getChild("spike");
		this.spike2 = this.spikes.getChild("spike2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 27).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bottom_ice = head.addOrReplaceChild("bottom_ice", CubeListBuilder.create().texOffs(0, 13).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cap = head.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(0, 13).addBox(-6.0F, -1.875F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.125F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition spikes = cap.addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.875F, 0.0F));

		PartDefinition big_spike_r1 = spikes.addOrReplaceChild("big_spike_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-2.5F, -5.0F, -2.5F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.1136F, 0.1663F, 0.2296F, 0.2093F, 0.1154F));

		PartDefinition front_spikes = spikes.addOrReplaceChild("front_spikes", CubeListBuilder.create(), PartPose.offset(-3.4558F, -4.6308F, -6.4515F));

		PartDefinition spike_r1 = front_spikes.addOrReplaceChild("spike_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3466F, 1.3133F, 0.9513F, 2.285F, 0.538F, 2.3187F));

		PartDefinition spike_r2 = front_spikes.addOrReplaceChild("spike_r2", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3801F, 1.0447F, 0.766F, 2.3384F, 0.4263F, 2.6545F));

		PartDefinition spike_r3 = front_spikes.addOrReplaceChild("spike_r3", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5354F, 0.8607F, 0.423F, 2.209F, 0.6157F, 2.5017F));

		PartDefinition spike_r4 = front_spikes.addOrReplaceChild("spike_r4", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5027F, 1.4695F, 1.0395F, 2.2055F, 0.6284F, 2.6968F));

		PartDefinition spike_r5 = front_spikes.addOrReplaceChild("spike_r5", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5564F, 1.029F, 0.8948F, 1.8149F, 0.7932F, 2.1357F));

		PartDefinition back_spikes = spikes.addOrReplaceChild("back_spikes", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0511F, -3.334F, 5.7452F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition spike_r6 = back_spikes.addOrReplaceChild("spike_r6", CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, 0.0F, -0.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5069F, -1.2968F, -0.9467F, 0.8992F, -0.5938F, -0.9024F));

		PartDefinition spike_r7 = back_spikes.addOrReplaceChild("spike_r7", CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, 0.75F, -0.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5069F, -1.2968F, -0.9467F, 0.9398F, -0.6243F, -0.757F));

		PartDefinition spike_r8 = back_spikes.addOrReplaceChild("spike_r8", CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, 0.75F, -0.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7431F, -1.0468F, -0.9467F, 0.9583F, -0.6413F, -0.6837F));

		PartDefinition spike_r9 = back_spikes.addOrReplaceChild("spike_r9", CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, 0.75F, -0.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9931F, -1.0468F, -0.9467F, 0.991F, -0.6783F, -0.5351F));

		PartDefinition spike_r10 = back_spikes.addOrReplaceChild("spike_r10", CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, 0.0F, -0.5F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.9931F, -1.2968F, -0.9467F, 1.0662F, -0.6798F, -0.6213F));

		PartDefinition spike = spikes.addOrReplaceChild("spike", CubeListBuilder.create(), PartPose.offset(1.0F, -5.1136F, -2.8337F));

		PartDefinition spike_r11 = spike.addOrReplaceChild("spike_r11", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0903F, -1.246F, -0.8303F, 2.2594F, 0.7715F, 2.5111F));

		PartDefinition spike_r12 = spike.addOrReplaceChild("spike_r12", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8269F, -1.2266F, 2.2483F, -2.9747F, 0.0824F, -2.5346F));

		PartDefinition spike_r13 = spike.addOrReplaceChild("spike_r13", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9928F, 0.8182F, 5.1018F, -2.7861F, -0.1067F, -2.3415F));

		PartDefinition spike_r14 = spike.addOrReplaceChild("spike_r14", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8306F, 0.5397F, 1.5101F, 2.7129F, 0.5646F, 2.1932F));

		PartDefinition spike_r15 = spike.addOrReplaceChild("spike_r15", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.4632F, 2.3496F, 6.0044F, -2.541F, 0.6471F, 1.8801F));

		PartDefinition spike_r16 = spike.addOrReplaceChild("spike_r16", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, 5.25F, -2.5255F, 0.4581F, 2.9621F));

		PartDefinition spike_r17 = spike.addOrReplaceChild("spike_r17", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.25F, 1.5F, 6.75F, -2.2248F, 0.0266F, -3.107F));

		PartDefinition spike_r18 = spike.addOrReplaceChild("spike_r18", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5438F, 2.5121F, 5.7328F, -1.4495F, -0.8139F, -3.0668F));

		PartDefinition spike_r19 = spike.addOrReplaceChild("spike_r19", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0583F, 0.6153F, 3.1303F, -3.0141F, 0.2752F, -2.7759F));

		PartDefinition spike_r20 = spike.addOrReplaceChild("spike_r20", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 0.5F, 5.25F, -2.7847F, 0.2048F, -3.0659F));

		PartDefinition spike_r21 = spike.addOrReplaceChild("spike_r21", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8535F, -0.3436F, -0.6194F, 2.4107F, 0.3706F, 2.3265F));

		PartDefinition spike_r22 = spike.addOrReplaceChild("spike_r22", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.189F, -0.5773F, 3.0513F, -2.9418F, 0.2057F, 2.8192F));

		PartDefinition spike2 = spikes.addOrReplaceChild("spike2", CubeListBuilder.create(), PartPose.offset(-4.5994F, -1.7884F, -1.2168F));

		PartDefinition spike_r23 = spike2.addOrReplaceChild("spike_r23", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3953F, 0.4657F, -1.7145F, -2.8127F, 0.2723F, 1.2012F));

		PartDefinition spike_r24 = spike2.addOrReplaceChild("spike_r24", CubeListBuilder.create().texOffs(28, 0).addBox(-0.5F, -3.5F, -0.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.0861F, 1.0576F, 1.7294F));

		PartDefinition spike_r25 = spike2.addOrReplaceChild("spike_r25", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -3.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1999F, -1.1263F, -2.3754F, 2.7625F, 0.5964F, 2.2069F));

		PartDefinition spike_r26 = spike2.addOrReplaceChild("spike_r26", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.8615F, -1.0303F, 4.1211F, -2.6368F, -0.4363F, -2.9951F));

		PartDefinition spike_r27 = spike2.addOrReplaceChild("spike_r27", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.2432F, -0.0491F, -1.5461F, 0.328F, 0.0158F, 1.8839F));

		PartDefinition spike_r28 = spike2.addOrReplaceChild("spike_r28", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.7984F, -0.258F, 0.2338F, -0.1293F, -0.4367F, 1.5836F));

		PartDefinition spike_r29 = spike2.addOrReplaceChild("spike_r29", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.7334F, -2.4298F, -1.4466F, 2.1397F, 1.0882F, 2.7719F));

		PartDefinition spike_r30 = spike2.addOrReplaceChild("spike_r30", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.6684F, -2.7688F, -3.2497F, 1.5099F, 0.9464F, 1.7462F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}