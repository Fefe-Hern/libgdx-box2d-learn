package blog.gamedevelopment.box2d.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class KeyboardController implements InputProcessor {

    public boolean left, right, up, down;
    public boolean mouse1Down, mouse2Down, mouse3Down;
    public boolean isDragged;
    public Vector2 mouseLocation;

    public KeyboardController() {
        this.mouseLocation = new Vector2();
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;
        switch(keycode) {
            case Keys.LEFT:
                left = true;
                keyProcessed = true;
                break;
            case Keys.RIGHT:
                right = true;
                keyProcessed = true;
                break;
            case Keys.UP:
                up = true;
                keyProcessed = true;
                break;
            case Keys.DOWN:
                down = true;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyProcessed = false;
        switch(keycode) {
            case Keys.LEFT:
                left = false;
                keyProcessed = true;
                break;
            case Keys.RIGHT:
                right = false;
                keyProcessed = true;
                break;
            case Keys.UP:
                up = false;
                keyProcessed = true;
                break;
            case Keys.DOWN:
                down = false;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch(button) {
            case 0: mouse1Down = true; break;
            case 1: mouse2Down = true; break;
            case 2: mouse3Down = true; break;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;
        //System.out.println(button);
        switch(button) {
            case 0: mouse1Down = false; break;
            case 1: mouse2Down = false; break;
            case 2: mouse3Down = false; break;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
