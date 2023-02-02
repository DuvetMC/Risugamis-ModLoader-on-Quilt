package org.duvetmc.risugami.risuimpl;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.imageio.ImageIO;

import net.fabricmc.tinyremapper.TinyRemapper;
import net.fabricmc.tinyremapper.TinyUtils;
import net.minecraft.class_1;
import net.minecraft.class_123;
import net.minecraft.class_14;
import net.minecraft.class_18;
import net.minecraft.class_209;
import net.minecraft.class_214;
import net.minecraft.class_215;
import net.minecraft.class_229;
import net.minecraft.class_253;
import net.minecraft.class_255;
import net.minecraft.class_262;
import net.minecraft.class_286;
import net.minecraft.class_3;
import net.minecraft.class_356;
import net.minecraft.class_379;
import net.minecraft.class_400;
import net.minecraft.class_413;
import net.minecraft.class_414;
import net.minecraft.class_447;
import net.minecraft.class_480;
import net.minecraft.class_49;
import net.minecraft.class_5;
import net.minecraft.class_611;
import net.minecraft.class_614;
import net.minecraft.class_618;
import net.minecraft.class_644;
import net.minecraft.class_669;
import net.minecraft.class_67;
import net.minecraft.class_679;
import net.minecraft.class_683;
import net.minecraft.class_750;
import net.minecraft.class_757;
import net.minecraft.class_764;
import net.minecraft.class_817;
import net.minecraft.class_844;
import net.minecraft.class_876;
import net.minecraft.class_89;
import net.minecraft.client.Minecraft;
import org.duvetmc.risugami.impl.C215Duck;
import org.duvetmc.risugami.mixin.McInstanceGetter;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.impl.QuiltLoaderImpl;
import org.quiltmc.loader.impl.game.minecraft.Hooks;
import org.quiltmc.loader.impl.game.minecraft.MinecraftGameProvider;
import org.spongepowered.tools.obfuscation.mapping.common.MappingProvider;
import sschr15.tools.qblo.MemberAccess;
import sschr15.tools.qblo.Unsafe;

@SuppressWarnings({"unchecked", "unused"})
public final class ModLoader {
    private static final List<class_89> animList = new LinkedList<>();
    private static final Map<Integer, BaseMod> blockModels = new HashMap<>();
    private static final Map<Integer, Boolean> blockSpecialInv = new HashMap<>();
    private static final File cfgdir = new File(Minecraft.method_1208(), "/config/");
    private static final File cfgfile = new File(cfgdir, "ModLoader.cfg");
    public static Level cfgLoggingLevel = Level.FINER;
    private static Map<String, Class<? extends class_1>> classMap = null;
    private static long clock = 0L;
    public static final boolean DEBUG = false;
    private static boolean hasInit = false;
    private static int highestEntityId = 3000;
    private static final Map<BaseMod, Boolean> inGameHooks = new HashMap<>();
    private static final Map<BaseMod, Boolean> inGUIHooks = new HashMap<>();
    private static Minecraft instance = null;
    private static int itemSpriteIndex = 0;
    private static int itemSpritesLeft = 0;
    private static final Map<BaseMod, Map<class_123, boolean[]>> keyList = new HashMap<>();
    private static final File logfile = new File(Minecraft.method_1208(), "ModLoader.txt");
    private static final Logger logger = Logger.getLogger("ModLoader");
    private static FileHandler logHandler = null;
    private static final File modDir = new File(Minecraft.method_1208(), "/mods/");
    private static final LinkedList<BaseMod> modList = new LinkedList<>();
    private static int nextBlockModelID = 1000;
    private static final Map<Integer, Map<String, Integer>> overrides = new HashMap<>();
    public static final Properties props = new Properties();
    private static class_611[] standardBiomes;
    private static int terrainSpriteIndex = 0;
    private static int terrainSpritesLeft = 0;
    private static String texPack = null;
    private static boolean texturesAdded = false;
    private static final boolean[] usedItemSprites = new boolean[256];
    private static final boolean[] usedTerrainSprites = new boolean[256];
    public static final String VERSION = "ModLoader Beta 1.7.3";

    public static void AddAchievementDesc(class_817 achievement, String name, String description) {
        try {
            if (achievement.field_3732.contains(".")) {
                String[] split = achievement.field_3732.split("\\.");
                if (split.length == 2) {
                    String key = split[1];
                    AddLocalization("achievement." + key, name);
                    AddLocalization("achievement." + key + ".desc", description);
                    setPrivateValue(class_844.class, achievement, 1, class_669.method_2120().method_2121("achievement." + key));
                    setPrivateValue(class_817.class, achievement, 3, class_669.method_2120().method_2121("achievement." + key + ".desc"));
                } else {
                    setPrivateValue(class_844.class, achievement, 1, name);
                    setPrivateValue(class_817.class, achievement, 3, description);
                }
            } else {
                setPrivateValue(class_844.class, achievement, 1, name);
                setPrivateValue(class_817.class, achievement, 3, description);
            }
        } catch (IllegalArgumentException | SecurityException | NoSuchFieldException var5) {
            logger.throwing("ModLoader", "AddAchievementDesc", var5);
            ThrowException(var5);
        }
    }

