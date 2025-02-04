package shadersmod.client;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.src.Config;
import net.minecraft.src.StrUtils;

public class ShaderPackParser {
	private static final Pattern PATTERN_INCLUDE = Pattern.compile("^\\s*#include\\s+\"([A-Za-z0-9_/\\.]+)\".*$");
	private static final Set<String> setConstNames = makeSetConstNames();

	public static ShaderOption[] parseShaderPackOptions(IShaderPack shaderPack, String[] programNames, List<Integer> listDimensions) {
		if (shaderPack == null) {
			return new ShaderOption[0];
		} else {
			Map<String, ShaderOption> map = new HashMap();
			collectShaderOptions(shaderPack, "/shaders", programNames, map);
			Iterator<Integer> iterator = listDimensions.iterator();

			while (iterator.hasNext()) {
				int i = iterator.next().intValue();
				String s = "/shaders/world" + i;
				collectShaderOptions(shaderPack, s, programNames, map);
			}

			Collection<ShaderOption> collection = map.values();
			ShaderOption[] ashaderoption = (collection.toArray(new ShaderOption[collection.size()]));
			Comparator<ShaderOption> comparator = new Comparator<ShaderOption>() {
				@Override
				public int compare(ShaderOption o1, ShaderOption o2) {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			};
			Arrays.sort(ashaderoption, comparator);
			return ashaderoption;
		}
	}

	private static void collectShaderOptions(IShaderPack shaderPack, String dir, String[] programNames, Map<String, ShaderOption> mapOptions) {
		for (String programName : programNames) {
			String s = programName;

			if (!s.equals("")) {
				String s1 = dir + "/" + s + ".vsh";
				String s2 = dir + "/" + s + ".fsh";
				collectShaderOptions(shaderPack, s1, mapOptions);
				collectShaderOptions(shaderPack, s2, mapOptions);
			}
		}
	}

	private static void collectShaderOptions(IShaderPack sp, String path, Map<String, ShaderOption> mapOptions) {
		String[] astring = getLines(sp, path);

		for (String s : astring) {
			ShaderOption shaderoption = getShaderOption(s, path);

			if ((shaderoption != null) && (!shaderoption.checkUsed() || isOptionUsed(shaderoption, astring))) {
				String s1 = shaderoption.getName();
				ShaderOption shaderoption1 = mapOptions.get(s1);

				if (shaderoption1 != null) {
					if (!Config.equals(shaderoption1.getValueDefault(), shaderoption.getValueDefault())) {
						Config.warn("Ambiguous shader option: " + shaderoption.getName());
						Config.warn(" - in " + Config.arrayToString(shaderoption1.getPaths()) + ": " + shaderoption1.getValueDefault());
						Config.warn(" - in " + Config.arrayToString(shaderoption.getPaths()) + ": " + shaderoption.getValueDefault());
						shaderoption1.setEnabled(false);
					}

					if ((shaderoption1.getDescription() == null) || (shaderoption1.getDescription().length() <= 0)) {
						shaderoption1.setDescription(shaderoption.getDescription());
					}

					shaderoption1.addPaths(shaderoption.getPaths());
				} else {
					mapOptions.put(s1, shaderoption);
				}
			}
		}
	}

	private static boolean isOptionUsed(ShaderOption so, String[] lines) {
		for (String s : lines) {
			if (so.isUsedInLine(s)) { return true; }
		}

		return false;
	}

	private static String[] getLines(IShaderPack sp, String path) {
		try {
			String s = loadFile(path, sp, 0);

			if (s == null) {
				return new String[0];
			} else {
				ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s.getBytes());
				String[] astring = Config.readLines(bytearrayinputstream);
				return astring;
			}
		} catch (IOException ioexception) {
			Config.dbg(ioexception.getClass().getName() + ": " + ioexception.getMessage());
			return new String[0];
		}
	}

	private static ShaderOption getShaderOption(String line, String path) {
		ShaderOption shaderoption = null;

		if (shaderoption == null) {
			shaderoption = ShaderOptionSwitch.parseOption(line, path);
		}

		if (shaderoption == null) {
			shaderoption = ShaderOptionVariable.parseOption(line, path);
		}

		if (shaderoption != null) {
			return shaderoption;
		} else {
			if (shaderoption == null) {
				shaderoption = ShaderOptionSwitchConst.parseOption(line, path);
			}

			if (shaderoption == null) {
				shaderoption = ShaderOptionVariableConst.parseOption(line, path);
			}

			return (shaderoption != null) && setConstNames.contains(shaderoption.getName()) ? shaderoption : null;
		}
	}

