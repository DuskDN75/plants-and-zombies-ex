package duskdn.plantz_ex.model.projectiles;

import duskdn.plantz_ex.renderer.entity.ProjectileRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jetbrains.annotations.NotNull;

import static duskdn.plantz_ex.util.UtilsKt.pazResource;

public class ShroomLightModel extends EntityModel<@NotNull ProjectileRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(pazResource("shroomlight"), "main");
    private final ModelPart body;

    public ShroomLightModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.0F, 2.0F, 2.0F, -4.0F, -4.0F, -4.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    public void setupAnim(final @NotNull ProjectileRenderState state) {
        super.setupAnim(state);
    }
}
