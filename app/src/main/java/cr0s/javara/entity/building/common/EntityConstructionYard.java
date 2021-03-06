package cr0s.javara.entity.building.common;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import cr0s.javara.combat.ArmorType;
import cr0s.javara.entity.ISelectable;
import cr0s.javara.entity.IShroudRevealer;
import cr0s.javara.entity.building.BibType;
import cr0s.javara.entity.building.EntityBuilding;
import cr0s.javara.gameplay.Player;
import cr0s.javara.gameplay.Production;
import cr0s.javara.gameplay.Team;
import cr0s.javara.gameplay.Team.Alignment;
import cr0s.javara.main.Main;
import cr0s.javara.resources.ResourceManager;
import cr0s.javara.resources.ShpTexture;

public class EntityConstructionYard extends EntityBuilding implements ISelectable, IShroudRevealer {

    private SpriteSheet sheet;

    private Image normal, corrupted;
    private final String TEXTURE_NAME = "fact.shp";
    private final String MAKE_TEXTURE_NAME = "factmake.shp";

    public static final int WIDTH_TILES = 3;
    public static final int HEIGHT_TILES = 4;
    private static final int SHROUD_REVEALING_RANGE = 10;

    private static final String FOOTPRINT = "xxx xxx xxx ~~~";

    private Alignment yardAlignment = Alignment.SOVIET;

    public EntityConstructionYard(Float tileX, Float tileY, Team team, Player player) {
	super(tileX, tileY, team, player, WIDTH_TILES * 24, HEIGHT_TILES * 24, FOOTPRINT);

	this.yardAlignment = player.getAlignment();

	setBibType(BibType.MIDDLE);
	setProgressValue(-1);

	setMaxHp(1500);
	setHp(getMaxHp());

	this.armorType = ArmorType.WOOD;
	
	this.buildingSpeed = 100;
	this.makeTextureName = MAKE_TEXTURE_NAME;
	initTextures();

	this.unitProductionAlingment = Alignment.NEUTRAL;

	this.setName("fact");
    }

    private void initTextures() {
	ShpTexture tex = ResourceManager.getInstance().getConquerTexture(TEXTURE_NAME);
	corrupted = tex.getAsImage(51, owner.playerColor);
	normal = tex.getAsImage(0, owner.playerColor);	
    }

    @Override
    public void renderEntity(Graphics g) {
	float nx = posX;
	float ny = posY;

	if (this.getHp() > this.getMaxHp() / 2) {
	    normal.draw(nx, ny);
	} else {
	    corrupted.draw(nx, ny);
	}

	// Draw bounding box if debug mode is on
	if (Main.DEBUG_MODE) {
	    g.setLineWidth(2);
	    g.setColor(owner.playerColor);
	    g.draw(boundingBox);
	    g.setLineWidth(1);
	}

	if (this.isSelected) {
	    EntityBuilding currentlyBuilding = (EntityBuilding) this.owner.getBase().getProductionQueue().getCurrentProducingBuilding();
	    if (currentlyBuilding != null) {
		Production p = this.owner.getBase().getProductionQueue().getProductionForBuilding(currentlyBuilding);

		if (p.isBuilding()) {
		    p.drawProductionButton(g, this.boundingBox.getMinX(), this.boundingBox.getMinY(), new Color(255, 255, 255, 255), true);
		}
	    }
	}
	
	// Render repairing wrench
	if (this.repairIconBlink) {
	    repairImage.draw(this.boundingBox.getX() + this.boundingBox.getWidth() / 2 - repairImage.getWidth() / 2, this.boundingBox.getY() + this.boundingBox.getHeight() / 2 - repairImage.getHeight() / 2);
	}	
    }

    @Override
    public boolean shouldRenderedInPass(int passnum) {
	return passnum == 0;
    }

    @Override
    public void updateEntity(int delta) {
	super.updateEntity(delta);

	EntityBuilding currentlyBuilding = (EntityBuilding) this.owner.getBase().getProductionQueue().getCurrentProducingBuilding();
	if (currentlyBuilding != null) {
	    Production p = this.owner.getBase().getProductionQueue().getProductionForBuilding(currentlyBuilding);

	    if (p.isBuilding()) {
		this.setProgressValue((int) (p.getCurrentBuildingProgress() * 100));
		this.setMaxProgress(100);
	    }
	} else {
	    this.setProgressValue(-1);
	}
    }

    @Override
    public void select() {
	this.isSelected = true;
    }

    @Override
    public void cancelSelect() {
	this.isSelected = false;
    }

    @Override
    public boolean isSelected() {
	return this.isSelected;
    }

    @Override
    public float getHeightInTiles() {
	return this.tileHeight;
    }

    @Override
    public float getWidthInTiles() {
	return this.tileWidth;
    }

    public Alignment getAlignment() {
	return this.yardAlignment;
    }

    @Override
    public int getRevealingRange() {
	return this.SHROUD_REVEALING_RANGE;
    }

    @Override
    public Image getTexture() {
	if (sheet == null) {
	    return null;
	}

	return sheet.getSubImage(0, 0);
    }    
}
