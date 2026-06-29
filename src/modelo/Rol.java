package modelo;

/**
 * Roles del sistema. Coinciden exactamente con el tipo ENUM de la tabla
 * usuarios, para no romper la trazabilidad entre Java y la base.
 */
public enum Rol {
    ADMIN,
    VENDEDOR,
    OPERADOR,
    TRANSPORTISTA
}
