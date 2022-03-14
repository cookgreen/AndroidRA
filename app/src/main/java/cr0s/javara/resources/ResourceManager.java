package cr0s.javara.resources;

import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.opengl.CursorLoader;
import org.newdawn.slick.opengl.Texture;

import cr0s.javara.entity.building.BibType;
import cr0s.javara.gameplay.Team.Alignment;
import redhorizon.filetypes.aud.AudFile;
import redhorizon.filetypes.mix.MixFile;
import redhorizon.filetypes.mix.MixRecord;
import redhorizon.filetypes.mix.MixRecordByteChannel;
import redhorizon.filetypes.pal.PalFile;
import redhorizon.filetypes.shp.ShpFile;
import redhorizon.filetypes.shp.ShpFileCnc;
import redhorizon.filetypes.tmp.TmpFileRA;
import soundly.XSound;

public class ResourceManager {
	private  AssetManager assetManager;

    private static ResourceManager instance;
    public static String ROOT_FOLDER = System.getProperty("user.dir")
	    + System.getProperty("file.separator");

    public static final String RESOURCE_FOLDER = ROOT_FOLDER + "assets"
	    + System.getProperty("file.separator");
    public static final String PAL_FOLDER = RESOURCE_FOLDER + "pal"
	    + System.getProperty("file.separator");
    public static final String TILESETS_FOLDER = ROOT_FOLDER + "tilesets"
	    + System.getProperty("file.separator");

    public static final String MAPS_FOLDER = ROOT_FOLDER + "maps"
	    + System.getProperty("file.separator");

    public static final String AI_FOLDER = ROOT_FOLDER + "ai" 
	    + System.getProperty("file.separator");
    
    public static final String SIDEBAR_CATEGORIES_SHEET = RESOURCE_FOLDER + "sidebar_buttons.png";

    //public static Cursor pointerCursor;
    private HashMap<String, MixFile> mixes = new HashMap<>();
    private HashMap<String, ShpTexture> commonTextureSources = new HashMap<>();
    private HashMap<String, ShpTexture> shpTextureSources = new HashMap<>();
    private HashMap<String, TmpTexture> templatesTexureSources = new HashMap<>();
    private HashMap<String, PalFile> palettes = new HashMap<>();
    private HashMap<String, XSound> sounds = new HashMap<>();

    private SpriteSheet bib1, bib2, bib3;

    private ResourceManager() {
    }

    public static ResourceManager getInstance() {
		if (instance == null) {
		    instance = new ResourceManager();
		}

		return instance;
    }

