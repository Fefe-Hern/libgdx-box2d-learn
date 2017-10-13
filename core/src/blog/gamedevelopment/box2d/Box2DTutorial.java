package blog.gamedevelopment.box2d;

import blog.gamedevelopment.box2d.loader.B2DAssetManager;
import blog.gamedevelopment.box2d.views.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;

public class Box2DTutorial extends Game {
    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private MenuScreen menuScreen;
    private MainScreen mainScreen;
    private EndScreen endScreen;
    public B2DAssetManager assetManager = new B2DAssetManager();
    Music song;

    public final static int MENU = 0;
    public final static int PREFERENCES = 1;
    public final static int APPLICATION = 2;
    public final static int ENDGAME = 3;

    private AppPreferences preferences;

    @Override
    public void create() {
        preferences = new AppPreferences();

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);

        assetManager.queueAddMusic();
        assetManager.manager.finishLoading();
        song = assetManager.manager.get("music/main.mp3");
        //song.play();

    }

    public void changeScreen(int screen) {
        switch(screen) {
            case MENU:
                if(menuScreen == null) menuScreen = new MenuScreen(this);
                this.setScreen(menuScreen);
                break;
            case PREFERENCES:
                if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
                this.setScreen(preferencesScreen);
                break;
            case APPLICATION:
                if(mainScreen == null) mainScreen = new MainScreen(this);
                this.setScreen(mainScreen);
                break;
            case ENDGAME:
                if(endScreen == null) endScreen = new EndScreen(this);
                this.setScreen(endScreen);
                break;
        }
    }

    public AppPreferences getPreferences() {
        return preferences;
    }

    @Override
    public void dispose() {
        super.dispose();
        song.dispose();
        assetManager.manager.dispose();
    }
}
