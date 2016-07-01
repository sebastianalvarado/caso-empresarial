package cl.inacap.unidad1.clases;


import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Vendedor {

    public String nombre_vendedor;
    public String login_vendedor;
    public String contrasena;


    //nombre de las columnas de la tabla
    private String COL_LOGIN_VENDEDOR = "login_vendedor";
    private String COL_NOMBRE_VENDEDOR = "nombre_vendedor";
    private String COL_CONTRASENA_VENDEDOR = "contrasena";
    //se juntan las columnas para generar los query con todas las columnas
    public String[] columnas = {
            this.COL_LOGIN_VENDEDOR,
            this.COL_NOMBRE_VENDEDOR,
            this.COL_CONTRASENA_VENDEDOR
    };


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
                return true;
        }
        return false;
    }

    //funcion para transformar un cursor a la clase tipo
    private Vendedor cursorToVendedor(Cursor cursor) {
        Vendedor vendedor = new Vendedor();
        vendedor.login_vendedor = cursor.getString(cursor.getColumnIndex("login_vendedor"));
        vendedor.nombre_vendedor = cursor.getString(cursor.getColumnIndex("nombre_vendedor"));
        vendedor.contrasena = cursor.getString(cursor.getColumnIndex("contrasena"));
        return vendedor;
    }

    //Se genera y obtiene la lista de usuarios
    public ArrayList<Vendedor> listaUsuarios(){
        ArrayList<Vendedor> vendedores = new ArrayList<Vendedor>();
        //se genera la query de consulta de vendedores
        Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query("vendedor",columnas , null, null, null, null, null);
        cursor.moveToFirst();
        //se recorre el cursor con los resultados
        while (!cursor.isAfterLast()) {
            //se transforma el cursor a la clase
            Vendedor vendedor = cursorToVendedor(cursor);
            vendedores.add(vendedor);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return vendedores;
    }
}
