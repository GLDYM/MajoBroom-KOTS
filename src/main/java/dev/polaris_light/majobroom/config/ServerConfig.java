package dev.polaris_light.majobroom.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 服务端配置（SERVER类型）
 * - 配置文件存储在: world/serverconfig/majobroom-server.toml
 * - 会自动从服务端同步到客户端
 * - 适用于游戏规则、平衡性等需要统一的设置
 */
public class ServerConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // 飞行设置
    static {
        BUILDER.comment("Flight Settings").push("flight");
        MAX_SPEED_SPEC = BUILDER
                .comment("Max Horizontal Speed (blocks/tick) (Acceration also needed to change)")
                .defineInRange("maxSpeed", 0.9D, 0.3D, 2.0D);
        ACCELERATION_SPEC = BUILDER
                .comment("Horizontal Acceleration")
                .defineInRange("acceleration", 0.08D, 0.04D, 0.1D);
        MAX_VERTICAL_SPEED_SPEC = BUILDER
                .comment("Max Vertical Speed (blocks/tick）")
                .defineInRange("maxVerticalSpeed", 0.5D, 0.1D, 2.0D);
        VERTICAL_ACCELERATION_SPEC = BUILDER
                .comment("Vertical Acceleration")
                .defineInRange("verticalAcceleration", 0.05D, 0.001D, 0.5D);
        MOMENTUM_SPEC = BUILDER
                .comment("Momentum (Larger will make broom stop faster)")
                .defineInRange("momentum", 0.92D, 0.5D, 0.99D);
        GROUND_CHECK_OFFSET_SPEC = BUILDER
                .comment("Ground Check Offset (blocks)")
                .defineInRange("groundCheckOffset", 0.02D, 0.0D, 0.1D);
        GROUND_REPULSION_SPEC = BUILDER
                .comment("Ground Repulsion (The force of ground pushing the broom back)")
                .defineInRange("groundRepulsion", 0.005D, 0.0D, 0.1D);
        NO_ARMOR_SPEED_PENALTY_SPEC = BUILDER
                .comment("No Armor Speed Penalty (1.0 means no penalty, 0.6 means speed reduced to 60%)")
                .defineInRange("noArmorSpeedPenalty", 0.38D, 0.1D, 1.0D);
        BUILDER.pop();
        BUILDER.comment("Majo Armor Settings").push("armor");
        ARMOR_OVERPOWER_SPEC = BUILDER
                .comment("Whether to enable Majo armor stat enhancement (including iron's spellbooks attributes)")
                .define("armorOverpower", false);
        ARMOR_BLESS_SPEC = BUILDER
                .comment("Whether to enable Majo armor blessings (effects)")
                .define("armorBless", false);
        ARMOR_IMMORTAL_SPEC = BUILDER
                .comment("Whether to enable Majo armor to not take damage")
                .define("armorImmortal", true);
        BUILDER.pop();
    }

    // 配置规范
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    // ============ ForgeConfigSpec 值（配置定义） ============
    private static ForgeConfigSpec.DoubleValue MAX_SPEED_SPEC;
    private static ForgeConfigSpec.DoubleValue ACCELERATION_SPEC;
    private static ForgeConfigSpec.DoubleValue MAX_VERTICAL_SPEED_SPEC;
    private static ForgeConfigSpec.DoubleValue VERTICAL_ACCELERATION_SPEC;
    private static ForgeConfigSpec.DoubleValue MOMENTUM_SPEC;
    private static ForgeConfigSpec.DoubleValue GROUND_CHECK_OFFSET_SPEC;
    private static ForgeConfigSpec.DoubleValue GROUND_REPULSION_SPEC;
    private static ForgeConfigSpec.DoubleValue NO_ARMOR_SPEED_PENALTY_SPEC;
    private static ForgeConfigSpec.BooleanValue ARMOR_OVERPOWER_SPEC;
    private static ForgeConfigSpec.BooleanValue ARMOR_BLESS_SPEC;
    private static ForgeConfigSpec.BooleanValue ARMOR_IMMORTAL_SPEC;

    // ============ 缓存值（实际使用，性能优化） ============
    // 飞行参数
    public static double maxSpeed;
    public static double acceleration;
    public static double maxVerticalSpeed;
    public static double verticalAcceleration;
    public static double momentum;
    public static double groundCheckOffset;
    public static double groundRepulsion;
    public static double noArmorSpeedPenalty;
    public static boolean armorOverpower;
    public static boolean armorBless;
    public static boolean armorImmortal;

    /**
     * 将配置值烘焙到缓存变量中（由 ConfigEvents 调用）
     * 这样在运行时使用缓存值，避免每次都调用 .get()
     */
    public static void bake() {
        // 飞行参数
        maxSpeed = MAX_SPEED_SPEC.get();
        acceleration = ACCELERATION_SPEC.get();
        maxVerticalSpeed = MAX_VERTICAL_SPEED_SPEC.get();
        verticalAcceleration = VERTICAL_ACCELERATION_SPEC.get();
        momentum = MOMENTUM_SPEC.get();
        groundCheckOffset = GROUND_CHECK_OFFSET_SPEC.get();
        groundRepulsion = GROUND_REPULSION_SPEC.get();
        noArmorSpeedPenalty = NO_ARMOR_SPEED_PENALTY_SPEC.get();
        armorOverpower = ARMOR_OVERPOWER_SPEC.get();
        armorBless = ARMOR_BLESS_SPEC.get();
        armorImmortal = ARMOR_IMMORTAL_SPEC.get();
    }
}

