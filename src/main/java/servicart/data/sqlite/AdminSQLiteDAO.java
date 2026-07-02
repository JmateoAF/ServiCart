package servicart.data.sqlite;

import servicart.data.interfaces.AdminDAO;
import servicart.entities.Administrador;
import servicart.exceptions.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminSQLiteDAO implements AdminDAO<Administrador> {
    @Override
    public Optional<Administrador> credenciales(String usuario, String contrasenia) {
        String sql = "SELECT usuario, contrasenia FROM Administradores WHERE usuario = ? AND contrasenia = ?";

        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasenia);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(new Administrador(rs.getString("usuario"), rs.getString("contrasenia")));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al validar credenciales de administrador", e);
        }

        return Optional.empty();
    }
}