    public static int AddAllFuel(int id) {
        logger.finest("Finding fuel for " + id);
        int result = 0;
        Iterator<BaseMod> iter = modList.iterator();

        while(iter.hasNext() && result == 0) {
            result = iter.next().AddFuel(id);
        }

        if (result != 0) {
            logger.finest("Returned " + result);
        }

        return result;
    }

    public static void AddAllRenderers(Map<Class<? extends class_1>, class_214> o) {
        if (!hasInit) {
            init();
            logger.fine("Initialized");
        }

        for(BaseMod mod : modList) {
            mod.AddRenderer(o);
        }
    }

    public static void addAnimation(class_89 anim) {
        logger.finest("Adding animation " + anim.toString());

        for(class_89 oldAnim : animList) {
            if (oldAnim.field_2757 == anim.field_2757 && oldAnim.field_511 == anim.field_511) {
                animList.remove(anim);
                break;
            }
        }

        animList.add(anim);
    }

    public static int AddArmor(String armor) {
        try {
            List<String> existingArmorList = Arrays.asList(class_356.field_1730);
            List<String> combinedList = new ArrayList<>(existingArmorList);
            if (!combinedList.contains(armor)) {
                combinedList.add(armor);
            }

            int index = combinedList.indexOf(armor);
            class_356.field_1730 = combinedList.toArray(new String[0]);
            return index;
        } catch (IllegalArgumentException var5) {
            logger.throwing("ModLoader", "AddArmor", var5);
            ThrowException("An impossible error has occured!", var5);
        }

        return -1;
    }

    public static void AddLocalization(String key, String value) {
        Properties props = null;

        try {
            props = getPrivateValue(class_669.class, class_669.method_2120(), 1);
        } catch (SecurityException | NoSuchFieldException var4) {
            logger.throwing("ModLoader", "AddLocalization", var4);
            ThrowException(var4);
        }

        if (props != null) {
            props.put(key, value);
        }
    }