	private static Set<String> makeSetConstNames() {
		Set<String> set = new HashSet();
		set.add("shadowMapResolution");
		set.add("shadowDistance");
		set.add("shadowIntervalSize");
		set.add("generateShadowMipmap");
		set.add("generateShadowColorMipmap");
		set.add("shadowHardwareFiltering");
		set.add("shadowHardwareFiltering0");
		set.add("shadowHardwareFiltering1");
		set.add("shadowtex0Mipmap");
		set.add("shadowtexMipmap");
		set.add("shadowtex1Mipmap");
		set.add("shadowcolor0Mipmap");
		set.add("shadowColor0Mipmap");
		set.add("shadowcolor1Mipmap");
		set.add("shadowColor1Mipmap");
		set.add("shadowtex0Nearest");
		set.add("shadowtexNearest");
		set.add("shadow0MinMagNearest");
		set.add("shadowtex1Nearest");
		set.add("shadow1MinMagNearest");
		set.add("shadowcolor0Nearest");
		set.add("shadowColor0Nearest");
		set.add("shadowColor0MinMagNearest");
		set.add("shadowcolor1Nearest");
		set.add("shadowColor1Nearest");
		set.add("shadowColor1MinMagNearest");
		set.add("wetnessHalflife");
		set.add("drynessHalflife");
		set.add("eyeBrightnessHalflife");
		set.add("centerDepthHalflife");
		set.add("sunPathRotation");
		set.add("ambientOcclusionLevel");
		set.add("superSamplingLevel");
		set.add("noiseTextureResolution");
		return set;
	}

	public static ShaderProfile[] parseProfiles(Properties props, ShaderOption[] shaderOptions) {
		String s = "profile.";
		List<ShaderProfile> list = new ArrayList();

		for (Object o : props.keySet()) {
			String s1 = o.toString();
			if (s1.startsWith(s)) {
				String s2 = s1.substring(s.length());
				props.getProperty(s1);
				Set<String> set = new HashSet();
				ShaderProfile shaderprofile = parseProfile(s2, props, set, shaderOptions);

				if (shaderprofile != null) {
					list.add(shaderprofile);
				}
			}
		}

		if (list.size() <= 0) {
			return null;
		} else {
			ShaderProfile[] ashaderprofile = (list.toArray(new ShaderProfile[list.size()]));
			return ashaderprofile;
		}
	}

	private static ShaderProfile parseProfile(String name, Properties props, Set<String> parsedProfiles, ShaderOption[] shaderOptions) {
		String s = "profile.";
		String s1 = s + name;

		if (parsedProfiles.contains(s1)) {
			Config.warn("[Shaders] Profile already parsed: " + name);
			return null;
		} else {
			parsedProfiles.add(name);
			ShaderProfile shaderprofile = new ShaderProfile(name);
			String s2 = props.getProperty(s1);
			String[] astring = Config.tokenize(s2, " ");

			for (String s3 : astring) {
				if (s3.startsWith(s)) {
					String s6 = s3.substring(s.length());
					ShaderProfile shaderprofile1 = parseProfile(s6, props, parsedProfiles, shaderOptions);

					if (shaderprofile != null) {
						shaderprofile.addOptionValues(shaderprofile1);
						shaderprofile.addDisabledPrograms(shaderprofile1.getDisabledPrograms());
					}
				} else {
					String[] astring1 = Config.tokenize(s3, ":=");

					if (astring1.length == 1) {
						String s7 = astring1[0];
						boolean flag = true;

						if (s7.startsWith("!")) {
							flag = false;
							s7 = s7.substring(1);
						}

						String s8 = "program.";

						if (!flag && s7.startsWith("program.")) {
							String s9 = s7.substring(s8.length());

							if (!Shaders.isProgramPath(s9)) {
								Config.warn("Invalid program: " + s9 + " in profile: " + shaderprofile.getName());
							} else {
								shaderprofile.addDisabledProgram(s9);
							}
						} else {
							ShaderOption shaderoption1 = ShaderUtils.getShaderOption(s7, shaderOptions);

							if (!(shaderoption1 instanceof ShaderOptionSwitch)) {
								Config.warn("[Shaders] Invalid option: " + s7);
							} else {
								shaderprofile.addOptionValue(s7, String.valueOf(flag));
								shaderoption1.setVisible(true);
							}
						}
					} else if (astring1.length != 2) {
						Config.warn("[Shaders] Invalid option value: " + s3);
					} else {
						String s4 = astring1[0];
						String s5 = astring1[1];
						ShaderOption shaderoption = ShaderUtils.getShaderOption(s4, shaderOptions);

						if (shaderoption == null) {
							Config.warn("[Shaders] Invalid option: " + s3);
						} else if (!shaderoption.isValidValue(s5)) {
							Config.warn("[Shaders] Invalid value: " + s3);
						} else {
							shaderoption.setVisible(true);
							shaderprofile.addOptionValue(s4, s5);
						}
					}
				}
			}

			return shaderprofile;
		}
	}

