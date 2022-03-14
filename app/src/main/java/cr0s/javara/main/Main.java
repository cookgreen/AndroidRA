package cr0s.javara.main;
import android.content.Context;

import com.android.redalert.GameActivity;
import com.badlogic.gdx.backends.android.AndroidInput;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import cr0s.javara.ai.AIPlayer;
import cr0s.javara.gameplay.BuildingOverlay;
import cr0s.javara.gameplay.Player;
import cr0s.javara.gameplay.Team;
import cr0s.javara.gameplay.Team.Alignment;
import cr0s.javara.render.Controller;
import cr0s.javara.render.World;
import cr0s.javara.render.shrouds.ShroudRenderer;
import cr0s.javara.render.viewport.Camera;
import cr0s.javara.resources.ResourceManager;
import cr0s.javara.ui.GameSideBar;
import cr0s.javara.ui.cursor.CursorType;
import cr0s.javara.util.Pos;

public class Main extends StateBasedGame {
    public ResourceManager rm;

    private StateMainMenu menu;
    private StateGameMap gameMap;

    private static Main instance;

    private World w;
    private Camera camera;
    private Controller controller;

    private Team team;
    private Player player;

    private BuildingOverlay bo;

    public static boolean DEBUG_MODE = false;

    private GameSideBar gsb;
    private ShroudRenderer observerShroudRenderer;

    private Context appContext;
    private AndroidInput androidInput;
    private ArrayList<InputGameState> gameStates;

    public Main() {
    	super("JavaRA");
    	gameStates = new ArrayList<>();
    }

	public void Init(Context appContext){
    	this.appContext = appContext;

    	ResourceManager.getInstance().Init(appContext.getAssets());
	}

    public static Main getInstance() {
		if (instance == null) {
	    	instance = new Main();
		}

		return instance;
    }

    @Override
    public void initStatesList(GameContainer arg0) throws SlickException {
	  StateMainMenu stateMainMenu = new StateMainMenu();
	  StateGameMap stateGameMap = new StateGameMap(arg0);
	  StatePauseMenu statePauseMenu = new StatePauseMenu();
	  StateLoadingScreen stateLoadingScreen = new StateLoadingScreen();
	  StateTestScreen stateTestScreen = new StateTestScreen();

	  this.addState(stateMainMenu);
	  this.addState(stateGameMap);
	  this.addState(statePauseMenu);
	  this.addState(stateLoadingScreen);
	  this.addState(stateTestScreen);

	  gameStates.add(stateMainMenu);
	  gameStates.add(stateGameMap);
	  gameStates.add(statePauseMenu);
	  gameStates.add(stateLoadingScreen);
	  gameStates.add(stateTestScreen);

	  androidInput.setInputProcessor(stateMainMenu);

	  // Disable native cursor
	  //Cursor emptyCursor;
	  try {
	      //emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
	      //Mouse.setNativeCursor(emptyCursor);
	  } catch (Exception e) {
	      e.printStackTrace();
	  }
    }

    public Camera getCamera() {
	return camera;
    }

    public Controller getController() {
	return controller;
    }

    public World getWorld() {
	return w;
    }

    public void setWorld(World w) {
	this.w = w;
    }

    public void setAndroidInput(AndroidInput androidInput)
	{
		this.androidInput = androidInput;
	}

    public void startNewGame(String mapName) {
		rm = ResourceManager.getInstance();
		rm.loadBibs();

		camera = new Camera();
		try {
		    camera.init(this.getContainer());
		} catch (SlickException e1) {
		    e1.printStackTrace();
		}

		controller = new Controller(null, camera, this.getContainer().getInput());
		w = new World("haos-ridges",
			this.getContainer(), camera);

		initGame();
    }

    public void initGame() {
		Random r = new Random();

		this.observerShroudRenderer = new ShroudRenderer(w);

		team = new Team();
		player = new Player(w, "Player", Alignment.SOVIET, new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
		player.setTeam(team);

		//player.setShroud(null);

		bo = new BuildingOverlay(player, w);

		w.addPlayer(player);

		Pos playerSpawn = player.getPlayerSpawnPoint();

		//this.getCamera().setOffset(-Math.max(w.getMap().getBounds().getMinX(), (playerSpawn.getX() * 24) - this.getContainer().getWidth() / 2), -Math.max(w.getMap().getBounds().getMinY(), (playerSpawn.getY() * 24)));

		this.getCamera().scrollCenterToCell(playerSpawn);

		this.gsb = new GameSideBar(Main.getInstance().getTeam(), Main.getInstance().getPlayer());
		this.gsb.initSidebarPages();

		Team team2 = new Team();
		Player otherPlayer = new AIPlayer(w, "NormalAI", Alignment.SOVIET, new Color(128, 0, 0));
		player.setTeam(team2);
		w.addPlayer(otherPlayer);
    }

    public void ChangeStateByID(int id)
	{
		InputGameState gameState = null;
		for(int i=0;i<gameStates.size();i++)
		{
			if(gameStates.get(i).getID()==id)
			{
				gameState = gameStates.get(i);
				break;
			}
		}
		androidInput.setInputProcessor(gameState);
		enterState(id);
	}

    public Player getPlayer() {
		return this.player;
    	}

    public Team getTeam() {
		return this.player.getTeam();
    }


    public GameSideBar getSideBar() {
		return this.gsb;
    }

    public ShroudRenderer getObserverShroudRenderer() {
		return this.observerShroudRenderer;
    }

    public BuildingOverlay getBuildingOverlay() {
		return this.bo;
    }
}
