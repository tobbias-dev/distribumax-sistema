package modelo;

/**
 * Pequeño utilitario para "hashear" contraseñas de forma determinista.
 * No pretende ser seguro: solo evita guardar la clave en texto plano en
 * la base de prueba y permite reproducir el mismo valor desde Java y desde
 * el script SQL. En un sistema real acá iría BCrypt o similar.
 */
public final class Hash {

    private Hash() {
    }

    public static String de(String texto) {
        // hash propio de Java, convertido a hexadecimal y prefijado con 'h'
        return "h" + Integer.toHexString(texto.hashCode());
    }
}
