package duskdn.plantz_ex.model.plants;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import duskdn.plantz_ex.animation.plants.PlanternAnimation;
import duskdn.plantz_ex.model.plants.init.PazPlantModel;
import duskdn.plantz_ex.renderer.entity.PlantRenderState;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jetbrains.annotations.NotNull;

import static duskdn.plantz_ex.util.UtilsKt.pazResource;

public class PlanternModel extends PazPlantModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(pazResource("plantern"),"main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart bottom;
	private final ModelPart spikes;
	private final ModelPart interior;
	private final ModelPart inverted;
	private final ModelPart center;
	private final ModelPart top;
	private final ModelPart leaves;
	private final ModelPart leaves_group;
	private final ModelPart leaves_bundle;
	private final ModelPart leaf;
	private final ModelPart leaf2;
	private final ModelPart leaf3;
	private final ModelPart leaves_group2;
	private final ModelPart leaves_bundle2;
	private final ModelPart leaf4;
	private final ModelPart leaf5;
	private final ModelPart leaf6;
	private final ModelPart leaves_group3;
	private final ModelPart leaves_bundle3;
	private final ModelPart leaf7;
	private final ModelPart leaf8;
	private final ModelPart leaf9;
	private final ModelPart leaves_group4;
	private final ModelPart leaves_bundle4;
	private final ModelPart leaf10;
	private final ModelPart leaf11;
	private final ModelPart leaf12;
	private final ModelPart edges;
	private final ModelPart edge2;
	private final ModelPart edge3;
	private final ModelPart edge4;
	private final ModelPart edge;
	private final ModelPart eyes;
	private final ModelPart left_eye;
	private final ModelPart right_eye;
	private final ModelPart stem;
	private final ModelPart roots;

	public PlanternModel(ModelPart root) {
		super(
				PlanternAnimation.init.bake(root),
				PlanternAnimation.idle.bake(root),
				null,
				PlanternAnimation.sleep.bake(root),
				null,
				root
		);
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.bottom = this.head.getChild("bottom");
		this.spikes = this.bottom.getChild("spikes");
		this.interior = this.head.getChild("interior");
		this.inverted = this.interior.getChild("inverted");
		this.center = this.inverted.getChild("center");
		this.top = this.head.getChild("top");
		this.leaves = this.top.getChild("leaves");
		this.leaves_group = this.leaves.getChild("leaves_group");
		this.leaves_bundle = this.leaves_group.getChild("leaves_bundle");
		this.leaf = this.leaves_bundle.getChild("leaf");
		this.leaf2 = this.leaves_bundle.getChild("leaf2");
		this.leaf3 = this.leaves_bundle.getChild("leaf3");
		this.leaves_group2 = this.leaves.getChild("leaves_group2");
		this.leaves_bundle2 = this.leaves_group2.getChild("leaves_bundle2");
		this.leaf4 = this.leaves_bundle2.getChild("leaf4");
		this.leaf5 = this.leaves_bundle2.getChild("leaf5");
		this.leaf6 = this.leaves_bundle2.getChild("leaf6");
		this.leaves_group3 = this.leaves.getChild("leaves_group3");
		this.leaves_bundle3 = this.leaves_group3.getChild("leaves_bundle3");
		this.leaf7 = this.leaves_bundle3.getChild("leaf7");
		this.leaf8 = this.leaves_bundle3.getChild("leaf8");
		this.leaf9 = this.leaves_bundle3.getChild("leaf9");
		this.leaves_group4 = this.leaves.getChild("leaves_group4");
		this.leaves_bundle4 = this.leaves_group4.getChild("leaves_bundle4");
		this.leaf10 = this.leaves_bundle4.getChild("leaf10");
		this.leaf11 = this.leaves_bundle4.getChild("leaf11");
		this.leaf12 = this.leaves_bundle4.getChild("leaf12");
		this.edges = this.head.getChild("edges");
		this.edge2 = this.edges.getChild("edge2");
		this.edge3 = this.edges.getChild("edge3");
		this.edge4 = this.edges.getChild("edge4");
		this.edge = this.edges.getChild("edge");
		this.eyes = this.head.getChild("eyes");
		this.left_eye = this.eyes.getChild("left_eye");
		this.right_eye = this.eyes.getChild("right_eye");
		this.stem = this.body.getChild("stem");
		this.roots = this.stem.getChild("roots");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition bottom = head.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition spikes = bottom.addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = spikes.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 19).mirror().addBox(-2.0F, -1.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.6828F, 0.0F, 4.6828F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r2 = spikes.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 19).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.6828F, 0.0F, -4.7172F, -3.1416F, 0.7854F, 3.1416F));

		PartDefinition cube_r3 = spikes.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 19).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7172F, 0.0F, 4.6828F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r4 = spikes.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 19).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7172F, 0.0F, -4.7172F, 0.0F, 0.7854F, 0.0F));

		PartDefinition interior = head.addOrReplaceChild("interior", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition inverted = interior.addOrReplaceChild("inverted", CubeListBuilder.create().texOffs(41, 49).addBox(3.5F, -4.0F, -3.6F, 0.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(41, 49).addBox(-3.5F, -4.0F, -3.6F, 0.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(41, 56).addBox(-3.5F, -4.0F, -3.6F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(41, 56).addBox(-3.5F, -4.0F, 3.4F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(41, 49).addBox(-3.5F, -4.0F, -3.6F, 7.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = inverted.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(41, 49).addBox(-3.5F, 0.0F, -3.5F, 7.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -0.1F, 0.0F, 0.0F, -3.1416F));

		PartDefinition center = inverted.addOrReplaceChild("center", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r6 = center.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 52).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition top = head.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 2.0F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 13).addBox(-5.0F, 1.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(32, 32).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(24, 33).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition leaves = top.addOrReplaceChild("leaves", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leaves_group = leaves.addOrReplaceChild("leaves_group", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 2.0F, -6.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition leaves_bundle = leaves_group.addOrReplaceChild("leaves_bundle", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition leaf = leaves_bundle.addOrReplaceChild("leaf", CubeListBuilder.create(), PartPose.offset(0.0F, 0.2F, 0.0F));

		PartDefinition cube_r7 = leaf.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(40, 13).addBox(-2.0F, -0.0038F, -1.4135F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaf2 = leaves_bundle.addOrReplaceChild("leaf2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, -0.7543F, 1.0823F, -0.8165F));

		PartDefinition cube_r8 = leaf2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.4514F, -0.0215F, -1.3826F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaf3 = leaves_bundle.addOrReplaceChild("leaf3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, 0.7543F, -1.0823F, -0.8165F));

		PartDefinition cube_r9 = leaf3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(40, 16).addBox(-1.5486F, -0.0215F, -1.3826F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaves_group2 = leaves.addOrReplaceChild("leaves_group2", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 2.0F, -6.0F, 0.0F, -2.3562F, 0.0F));

		PartDefinition leaves_bundle2 = leaves_group2.addOrReplaceChild("leaves_bundle2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition leaf4 = leaves_bundle2.addOrReplaceChild("leaf4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.2F, 0.0F));

		PartDefinition cube_r10 = leaf4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(40, 13).addBox(-2.0F, -0.0038F, -1.4135F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaf5 = leaves_bundle2.addOrReplaceChild("leaf5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, -0.7543F, 1.0823F, -0.8165F));

		PartDefinition cube_r11 = leaf5.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.4514F, -0.0215F, -1.3826F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaf6 = leaves_bundle2.addOrReplaceChild("leaf6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, 0.7543F, -1.0823F, -0.8165F));

		PartDefinition cube_r12 = leaf6.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(40, 16).addBox(-1.5486F, -0.0215F, -1.3826F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaves_group3 = leaves.addOrReplaceChild("leaves_group3", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 2.0F, 6.0F, 0.0F, 2.3562F, 0.0F));

		PartDefinition leaves_bundle3 = leaves_group3.addOrReplaceChild("leaves_bundle3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition leaf7 = leaves_bundle3.addOrReplaceChild("leaf7", CubeListBuilder.create(), PartPose.offset(0.0F, 0.2F, 0.0F));

		PartDefinition cube_r13 = leaf7.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(40, 13).addBox(-2.0F, -0.0038F, -1.4135F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaf8 = leaves_bundle3.addOrReplaceChild("leaf8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, -0.7543F, 1.0823F, -0.8165F));

		PartDefinition cube_r14 = leaf8.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.4514F, -0.0215F, -1.3826F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaf9 = leaves_bundle3.addOrReplaceChild("leaf9", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, 0.7543F, -1.0823F, -0.8165F));

		PartDefinition cube_r15 = leaf9.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(40, 16).addBox(-1.5486F, -0.0215F, -1.3826F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaves_group4 = leaves.addOrReplaceChild("leaves_group4", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 2.0F, 6.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition leaves_bundle4 = leaves_group4.addOrReplaceChild("leaves_bundle4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition leaf10 = leaves_bundle4.addOrReplaceChild("leaf10", CubeListBuilder.create(), PartPose.offset(0.0F, 0.2F, 0.0F));

		PartDefinition cube_r16 = leaf10.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(40, 13).addBox(-2.0F, -0.0038F, -1.4135F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaf11 = leaves_bundle4.addOrReplaceChild("leaf11", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, -0.7543F, 1.0823F, -0.8165F));

		PartDefinition cube_r17 = leaf11.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.4514F, -0.0215F, -1.3826F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition leaf12 = leaves_bundle4.addOrReplaceChild("leaf12", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.2F, 0.0F, 0.7543F, -1.0823F, -0.8165F));

		PartDefinition cube_r18 = leaf12.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(40, 16).addBox(-1.5486F, -0.0215F, -1.3826F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.2047F, 0.4933F, 0.0F, -1.5708F, 3.1416F));

		PartDefinition edges = head.addOrReplaceChild("edges", CubeListBuilder.create(), PartPose.offset(1.0F, -6.0F, -1.0F));

		PartDefinition edge2 = edges.addOrReplaceChild("edge2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r19 = edge2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(40, 39).mirror().addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5F, -1.0F, -3.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r20 = edge2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(40, 39).mirror().addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, 3.0F, -2.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition edge3 = edges.addOrReplaceChild("edge3", CubeListBuilder.create(), PartPose.offset(-7.0F, 0.0F, 0.0F));

		PartDefinition cube_r21 = edge3.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(40, 39).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -1.0F, -3.5F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r22 = edge3.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(40, 39).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 3.0F, -2.5F, 0.0F, -1.5708F, 0.0F));

		PartDefinition edge4 = edges.addOrReplaceChild("edge4", CubeListBuilder.create().texOffs(40, 39).addBox(2.0F, 1.0F, -3.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(40, 39).addBox(1.0F, -3.0F, -2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 0.0F, 7.0F));

		PartDefinition edge = edges.addOrReplaceChild("edge", CubeListBuilder.create().texOffs(40, 39).mirror().addBox(3.0F, -3.0F, -4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(40, 39).mirror().addBox(2.0F, 1.0F, -5.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 9.0F));

		PartDefinition eyes = head.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(3.5F, -3.0F, 3.5F));

		PartDefinition left_eye = eyes.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(50, 40).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.0F, -7.5F));

		PartDefinition right_eye = eyes.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(44, 40).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, -2.0F, -7.5F));

		PartDefinition stem = body.addOrReplaceChild("stem", CubeListBuilder.create().texOffs(0, 39).addBox(-2.0F, -7.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition roots = stem.addOrReplaceChild("roots", CubeListBuilder.create().texOffs(28, 39).addBox(0.0F, -6.0F, -6.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r23 = roots.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(28, 39).addBox(0.0F, -6.0F, -6.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 2.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r24 = roots.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(28, 39).addBox(0.0F, -6.0F, -4.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 2.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r25 = roots.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(28, 39).addBox(0.0F, -6.0F, -4.0F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull PlantRenderState state) {
		this.head.xRot += state.xRot * (float) (Math.PI / 180.0);
		super.setupAnim(state);
		this.head.yRot += state.yRot * (float) (Math.PI / 180.0);
	}
}