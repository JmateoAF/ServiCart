package servicart.data.sql;

import servicart.core.models.Usuario;
import servicart.data.interfaces.InterfazCRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioSQLite implements InterfazCRUD<Usuario> {
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
}
