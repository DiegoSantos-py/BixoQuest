package service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class AudioService {

    private MediaPlayer mediaPlayer;

    /**
     * Toca uma música em loop contínuo (BGM - Background Music).
     * @param path Caminho do recurso (ex: "/assets/audio/boss_theme.mp3")
     */
    public void playBGM(String path) {
        stopBGM(); // Para qualquer música tocando atualmente

        try {
            URL resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("Áudio não encontrado: " + path);
                return;
            }
            
            Media media = new Media(resource.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop infinito
            mediaPlayer.setVolume(0.5); // Volume inicial em 50%
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Erro ao carregar o áudio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Toca um efeito sonoro uma única vez (SFX - Sound Effect).
     * @param path Caminho do recurso (ex: "/assets/audio/hit.mp3")
     */
    public void playSFX(String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("SFX não encontrado: " + path);
                return;
            }
            
            Media media = new Media(resource.toString());
            MediaPlayer sfxPlayer = new MediaPlayer(media);
            sfxPlayer.setVolume(0.8);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Erro ao tocar o SFX: " + e.getMessage());
        }
    }

    /**
     * Para a música de fundo atual.
     */
    public void stopBGM() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    /**
     * Define o volume da música de fundo.
     * @param volume Valor entre 0.0 e 1.0
     */
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
}