    public void Init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		loadMixes();
		loadPals();
	}

    public void loadBibs() {
		bib1 = new SpriteSheet(getTemplateShpTexture("temperat", "bib1.tem").getAsCombinedImage(null), 24, 24);
		bib2 = new SpriteSheet(getTemplateShpTexture("temperat", "bib2.tem").getAsCombinedImage(null), 24, 24);
		bib3 = new SpriteSheet(getTemplateShpTexture("temperat", "bib3.tem").getAsCombinedImage(null), 24, 24);
    }

    public InputStream OpenFile(String path) throws IOException {
		return assetManager.open(path);
	}

	public InputStream OpenResourcesFile(String file) throws IOException {
		return assetManager.open("resources/" + file);
	}

	public InputStream OpenResourcesPalFile(String file) throws IOException {
		return assetManager.open("resources/pal/" + file);
	}

	public InputStream OpenTilesetsFile(String file) throws IOException {
		return assetManager.open("tilesets/" + file);
	}

	public InputStream OpenAIFile(String file) throws IOException {
		return assetManager.open("ai/" + file);
	}

	public InputStream OpenMapsFile(String file) throws IOException {
		return assetManager.open("maps/" + file);
	}

    public SpriteSheet getBibSheet(BibType bt) {
		switch (bt) {
		case SMALL:
		    return bib3;
		case MIDDLE:
		    return bib2;

		case BIG:
		    return bib1;

		default:
		    return null;
		}
    }

    private void loadMixes() {
		try {
			String[] contents = assetManager.list("resources");
			for (String content : contents)
			{
				if(content.endsWith(".mix")) {
					InputStream mixFileStream = assetManager.open("resources" + File.separator + content);
					MixFile mixFile = new MixFile(content, mixFileStream);
					mixes.put(mixFile.getFileName(), mixFile);
				}
			}
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		}
    }

    private void  loadPals(){
		try{
			String path = "resources" + File.separator + "pal";
			String[] contents = assetManager.list(path);
			for (String content : contents)
			{
				if(content.endsWith(".pal")){
					InputStream palStream = OpenResourcesPalFile(content);
					PalFile palFile = new PalFile(content, palStream);
					palettes.put(palFile.getFileName(), palFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
		}
	}

    public ShpTexture getSidebarTexture(String name) {
		MixFile mix = mixes.get("interface.mix");

		// Check texture sources cache
		if (commonTextureSources.containsKey(name)) {
		    return commonTextureSources.get(name);
		}

		if (mix != null) {
		    MixRecord rec = mix.getEntry(name);

		    if (rec != null) {
				MixRecordByteChannel rbc = mix.getEntryData(rec);

				ShpFileCnc shp = new ShpFileCnc(name, rbc);
				ShpTexture shpTexture = new ShpTexture(shp);
				commonTextureSources.put(name, shpTexture);
				return shpTexture;
		    } else {
				return null;
		    }
		}

		return null;
    }    

    public ShpTexture getConquerTexture(String name) {
		MixFile mix = mixes.get("conquer.mix");

		// Check texture sources cache
		if (commonTextureSources.containsKey(name)) {
		    return commonTextureSources.get(name);
		}

		if (mix != null) {
		    MixRecord rec = mix.getEntry(name);

		    if (rec != null) {
				MixRecordByteChannel rbc = mix.getEntryData(rec);

				ShpFileCnc shp = new ShpFileCnc(name, rbc);
				ShpTexture shpTexture = new ShpTexture(shp);
				shpTexture.palleteName = "temperat.pal";
				commonTextureSources.put(name, shpTexture);
				return shpTexture;
		    } else {
				return null;
		    }
		}

		return null;
    }

    public XSound loadSpeechSound(String name) {
		return loadSound("speech.mix", name + ".aud");
    }

    	public XSound loadSound(String mixname, String name) {
		MixFile mix = mixes.get(mixname);

		// Check texture sources cache
		if (this.sounds.containsKey(name)) {
		    return sounds.get(name);
		}

		if (mix != null) {
		    MixRecord rec = mix.getEntry(name);

		    if (rec != null) {
			ReadableByteChannel rbc = mix.getEntryData(rec);
			AudFile aud = new AudFile(name, rbc);

			XSound sound = null;
			try {
			    sound = new XSound(name, new BufferedInputStream(Channels.newInputStream(aud.getSoundData())));
			} catch (SlickException e) {
			    e.printStackTrace();
			}
			//aud.close();

			if (sound != null) {
			    this.sounds.put(name, sound);
			}

			return sound;
		    } else {
			return null;
		    }
		}

		return null;
    }

    public ShpTexture getTemplateShpTexture(String tileSetName, String name) {
		MixFile mix = mixes.get(tileSetName.toLowerCase() + ".mix");

		// Check texture sources cache
		if (shpTextureSources.containsKey(name)) {
		    return shpTextureSources.get(name);
		}

		if (mix != null) {
		    MixRecord rec = mix.getEntry(name);

		    if (rec != null) {
				MixRecordByteChannel rbc = mix.getEntryData(rec);

				ShpFileCnc shp = new ShpFileCnc(name, rbc);
				ShpTexture shpTexture = new ShpTexture(shp);
				shpTexture.palleteName = tileSetName + ".pal";
				shpTextureSources.put(name, shpTexture);

				return shpTexture;
		    } else {
				System.err.println("Record SHP (" + name +") in " + tileSetName + ".mix is not found");
				return null;
		    }
		} else {
		    System.err.println("Mix file " + tileSetName + ".mix is not found");
		}

		return null;
    }    

    public TmpTexture getTemplateTexture(String type, String name) {
		type = type.toLowerCase();
		MixFile mix = mixes.get(type + ".mix");

		// Check texture sources cache
		if (templatesTexureSources.containsKey(name)) {
		    return templatesTexureSources.get(name);
		}

		if (mix != null) {
		    MixRecord rec = mix.getEntry(name);

		    if (rec != null) {
			ReadableByteChannel rbc = mix.getEntryData(rec);

			TmpFileRA tmp = new TmpFileRA(name, rbc);
			TmpTexture tmpTexture = new TmpTexture(tmp, type);

			templatesTexureSources.put(name, tmpTexture);
			return tmpTexture;
		    } else {
			//System.out.println("Record (" + name +") in " + type + ".mix is not found");
			return null;
		    }
		}

		System.out.println(type + ".mix is not found");
		return null;
    }    

    public PalFile getPaletteByName(String name) {
		if (palettes.containsKey(name)) {
		    return palettes.get(name);
		}

		try (RandomAccessFile randomAccessFile = new RandomAccessFile(PAL_FOLDER + name.toString().toLowerCase(), "r")) {
		    FileChannel inChannel = randomAccessFile.getChannel();
		    PalFile palfile = new PalFile(name, inChannel);

		    palettes.put(name, palfile);

		    return palfile;
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		}

		return null;
    }

    List<Path> listDirectoryMixes(Path resourceFolder) throws IOException {
		List<Path> result = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(
			resourceFolder, "*.{mix}")) {
		    for (Path entry : stream) {
			result.add(entry);
		    }
		} catch (DirectoryIteratorException ex) {
		    throw ex.getCause();
		}

		return result;
    	}

    	public XSound loadUnitSound(Alignment alignment, String name) {
		String mixname = "allies.mix";
		if (alignment == Alignment.SOVIET) {
		    mixname = "russian.mix";
		}

		return loadSound(mixname, name);
    }

    public ShpTexture getInfantryTexture(String name) {
		MixFile mix = mixes.get("hires.mix");

		// Check texture sources cache
		if (commonTextureSources.containsKey(name)) {
		    return commonTextureSources.get(name);
		}

		if (mix != null) {
		    MixRecord rec = mix.getEntry(name);

		    if (rec != null) {
				MixRecordByteChannel rbc = mix.getEntryData(rec);

				ShpFileCnc shp = new ShpFileCnc(name, rbc);
				ShpTexture shpTexture = new ShpTexture(shp);
				commonTextureSources.put(name, shpTexture);
				return shpTexture;
		    	} else {
				System.out.println("HIRES: " + name + " not found");
				return null;
		    }
		}

		return null;
    }

    public ShpTexture getShpTexture(String name) {
	// Check texture sources cache
		if (commonTextureSources.containsKey(name)) {
	    	return commonTextureSources.get(name);
		}

		RandomAccessFile randomAccessFile = null;

		try {
			InputStream inputStream = OpenResourcesFile(name);

		    ShpFileCnc shp = new ShpFileCnc(name, inputStream);
		    ShpTexture shpTexture = new ShpTexture(shp);
		    shpTexture.palleteName = "temperat.pal";

		    commonTextureSources.put(name, shpTexture);
		    return shpTexture;
		} catch (IOException e) {
		    e.printStackTrace();
		    return null;
		} finally {
		    if (randomAccessFile != null) {
				try {
				    randomAccessFile.close();
				} catch (IOException e) {
				    e.printStackTrace();
				}
		    }
		}
    }
}
