package cl.inacap.unidad1.clases;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Vendedor {

    public int id_vendedor;
    public String nombre_vendedor;
    public String login_vendedor;
    public String contrasena;

    //Se genera y obtiene la lista de usuarios
    public ArrayList<Vendedor> listaUsuarios()
    {
        ArrayList<Vendedor> lista = new ArrayList<Vendedor>();

        JSONArray jsonArray = JsonUtil._vendedores;
        if(jsonArray != null)
        {
            try {
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Vendedor vendedor = new Vendedor();
                    vendedor.id_vendedor = jsonObject.getInt("id_vendedor");
                    vendedor.nombre_vendedor = jsonObject.getString("nombre_vendedor");
                    vendedor.login_vendedor = jsonObject.getString("login_vendedor");
                    vendedor.contrasena = jsonObject.getString("contrasena");
                    lista.add(vendedor);
                }
                return lista;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    //Se realiza la validacion del Login de usuario
    public boolean validarLogin(String login, String contrasena)
    {
        Vendedor usuario;
        ArrayList<Vendedor> usuarios = listaUsuarios();
        int largo = usuarios.size();
        for(int i=0;i < largo;i++)
        {
            usuario = usuarios.get(i);
            if(usuario.login_vendedor.equals(login) && usuario.contrasena.equals(contrasena))
            {
                return true;
            }
        }

        return false;
    }


}
