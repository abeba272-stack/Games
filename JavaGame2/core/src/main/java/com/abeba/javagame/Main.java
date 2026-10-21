package com.abeba.javagame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
    private static final int MAX_LP = 100;

    private SpriteBatch batch;
    private BitmapFont font;
    private Held held;
    private Charakter gegner;
    private int heiltraenke;
    private int goblinNummer;
    private int orcNummer;
    private boolean gameOver;
    private String meldung;
    private String schadensmeldung = "";
    private float meldungszeit = 0f;
    private boolean gegenangriffAusstehend;
    private float gegenangriffTimer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        neuesSpiel();
    }

    @Override
    public void render() {
        eingabenVerarbeiten();
        ScreenUtils.clear(0.08f, 0.11f, 0.12f, 1f);

        batch.begin();

        float links = 36f;
        float oben = Gdx.graphics.getHeight() - 34f;
        float rechts = Gdx.graphics.getWidth() * 0.7f;

        font.setColor(0.96f, 0.76f, 0.38f, 1f);
        font.getData().setScale(1.6f);
        font.draw(batch, "ABEBAS ABENTEUER", links, oben);

        font.setColor(Color.WHITE);
        font.getData().setScale(1.15f);
        font.draw(batch, "Held: " + held.name + "   Level: " + held.lvl, links, oben - 50f);
        font.draw(batch, "Lebenspunkte: " + held.lp + " / " + MAX_LP, links, oben - 83f);
        font.draw(batch, "Heiltraenke: " + heiltraenke, links, oben - 116f);

        if (!gameOver && gegner != null) {
            if (gegner.lp > 0 && held.lp > 0) {
                if (gegenangriffAusstehend) {
                    gegenangriffTimer -= Gdx.graphics.getDeltaTime();

                    if (gegenangriffTimer <= 0f) {
                        gegenangriffAusstehend = false;
                        int schadenGegner = gegner.Angriff(held);
                        schadensmeldung = "Gegner verursacht " + schadenGegner + " Schaden";
                    }
                }
            }
        }
        

        meldungszeit -= Gdx.graphics.getDeltaTime();
        if (meldungszeit > 0) {
            font.setColor(0.96f, 0.52f, 0.40f, 1f);
            font.getData().setScale(1.15f);
            font.draw(batch, schadensmeldung, rechts, oben - 149f);
        }

        if (gegner != null) {
            font.setColor(0.96f, 0.52f, 0.40f, 1f);
            font.draw(batch, "Gegner: " + gegner.name, links, oben - 184f);
            font.setColor(Color.WHITE);
            font.draw(batch, "Gegner-Lebenspunkte: " + gegner.lp, links, oben - 217f);
        } else if (!gameOver) {
            font.setColor(0.55f, 0.88f, 0.68f, 1f);
            font.draw(batch, "Gegner besiegt!", links, oben - 184f);
        }

        font.setColor(Color.WHITE);
        font.draw(batch, meldung, links, oben - 278f);
        font.setColor(0.72f, 0.82f, 0.82f, 1f);
        font.draw(batch, "Leertaste: Angriff / naechster Gegner", links, 74f);
        font.draw(batch, "H: Heiltrank nach einem Kampf", links, 48f);
        if (gameOver) {
            font.setColor(1f, 0.42f, 0.36f, 1f);
            font.draw(batch, "R: Neues Spiel", links, 22f);
        }
        batch.end();
    }

    private void eingabenVerarbeiten() {
        if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                neuesSpiel();
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            heiltrankBenutzen();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (gegner == null) {
                gegnerErstellen();
                meldung = "Ein neuer Gegner erscheint: " + gegner.name;
            } else {
                if (!gegenangriffAusstehend) {
                    kampfrunde();
                }
            }
        }
    }

    private void kampfrunde() {
        int schadenHeld = held.Angriff(gegner);
        schadensmeldung = "Schaden Gegeben: " + schadenHeld;
        meldungszeit = 3f;
        gegenangriffTimer = 3f;

        if (gegner.lp <= 0) {
            if (gegner instanceof Goblin) {
                held.levelAufstieg(1);
            } else {
                held.levelAufstieg(2);
            }
            meldung = gegner.name + " besiegt! Du steigst ein Level auf.";
            gegner = null;
            return;
        } else {
            gegenangriffAusstehend = true;
        }

        if (held.lp <= 0) {
            held.lp = 0;
            gameOver = true;
            meldung = "Du wurdest besiegt.";
        } else {
            meldung = "Du greifst an und der Gegner schlaegt zurueck.";
        }
    }

    private void heiltrankBenutzen() {
        if (gegner != null) {
            meldung = "Heilen kannst du erst nach einem gewonnenen Kampf.";
        } else if (heiltraenke <= 0) {
            meldung = "Du hast keine Heiltraenke mehr.";
        } else if (held.lp >= MAX_LP) {
            meldung = "Deine Lebenspunkte sind bereits voll.";
        } else {
            held.lp = Math.min(MAX_LP, held.lp + 20);
            heiltraenke--;
            meldung = "Heiltrank benutzt. Uebrige Traenke: " + heiltraenke;
        }
    }

    private void gegnerErstellen() {
        if (Math.random() < 0.5) {
            gegner = new Goblin(40, 1, "Goblin " + goblinNummer++);
        } else {
            gegner = new Orc(200, 1, "Orc " + orcNummer++);
        }
    }

    private void neuesSpiel() {
        held = new Held(1, MAX_LP, "Abeba");
        heiltraenke = 10;
        goblinNummer = 1;
        orcNummer = 1;
        gameOver = false;
        gegnerErstellen();
        meldung = "Ein Gegner erscheint. Druecke die Leertaste zum Angreifen.";
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}