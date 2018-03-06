package com.unlucky.ui.battleui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.entity.enemy.Boss;
import com.unlucky.entity.enemy.Enemy;
import com.unlucky.entity.Player;
import com.unlucky.event.*;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;
import com.unlucky.ui.UI;

/**
 * Handles all UI for battle scenes
 *
 * @author Ming Li
 */
public class BattleUIHandler extends UI implements Disposable {

    // Scene2D
    public Stage stage;
    public Viewport viewport;
    public MoveUI moveUI;
    public com.unlucky.ui.battleui.BattleEventHandler battleEventHandler;
    public com.unlucky.ui.battleui.BattleScene battleScene;

    // battle
    public com.unlucky.event.BattleState currentState;

    public BattleUIHandler(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle, ResourceManager rm) {
        super(gameScreen, tileMap, player, rm);

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.getBatch());

        currentState = com.unlucky.event.BattleState.NONE;

        battleScene = new com.unlucky.ui.battleui.BattleScene(gameScreen, tileMap, player, battle, this, stage, rm);
        moveUI = new MoveUI(gameScreen, tileMap, player, battle, this, stage, rm);
        battleEventHandler = new com.unlucky.ui.battleui.BattleEventHandler(gameScreen, tileMap, player, battle, this, stage, rm);

        moveUI.toggleMoveAndOptionUI(false);
        battleEventHandler.endDialog();
    }

    public void update(float dt) {
        if (currentState == com.unlucky.event.BattleState.MOVE) moveUI.update(dt);
        if (currentState == com.unlucky.event.BattleState.DIALOG) battleEventHandler.update(dt);
        battleScene.update(dt);
    }

    public void render(float dt) {
        battleScene.render(dt);

        stage.act(dt);
        stage.draw();

        if (currentState == com.unlucky.event.BattleState.MOVE) moveUI.render(dt);
        if (currentState == com.unlucky.event.BattleState.DIALOG) battleEventHandler.render(dt);
    }

    /**
     * When the player first encounters the enemy and engages in battle
     * There's a 1% chance that the enemy doesn't want to fight
     *
     * @param enemy
     */
    public void engage(Enemy enemy) {
        player.setDead(false);
        moveUI.init();
        battleScene.resetPositions();
        battleScene.toggle(true);
        currentState = com.unlucky.event.BattleState.DIALOG;

        String[] intro;
        boolean saved = Util.isSuccess(Util.SAVED_FROM_BATTLE);

        if (enemy.isBoss()) {
            if (MathUtils.randomBoolean()) {
                intro = new String[] {
                        "you encountered the boss " + enemy.getId() + "!",
                        "its power is far greater than any regular enemy.",
                        "Passive: " + ((Boss) enemy).getPassiveDescription()
                };
                battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.PLAYER_TURN);
            } else {
                intro = new String[] {
                        "you encountered the boss " + enemy.getId() + "!",
                        "its power is far greater than any regular enemy.",
                        "Passive: " + ((Boss) enemy).getPassiveDescription(),
                        enemy.getId() + " strikes first!"
                };
                battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.ENEMY_TURN);
            }
        }
        else {
            if (saved) {
                intro = new String[]{
                        "you encountered " + enemy.getId() + "! " +
                                "maybe there's a chance it doesn't want to fight...",
                        "the enemy stares at you and decides to flee the battle."
                };
                battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.END_BATTLE);
            } else {
                // 50-50 chance for first attack from enemy or player
                if (MathUtils.randomBoolean()) {
                    intro = new String[]{
                            "you encountered " + enemy.getId() + "! " +
                                    "maybe there's a chance it doesn't want to fight...",
                            "the enemy glares at you and decides to engage in battle!"
                    };
                    battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.PLAYER_TURN);
                } else {
                    intro = new String[]{
                            "you encountered " + enemy.getId() + "! " +
                                    "maybe there's a chance it doesn't want to fight...",
                            "the enemy glares at you and decides to engage in battle!",
                            enemy.getId() + " attacks first!"
                    };
                    battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.ENEMY_TURN);
                }
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}