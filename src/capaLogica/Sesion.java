package capaLogica;

/**
 *
 * @author Fabian Antonio Carrasco Vera
 */
public class Sesion {

    private static UsuarioSesion usuarioActual;

    public static void setUsuario(UsuarioSesion usuario) {
        usuarioActual = usuario;
    }

    public static UsuarioSesion getUsuario() {
        return usuarioActual;
    }
}
