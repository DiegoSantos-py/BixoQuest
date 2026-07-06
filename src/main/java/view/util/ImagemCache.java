package view.util;

import javafx.scene.image.Image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;

public final class ImagemCache {

    private static final Map<String, Image> CACHE = new ConcurrentHashMap<>();

    private ImagemCache() {}

    public static Image get(String caminho) {
        return CACHE.computeIfAbsent(caminho, ImagemCache::carregar);
    }

    public static Image get(String caminho, double largura, double altura) {
        String chave = caminho + "|" + largura + "x" + altura;
        return CACHE.computeIfAbsent(chave, k ->
                new Image(
                        Objects.requireNonNull(ImagemCache.class.getResourceAsStream(caminho)),
                        largura, altura, false, true
                )
        );
    }

    private static Image carregar(String caminho) {
        return new Image(
                Objects.requireNonNull(ImagemCache.class.getResourceAsStream(caminho))
        );
    }
}