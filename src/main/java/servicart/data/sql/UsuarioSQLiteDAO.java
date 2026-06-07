package servicart.data.sql;

import servicart.domain.models.Usuario;
import servicart.domain.interfaces.CrudDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioSQLiteDAO implements CrudDAO<Usuario> {
    @Override
    public void save(Usuario usuario) {
    }

    //@Override
    //public Optional<Usuario> findId(String id){
    //}

    //@Override
    //public List<Usuario> findAll(){
    //}

    @Override
    public void update(Usuario usuario) {
    }

    @Override
    public void delete(String id){
    }
}