	public static Map<String, ShaderOption[]> parseGuiScreens(Properties props, ShaderProfile[] shaderProfiles, ShaderOption[] shaderOptions) {
		Map<String, ShaderOption[]> map = new HashMap();
		parseGuiScreen("screen", props, map, shaderProfiles, shaderOptions);
		return map.isEmpty() ? null : map;
	}

	private static boolean parseGuiScreen(String key, Properties props, Map<String, ShaderOption[]> map, ShaderProfile[] shaderProfiles, ShaderOption[] shaderOptions) {
		String s = props.getProperty(key);

		if (s == null) {
			return false;
		} else {
			List<ShaderOption> list = new ArrayList();
			Set<String> set = new HashSet();
			String[] astring = Config.tokenize(s, " ");

			for (String s1 : astring) {
				if (s1.equals("<empty>")) {
					list.add((ShaderOption) null);
				} else if (set.contains(s1)) {
					Config.warn("[Shaders] Duplicate option: " + s1 + ", key: " + key);
				} else {
					set.add(s1);

					if (s1.equals("<profile>")) {
						if (shaderProfiles == null) {
							Config.warn("[Shaders] Option profile can not be used, no profiles defined: " + s1 + ", key: " + key);
						} else {
							ShaderOptionProfile shaderoptionprofile = new ShaderOptionProfile(shaderProfiles, shaderOptions);
							list.add(shaderoptionprofile);
						}
					} else if (s1.equals("*")) {
						ShaderOption shaderoption1 = new ShaderOptionRest("<rest>");
						list.add(shaderoption1);
					} else if (s1.startsWith("[") && s1.endsWith("]")) {
						String s2 = StrUtils.removePrefixSuffix(s1, "[", "]");

						if (!s2.matches("^[a-zA-Z0-9_]+$")) {
							Config.warn("[Shaders] Invalid screen: " + s1 + ", key: " + key);
						} else if (!parseGuiScreen("screen." + s2, props, map, shaderProfiles, shaderOptions)) {
							Config.warn("[Shaders] Invalid screen: " + s1 + ", key: " + key);
						} else {
							ShaderOptionScreen shaderoptionscreen = new ShaderOptionScreen(s2);
							list.add(shaderoptionscreen);
						}
					} else {
						ShaderOption shaderoption = ShaderUtils.getShaderOption(s1, shaderOptions);

						if (shaderoption == null) {
							Config.warn("[Shaders] Invalid option: " + s1 + ", key: " + key);
							list.add((ShaderOption) null);
						} else {
							shaderoption.setVisible(true);
							list.add(shaderoption);
						}
					}
				}
			}

			ShaderOption[] ashaderoption = (list.toArray(new ShaderOption[list.size()]));
			map.put(key, ashaderoption);
			return true;
		}
	}

	public static BufferedReader resolveIncludes(BufferedReader reader, String filePath, IShaderPack shaderPack, int includeLevel) throws IOException {
		String s = "/";
		int i = filePath.lastIndexOf("/");

		if (i >= 0) {
			s = filePath.substring(0, i);
		}

		CharArrayWriter chararraywriter = new CharArrayWriter();

		while (true) {
			String s1 = reader.readLine();

			if (s1 == null) {
				CharArrayReader chararrayreader = new CharArrayReader(chararraywriter.toCharArray());
				return new BufferedReader(chararrayreader);
			}

			Matcher matcher = PATTERN_INCLUDE.matcher(s1);

			if (matcher.matches()) {
				String s2 = matcher.group(1);
				boolean flag = s2.startsWith("/");
				String s3 = flag ? "/shaders" + s2 : s + "/" + s2;
				s1 = loadFile(s3, shaderPack, includeLevel);

				if (s1 == null) { throw new IOException("Included file not found: " + filePath); }
			}

			chararraywriter.write(s1);
			chararraywriter.write("\n");
		}
	}

	private static String loadFile(String filePath, IShaderPack shaderPack, int includeLevel) throws IOException {
		if (includeLevel >= 10) {
			throw new IOException("#include depth exceeded: " + includeLevel + ", file: " + filePath);
		} else {
			++includeLevel;
			InputStream inputstream = shaderPack.getResourceAsStream(filePath);

			if (inputstream == null) {
				return null;
			} else {
				InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "ASCII");
				BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
				bufferedreader = resolveIncludes(bufferedreader, filePath, shaderPack, includeLevel);
				CharArrayWriter chararraywriter = new CharArrayWriter();

				while (true) {
					String s = bufferedreader.readLine();

					if (s == null) { return chararraywriter.toString(); }

					chararraywriter.write(s);
					chararraywriter.write("\n");
				}
			}
		}
	}
}