    private static void remapMod(Path path, Path cachePath) throws IOException {
        if (!Files.exists(path)) {
            throw new FileNotFoundException(path.toString());
        }

		var provider = ((MinecraftGameProvider) QuiltLoaderImpl.INSTANCE.getGameProvider());
		if (!provider.isObfuscated()) {
            // No remapping necessary
            Files.copy(path, cachePath);
            return;
        }

		var mappingResolver = QuiltLoaderImpl.INSTANCE.getMappingResolver();

		if (path.toString().endsWith(".jar") || path.toString().endsWith(".zip")) {
			try (FileSystem in = FileSystems.newFileSystem(path); FileSystem out = FileSystems.newFileSystem(cachePath)) {
				Files.walk(in.getPath("/"))
					.filter(Files::isRegularFile)
					.forEach(p -> {
						try {
							if (!p.toString().endsWith(".class") || true) {
								Files.copy(p, out.getPath("").resolve(p));
							} else {
								ClassNode node = new ClassNode();
								try (InputStream is = Files.newInputStream(p)) {
									new ClassReader(is).accept(node, 0);
								}
								boolean isSubclassOfMinecraftClass = false;
								if (node.superName != null) {
									String prev = node.superName;
									node.superName = mappingResolver.mapClassName("official", node.superName.replace('/', '.')).replace('.', '/');
									isSubclassOfMinecraftClass = !prev.equals(node.superName);
								}
								for (FieldNode field : node.fields) {
									if (field.signature.length() > 2 && field.signature.startsWith("L") && field.signature.endsWith(";")) {
										field.signature = "L" + mappingResolver.mapClassName(
											"official",
											field.signature.substring(1, field.signature.length() - 1).replace('/', '.')
										).replace('.', '/') + ";";
									}
								}
							}
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					});
			}
		}
    }

    private static void addMod(Path path) {
        try {
            Path storage = QuiltLoader.getConfigDir().resolve("rgml/classes");
            Files.createDirectories(storage);
            Path cachePath = storage.resolve(path);
            if (!Files.exists(cachePath)) {
                remapMod(path, cachePath);
            }

            if (path.endsWith(".jar") || path.endsWith(".zip")) {
                FileSystem fs = FileSystems.newFileSystem(cachePath);
                for (Path p : fs.getRootDirectories()) {
                    Files.walkFileTree(p, new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            String extension = file.getFileName().toString().split("\\.")[1];
                            if (extension.equals("class") || extension.equals("jar") || extension.equals("zip")) {
                                addMod(file);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
            }

            String name = path.getFileName().toString().split("\\.")[0];

            if (props.containsKey(name) && (props.getProperty(name).equalsIgnoreCase("no") || props.getProperty(name).equalsIgnoreCase("off"))) {
                return;
            }

            byte[] bytes = Files.readAllBytes(cachePath);
            Class<?> instclass = Unsafe.lookup().defineClass(bytes);
            if (!BaseMod.class.isAssignableFrom(instclass)) {
                return;
            }

            setupProperties((Class<? extends BaseMod>) instclass);
            BaseMod mod = (BaseMod)instclass.getConstructor().newInstance();
            modList.add(mod);
            logger.fine("Mod Loaded: \"" + mod + "\" from " + name);
            System.out.println("Mod Loaded: " + mod);
        } catch (Throwable var6) {
            logger.fine("Failed to load mod from \"" + path + "\"");
            System.out.println("Failed to load mod from \"" + path + "\"");
            logger.throwing("ModLoader", "addMod", var6);
            ThrowException(var6);
        }
    }

    public static void AddName(Object instance, String name) {
        String tag = null;
        if (instance instanceof class_255 item) {
            if (item.method_2094() != null) {
                tag = item.method_2094() + ".name";
            }
        } else if (instance instanceof class_18 block) {
            if (block.method_2136() != null) {
                tag = block.method_2136() + ".name";
            }
        } else if (instance instanceof class_229 stack) {
            if (stack.method_2114() != null) {
                tag = stack.method_2114() + ".name";
            }
        } else {
            Exception e = new Exception(instance.getClass().getName() + " cannot have name attached to it!");
            logger.throwing("ModLoader", "AddName", e);
            ThrowException(e);
        }

        if (tag != null) {
            AddLocalization(tag, name);
        } else {
            Exception e = new Exception(instance + " is missing name tag!");
            logger.throwing("ModLoader", "AddName", e);
            ThrowException(e);
        }
    }

    public static int addOverride(String fileToOverride, String fileToAdd) {
        try {
            int i = getUniqueSpriteIndex(fileToOverride);
            addOverride(fileToOverride, fileToAdd, i);
            return i;
        } catch (Throwable var3) {
            logger.throwing("ModLoader", "addOverride", var3);
            ThrowException(var3);
            throw new RuntimeException(var3);
        }
    }

    public static void addOverride(String path, String overlayPath, int index) {
        int dst = -1;
        int left = 0;
        byte var6;
        if (path.equals("/terrain.png")) {
            var6 = 0;
            left = terrainSpritesLeft;
        } else {
            if (!path.equals("/gui/items.png")) {
                return;
            }

            var6 = 1;
            left = itemSpritesLeft;
        }

        System.out.println("Overriding " + path + " with " + overlayPath + " @ " + index + ". " + left + " left.");
        logger.finer("addOverride(" + path + "," + overlayPath + "," + index + "). " + left + " left.");
        Map<String, Integer> overlays = overrides.get(Integer.valueOf(var6));
        if (overlays == null) {
            overlays = new HashMap<>();
            overrides.put(Integer.valueOf(var6), overlays);
        }

        overlays.put(overlayPath, index);
    }

    public static void AddRecipe(class_229 output, Object... params) {
        class_286.method_797().method_798(output, params);
    }

    public static void AddShapelessRecipe(class_229 output, Object... params) {
        class_286.method_797().method_2183(output, params);
    }

    public static void AddSmelting(int input, class_229 output) {
        class_683.method_2161().method_2163(input, output);
    }

    public static void AddSpawn(Class<? extends class_209> entityClass, int weightedProb, class_614 spawnList) {
        AddSpawn(entityClass, weightedProb, spawnList, null);
    }

    public static void AddSpawn(Class<? extends class_209> entityClass, int weightedProb, class_614 spawnList, class_611... biomes) {
        if (entityClass == null) {
            throw new IllegalArgumentException("entityClass cannot be null");
        } else if (spawnList == null) {
            throw new IllegalArgumentException("spawnList cannot be null");
        } else {
            if (biomes == null) {
                biomes = standardBiomes;
            }

            for (class_611 biome : biomes) {
                List<class_750> list = biome.method_1973(spawnList);
                if (list != null) {
                    boolean exists = false;

                    for (class_750 entry : list) {
                        if (entry.field_3448 == entityClass) {
                            entry.field_3449 = weightedProb;
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        list.add(new class_750(entityClass, weightedProb));
                    }
                }
            }
        }
    }

    public static void AddSpawn(String entityName, int weightedProb, class_614 spawnList) {
        AddSpawn(entityName, weightedProb, spawnList, null);
    }

    public static void AddSpawn(String entityName, int weightedProb, class_614 spawnList, class_611... biomes) {
        Class<? extends class_1> entityClass = classMap.get(entityName);
        if (entityClass != null && class_209.class.isAssignableFrom(entityClass)) {
            AddSpawn((Class<? extends class_209>) entityClass, weightedProb, spawnList, biomes);
        }
    }

    public static boolean DispenseEntity(class_14 world, double x, double y, double z, int xVel, int zVel, class_229 item) {
        boolean result = false;
        Iterator<BaseMod> iter = modList.iterator();

        while(iter.hasNext() && !result) {
            result = iter.next().DispenseEntity(world, x, y, z, xVel, zVel, item);
        }

        return result;
    }

    public static List<BaseMod> getLoadedMods() {
        return Collections.unmodifiableList(modList);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Minecraft getMinecraftInstance() {
        if (instance == null) {
            try {
                ThreadGroup group = Thread.currentThread().getThreadGroup();
                int count = group.activeCount();
                Thread[] threads = new Thread[count];
                group.enumerate(threads);

                for (Thread thread : threads) {
                    if (thread.getName().equals("Minecraft main thread")) {
                        instance = getPrivateValue(Thread.class, thread, "target");
                        break;
                    }
                }
            } catch (SecurityException | NoSuchFieldException var4) {
                logger.throwing("ModLoader", "getMinecraftInstance", var4);
                throw new RuntimeException(var4);
            }
        }

        return instance;
    }

    public static <T, E> T getPrivateValue(Class<? super E> instanceclass, E instance, int fieldindex) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field f = instanceclass.getDeclaredFields()[fieldindex];
            f.setAccessible(true);
            return (T)f.get(instance);
        } catch (IllegalAccessException var4) {
            logger.throwing("ModLoader", "getPrivateValue", var4);
            ThrowException("An impossible error has occured!", var4);
            return null;
        }
    }

    public static <T, E> T getPrivateValue(Class<? super E> instanceclass, E instance, String field) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        try {
            Field f = instanceclass.getDeclaredField(field);
            f.setAccessible(true);
            return (T)f.get(instance);
        } catch (IllegalAccessException var4) {
            logger.throwing("ModLoader", "getPrivateValue", var4);
            ThrowException("An impossible error has occured!", var4);
            return null;
        }
    }

    private static Method getByPossibleNames(Class<?> parent, String[] names, Class<?>... params) {
        for (String name : names) {
            try {
                return parent.getDeclaredMethod(name, params);
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    public static int getUniqueBlockModelID(BaseMod mod, boolean full3DItem) {
        int id = nextBlockModelID++;
        blockModels.put(id, mod);
        blockSpecialInv.put(id, full3DItem);
        return id;
    }

    public static int getUniqueEntityId() {
        return highestEntityId++;
    }

    private static int getUniqueItemSpriteIndex() {
        while(itemSpriteIndex < usedItemSprites.length) {
            if (!usedItemSprites[itemSpriteIndex]) {
                usedItemSprites[itemSpriteIndex] = true;
                --itemSpritesLeft;
                return itemSpriteIndex++;
            }

            ++itemSpriteIndex;
        }

        Exception e = new Exception("No more empty item sprite indices left!");
        logger.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
        ThrowException(e);
        return 0;
    }

    public static int getUniqueSpriteIndex(String path) {
        if (path.equals("/gui/items.png")) {
            return getUniqueItemSpriteIndex();
        } else if (path.equals("/terrain.png")) {
            return getUniqueTerrainSpriteIndex();
        } else {
            Exception e = new Exception("No registry for this texture: " + path);
            logger.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
            ThrowException(e);
            return 0;
        }
    }

    private static int getUniqueTerrainSpriteIndex() {
        while(terrainSpriteIndex < usedTerrainSprites.length) {
            if (!usedTerrainSprites[terrainSpriteIndex]) {
                usedTerrainSprites[terrainSpriteIndex] = true;
                --terrainSpritesLeft;
                return terrainSpriteIndex++;
            }

            ++terrainSpriteIndex;
        }

        Exception e = new Exception("No more empty terrain sprite indices left!");
        logger.throwing("ModLoader", "getUniqueItemSpriteIndex", e);
        ThrowException(e);
        return 0;
    }

    private static void init() {
        hasInit = true;
        String usedItemSpritesString = "1111111111111111111111111111111111111101111111011111111111111001111111111111111111111111111011111111100110000011111110000000001111111001100000110000000100000011000000010000001100000000000000110000000000000000000000000000000000000000000000001100000000000000";
        String usedTerrainSpritesString = "1111111111111111111111111111110111111111111111111111110111111111111111111111000111111011111111111111001111111110111111111111100011111111000010001111011110000000111111000000000011111100000000001111000000000111111000000000001101000000000001111111111111000011";

        for(int i = 0; i < 256; ++i) {
            usedItemSprites[i] = usedItemSpritesString.charAt(i) == '1';
            if (!usedItemSprites[i]) {
                ++itemSpritesLeft;
            }

            usedTerrainSprites[i] = usedTerrainSpritesString.charAt(i) == '1';
            if (!usedTerrainSprites[i]) {
                ++terrainSpritesLeft;
            }
        }

        try {
            instance = McInstanceGetter.getInstance();
            instance.field_607 = new EntityRendererProxy(instance);

            Field[] fieldArray = class_611.class.getDeclaredFields();
            List<class_611> biomes = new LinkedList<>();

            for (Field field : fieldArray) {
                Class<?> fieldType = field.getType();
                if ((field.getModifiers() & 8) != 0 && fieldType.isAssignableFrom(class_611.class)) {
                    class_611 biome = (class_611) field.get(null);
                    if (!(biome instanceof class_618) && !(biome instanceof class_876)) {
                        biomes.add(biome);
                    }
                }
            }

            standardBiomes = biomes.toArray(new class_611[0]);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException var10) {
            logger.throwing("ModLoader", "init", var10);
            ThrowException(var10);
            throw new RuntimeException(var10);
        }

        try {
            loadConfig();
            if (props.containsKey("loggingLevel")) {
                cfgLoggingLevel = Level.parse(props.getProperty("loggingLevel"));
            }

            if (props.containsKey("grassFix")) {
                C215Duck.INSTANCE.setGrassFix(Boolean.parseBoolean(props.getProperty("grassFix")));
            }

            logger.setLevel(cfgLoggingLevel);
            if ((logfile.exists() || logfile.createNewFile()) && logfile.canWrite() && logHandler == null) {
                logHandler = new FileHandler(logfile.getPath());
                logHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(logHandler);
            }

            logger.fine("ModLoader Beta 1.7.3 Initializing...");
            System.out.println("ModLoader Beta 1.7.3 Initializing...");
            File source = new File(ModLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            modDir.mkdirs();
            readFromModFolder(modDir);
            readFromClassPath(source);
            System.out.println("Done.");
            props.setProperty("loggingLevel", cfgLoggingLevel.getName());
            props.setProperty("grassFix", Boolean.toString(C215Duck.INSTANCE.getGrassFix()));

            for(BaseMod mod : modList) {
                mod.ModsLoaded();
                if (!props.containsKey(mod.getClass().getName())) {
                    props.setProperty(mod.getClass().getName(), "on");
                }
            }

            instance.field_767.field_764 = RegisterAllKeys(instance.field_767.field_764);
            instance.field_767.method_351();
            initStats();
            saveConfig();
        } catch (Throwable var9) {
            logger.throwing("ModLoader", "init", var9);
            ThrowException("ModLoader has failed to initialize.", var9);
            if (logHandler != null) {
                logHandler.close();
            }

            throw new RuntimeException(var9);
        }
    }

    private static void initStats() {
        for(int id = 0; id < class_18.field_208.length; ++id) {
            if (!class_757.field_3480.containsKey(16777216 + id) && class_18.field_208[id] != null && class_18.field_208[id].method_2731()) {
                String str = class_669.method_2120().method_2122("stat.mineBlock", class_18.field_208[id].method_2492());
                class_757.field_3507[id] = new class_764(16777216 + id, str, id).method_2735();
                class_757.field_3486.add(class_757.field_3507[id]);
            }
        }

        for(int id = 0; id < class_255.field_1277.length; ++id) {
            if (!class_757.field_3480.containsKey(16908288 + id) && class_255.field_1277[id] != null) {
                String str = class_669.method_2120().method_2122("stat.useItem", class_255.field_1277[id].method_2451());
                class_757.field_3478[id] = new class_764(16908288 + id, str, id).method_2735();
                if (id >= class_18.field_208.length) {
                    class_757.field_3485.add(class_757.field_3478[id]);
                }
            }

            if (!class_757.field_3480.containsKey(16973824 + id) && class_255.field_1277[id] != null && class_255.field_1277[id].method_2450()) {
                String str = class_669.method_2120().method_2122("stat.breakItem", class_255.field_1277[id].method_2451());
                class_757.field_3479[id] = new class_764(16973824 + id, str, id).method_2735();
            }
        }

        HashSet<Integer> idHashSet = new HashSet<>();

        for(Object result : class_286.method_797().method_2460()) {
            idHashSet.add(((class_679)result).method_2428().field_1166);
        }

        for(Object result : class_683.method_2161().method_2431().values()) {
            idHashSet.add(((class_229)result).field_1166);
        }

        for(int id : idHashSet) {
            if (!class_757.field_3480.containsKey(16842752 + id) && class_255.field_1277[id] != null) {
                String str = class_669.method_2120().method_2122("stat.craftItem", class_255.field_1277[id].method_2451());
                class_757.field_3508[id] = new class_764(16842752 + id, str, id).method_2735();
            }
        }
    }

    public static boolean isGUIOpen(Class<? extends class_49> gui) {
        Minecraft game = getMinecraftInstance();
        if (gui == null) {
            return game.field_290 == null;
        } else {
            return gui.isInstance(game.field_290);
        }
    }

    public static boolean isModLoaded(String modname) {
        Class<?> chk;

        try {
            chk = Class.forName(modname);
        } catch (ClassNotFoundException var4) {
            return false;
        }

        for (BaseMod mod : modList) {
            if (chk.isInstance(mod)) {
                return true;
            }
        }

        return false;
    }

    public static void loadConfig() throws IOException {
        cfgdir.mkdir();
        if (cfgfile.exists() || cfgfile.createNewFile()) {
            if (cfgfile.canRead()) {
                InputStream in = new FileInputStream(cfgfile);
                props.load(in);
                in.close();
            }
        }
    }

    public static BufferedImage loadImage(class_5 texCache, String path) throws Exception {
        class_644 pack = getPrivateValue(class_5.class, texCache, 11);
        InputStream input = pack.field_2967.method_2033(path);
        if (input == null) {
            throw new Exception("Image not found: " + path);
        } else {
            BufferedImage image = ImageIO.read(input);
            if (image == null) {
                throw new Exception("Image corrupted: " + path);
            } else {
                return image;
            }
        }
    }

    public static void OnItemPickup(class_3 player, class_229 item) {
        for(BaseMod mod : modList) {
            mod.OnItemPickup(player, item);
        }
    }

    public static void OnTick(Minecraft game) {
        if (!hasInit) {
            init();
            logger.fine("Initialized");
        }

        if (texPack == null || game.field_767.field_2975 != texPack) {
            texturesAdded = false;
            texPack = game.field_767.field_2975;
        }

        if (!texturesAdded && game.field_323 != null) {
            RegisterAllTextureOverrides(game.field_323);
            texturesAdded = true;
        }

        long newclock = 0L;
        if (game.field_301 != null) {
            newclock = game.field_301.method_2291();
            Iterator<Entry<BaseMod, Boolean>> iter = inGameHooks.entrySet().iterator();

            while(iter.hasNext()) {
                Entry<BaseMod, Boolean> modSet = iter.next();
                if ((clock != newclock || !modSet.getValue()) && !modSet.getKey().OnTickInGame(game)) {
                    iter.remove();
                }
            }
        }

        if (game.field_290 != null) {
            Iterator<Entry<BaseMod, Boolean>> iter = inGUIHooks.entrySet().iterator();

            while(iter.hasNext()) {
                Entry<BaseMod, Boolean> modSet = iter.next();
                if ((clock != newclock || !(modSet.getValue() & game.field_301 != null)) && !modSet.getKey().OnTickInGUI(game, game.field_290)) {
                    iter.remove();
                }
            }
        }

        if (clock != newclock) {
            for(Entry<BaseMod, Map<class_123, boolean[]>> modSet : keyList.entrySet()) {
                for(Entry<class_123, boolean[]> keySet : modSet.getValue().entrySet()) {
                    boolean state = Keyboard.isKeyDown(keySet.getKey().field_748);
                    boolean[] keyInfo = keySet.getValue();
                    boolean oldState = keyInfo[1];
                    keyInfo[1] = state;
                    if (state && (!oldState || keyInfo[0])) {
                        modSet.getKey().KeyboardEvent(keySet.getKey());
                    }
                }
            }
        }

        clock = newclock;
    }

    public static void OpenGUI(class_3 player, class_49 gui) {
        if (!hasInit) {
            init();
            logger.fine("Initialized");
        }

        Minecraft game = getMinecraftInstance();
        if (game.field_316 == player) {
            if (gui != null) {
                game.method_162(gui);
            }
        }
    }

    public static void PopulateChunk(class_379 generator, int chunkX, int chunkZ, class_14 world) {
        if (!hasInit) {
            init();
            logger.fine("Initialized");
        }

        Random rnd = new Random(world.method_2290());
        long xSeed = rnd.nextLong() / 2L * 2L + 1L;
        long zSeed = rnd.nextLong() / 2L * 2L + 1L;
        rnd.setSeed((long)chunkX * xSeed + (long)chunkZ * zSeed ^ world.method_2290());

        for(BaseMod mod : modList) {
            if (generator.method_2145().equals("RandomLevelSource")) {
                mod.GenerateSurface(world, rnd, chunkX << 4, chunkZ << 4);
            } else if (generator.method_2145().equals("HellRandomLevelSource")) {
                mod.GenerateNether(world, rnd, chunkX << 4, chunkZ << 4);
            }
        }
    }

    private static void readFromClassPath(File source) throws IOException {
        //TODO: Re-implement this
    }

    private static void readFromModFolder(File folder) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        //TODO: Re-implement this
    }

    public static class_123[] RegisterAllKeys(class_123[] w) {
        List<class_123> combinedList = new LinkedList<>();
        combinedList.addAll(Arrays.asList(w));

        for(Map<class_123, boolean[]> keyMap : keyList.values()) {
            combinedList.addAll(keyMap.keySet());
        }

        return combinedList.toArray(new class_123[0]);
    }

    public static void RegisterAllTextureOverrides(class_5 texCache) {
        animList.clear();
        Minecraft game = getMinecraftInstance();

        for(BaseMod mod : modList) {
            mod.RegisterAnimation(game);
        }

        for(class_89 anim : animList) {
            texCache.method_255(anim);
        }

        for(Entry<Integer, Map<String, Integer>> overlay : overrides.entrySet()) {
            for(Entry<String, Integer> overlayEntry : overlay.getValue().entrySet()) {
                String overlayPath = overlayEntry.getKey();
                int index = overlayEntry.getValue();
                int dst = overlay.getKey();

                try {
                    BufferedImage im = loadImage(texCache, overlayPath);
                    class_89 anim = new ModTextureStatic(index, dst, im);
                    texCache.method_255(anim);
                } catch (Exception var11) {
                    logger.throwing("ModLoader", "RegisterAllTextureOverrides", var11);
                    ThrowException(var11);
                    throw new RuntimeException(var11);
                }
            }
        }
    }

    public static void RegisterBlock(class_18 block) {
        RegisterBlock(block, null);
    }

    public static void RegisterBlock(class_18 block, Class<? extends class_253> itemclass) {
        try {
            if (block == null) {
                throw new IllegalArgumentException("block parameter cannot be null.");
            }

            List<class_18> list = class_67.field_526;
            list.add(block);
            int id = block.field_24;
            class_253 item;
            if (itemclass != null) {
                item = itemclass.getConstructor(Integer.TYPE).newInstance(id - 256);
            } else {
                item = new class_253(id - 256);
            }

            if (class_18.field_208[id] != null && class_255.field_1277[id] == null) {
                class_255.field_1277[id] = item;
            }
        } catch (IllegalArgumentException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 SecurityException | IllegalAccessException var5) {
            logger.throwing("ModLoader", "RegisterBlock", var5);
            ThrowException(var5);
        }
    }

    public static void RegisterEntityID(Class<? extends class_1> entityClass, String entityName, int id) {
        class_400.method_1233(entityClass, entityName, id);
    }

    public static void RegisterKey(BaseMod mod, class_123 keyHandler, boolean allowRepeat) {
        Map<class_123, boolean[]> keyMap = keyList.get(mod);
        if (keyMap == null) {
            keyMap = new HashMap<>();
        }

        keyMap.put(keyHandler, new boolean[]{allowRepeat, false});
        keyList.put(mod, keyMap);
    }

    public static void RegisterTileEntity(Class<? extends class_262> tileEntityClass, String id) {
        RegisterTileEntity(tileEntityClass, id, null);
    }

    public static void RegisterTileEntity(Class<? extends class_262> tileEntityClass, String id, class_413 renderer) {
        try {
            class_262.method_1175(tileEntityClass, id);
            if (renderer != null) {
                class_414 ref = class_414.field_2003;
                Map<Class<? extends class_262>, class_413> renderers = ref.field_2008;
                renderers.put(tileEntityClass, renderer);
                renderer.method_1261(ref);
            }
        } catch (IllegalArgumentException var5) {
            logger.throwing("ModLoader", "RegisterTileEntity", var5);
            ThrowException(var5);
        }
    }

    public static void RemoveSpawn(Class<? extends class_209> entityClass, class_614 spawnList) {
        RemoveSpawn(entityClass, spawnList, null);
    }

    public static void RemoveSpawn(Class<? extends class_209> entityClass, class_614 spawnList, class_611... biomes) {
        if (entityClass == null) {
            throw new IllegalArgumentException("entityClass cannot be null");
        } else if (spawnList == null) {
            throw new IllegalArgumentException("spawnList cannot be null");
        } else {
            if (biomes == null) {
                biomes = standardBiomes;
            }

            for (class_611 biome : biomes) {
                List<class_750> list = biome.method_1973(spawnList);
                if (list != null) {
                    Iterator<class_750> iter = list.iterator();

                    while (iter.hasNext()) {
                        class_750 entry = iter.next();
                        if (entry.field_3448 == entityClass) {
                            iter.remove();
                        }
                    }
                }
            }
        }
    }

    public static void RemoveSpawn(String entityName, class_614 spawnList) {
        RemoveSpawn(entityName, spawnList, null);
    }

    public static void RemoveSpawn(String entityName, class_614 spawnList, class_611... biomes) {
        Class<? extends class_1> entityClass = classMap.get(entityName);
        if (entityClass != null && class_209.class.isAssignableFrom(entityClass)) {
            RemoveSpawn((Class<? extends class_209>) entityClass, spawnList, biomes);
        }
    }

    public static boolean RenderBlockIsItemFull3D(int modelID) {
        if (!blockSpecialInv.containsKey(modelID)) {
            return modelID == 16;
        } else {
            return blockSpecialInv.get(modelID);
        }
    }

    public static void RenderInvBlock(class_215 renderer, class_18 block, int metadata, int modelID) {
        BaseMod mod = blockModels.get(modelID);
        if (mod != null) {
            mod.RenderInvBlock(renderer, block, metadata, modelID);
        }
    }

    public static boolean RenderWorldBlock(class_215 renderer, class_447 world, int x, int y, int z, class_18 block, int modelID) {
        BaseMod mod = blockModels.get(modelID);
        return mod != null && mod.RenderWorldBlock(renderer, world, x, y, z, block, modelID);
    }

    public static void saveConfig() throws IOException {
        cfgdir.mkdir();
        if (cfgfile.exists() || cfgfile.createNewFile()) {
            if (cfgfile.canWrite()) {
                OutputStream out = new FileOutputStream(cfgfile);
                props.store(out, "ModLoader Config");
                out.close();
            }
        }
    }

    public static void SetInGameHook(BaseMod mod, boolean enable, boolean useClock) {
        if (enable) {
            inGameHooks.put(mod, useClock);
        } else {
            inGameHooks.remove(mod);
        }
    }

    public static void SetInGUIHook(BaseMod mod, boolean enable, boolean useClock) {
        if (enable) {
            inGUIHooks.put(mod, useClock);
        } else {
            inGUIHooks.remove(mod);
        }
    }

    public static <T, E> void setPrivateValue(Class<? super T> instanceclass, T instance, int fieldindex, E value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        Field f = instanceclass.getDeclaredFields()[fieldindex];
        MemberAccess.setFieldValue(f, instance, value);
    }

    public static <T, E> void setPrivateValue(Class<? super T> instanceclass, T instance, String field, E value) throws IllegalArgumentException, SecurityException, NoSuchFieldException {
        Field f = instanceclass.getDeclaredField(field);
        MemberAccess.setFieldValue(f, instance, value);
    }

    private static void setupProperties(Class<? extends BaseMod> mod) throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchFieldException {
        Properties modprops = new Properties();
        File modcfgfile = new File(cfgdir, mod.getName() + ".cfg");
        if (modcfgfile.exists() && modcfgfile.canRead()) {
            modprops.load(new FileInputStream(modcfgfile));
        }

        StringBuilder helptext = new StringBuilder();

        Field[] var7;
        for(Field field : var7 = mod.getFields()) {
            if ((field.getModifiers() & 8) != 0 && field.isAnnotationPresent(MLProp.class)) {
                Class<?> type = field.getType();
                MLProp annotation = field.getAnnotation(MLProp.class);
                String key = annotation.name().length() == 0 ? field.getName() : annotation.name();
                Object currentvalue = field.get(null);
                StringBuilder range = new StringBuilder();
                if (annotation.min() != Double.NEGATIVE_INFINITY) {
                    range.append(String.format(",>=%.1f", annotation.min()));
                }

                if (annotation.max() != Double.POSITIVE_INFINITY) {
                    range.append(String.format(",<=%.1f", annotation.max()));
                }

                StringBuilder info = new StringBuilder();
                if (annotation.info().length() > 0) {
                    info.append(" -- ");
                    info.append(annotation.info());
                }

                helptext.append(String.format("%s (%s:%s%s)%s\n", key, type.getName(), currentvalue, range, info));
                if (modprops.containsKey(key)) {
                    String strvalue = modprops.getProperty(key);
                    Object value = null;
                    if (type.isAssignableFrom(String.class)) {
                        value = strvalue;
                    } else if (type.isAssignableFrom(Integer.TYPE)) {
                        value = Integer.parseInt(strvalue);
                    } else if (type.isAssignableFrom(Short.TYPE)) {
                        value = Short.parseShort(strvalue);
                    } else if (type.isAssignableFrom(Byte.TYPE)) {
                        value = Byte.parseByte(strvalue);
                    } else if (type.isAssignableFrom(Boolean.TYPE)) {
                        value = Boolean.parseBoolean(strvalue);
                    } else if (type.isAssignableFrom(Float.TYPE)) {
                        value = Float.parseFloat(strvalue);
                    } else if (type.isAssignableFrom(Double.TYPE)) {
                        value = Double.parseDouble(strvalue);
                    }

                    if (value != null) {
                        if (value instanceof Number) {
                            double num = ((Number)value).doubleValue();
                            if (annotation.min() != Double.NEGATIVE_INFINITY && num < annotation.min() || annotation.max() != Double.POSITIVE_INFINITY && num > annotation.max()) {
                                continue;
                            }
                        }

                        logger.finer(key + " set to " + value);
                        if (!value.equals(currentvalue)) {
                            field.set(null, value);
                        }
                    }
                } else {
                    logger.finer(key + " not in config, using default: " + currentvalue);
                    modprops.setProperty(key, currentvalue.toString());
                }
            }
        }

        if (!modprops.isEmpty() && (modcfgfile.exists() || modcfgfile.createNewFile()) && modcfgfile.canWrite()) {
            modprops.store(new FileOutputStream(modcfgfile), helptext.toString());
        }
    }

    public static void TakenFromCrafting(class_3 player, class_229 item) {
        for(BaseMod mod : modList) {
            mod.TakenFromCrafting(player, item);
        }
    }

    public static void TakenFromFurnace(class_3 player, class_229 item) {
        for(BaseMod mod : modList) {
            mod.TakenFromFurnace(player, item);
        }
    }

    public static void ThrowException(String message, Throwable e) {
        Minecraft game = getMinecraftInstance();
        if (game != null) {
            game.method_1393(new class_480(message, e));
        } else {
            throw new RuntimeException(e);
        }
    }

    private static void ThrowException(Throwable e) {
        ThrowException("Exception occured in ModLoader", e);
    }

    private ModLoader() {
    }
}
