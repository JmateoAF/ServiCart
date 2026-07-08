package servicart.data.sqlite;

import servicart.data.interfaces.AdminDAO;
import servicart.entities.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminSQLiteDAO implements AdminDAO<Admin> {
    @Override
    public Optional<Admin> credenciales(String usuario, String contrasenia) {
        String sql = "SELECT usuario, contrasenia FROM Administradores WHERE usuario = ? AND contrasenia = ?";

        try (Connection con = ConexionSQLite.conectar(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, contrasenia);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(new Admin(rs.getString("usuario"), rs.getString("contrasenia")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return Optional.empty();
    }
}
