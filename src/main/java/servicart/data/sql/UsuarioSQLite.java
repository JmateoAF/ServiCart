package servicart.data.sql;

import servicart.data.interfaces.InterfazUsuario;
import servicart.core.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSQLite implements InterfazUsuario {
    @Override
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, cedula) VALUES (?, ?)";

        // Abre y cierra automáticamente
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCedula());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, cedula = ? WHERE id = ?";

        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCedula());
            stmt.setInt(3, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        // Abre, ejecuta y cierra por las mismas
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT id, username, cedula FROM usuarios";
        List<Usuario> lista = new ArrayList<>();

        // En un SELECT también metemos el ResultSet dentro del try()
        // para que se cierre junto con la conexión y el statement
        try (Connection con = ConexionSQLite.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(0, null, null);
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("username"));
                usuario.setCedula(rs.getString("cedula"));

                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }
}
