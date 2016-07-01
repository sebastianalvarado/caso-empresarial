package cl.inacap.unidad1.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

//clase para operar la base de datos
public class OperacionesBaseDatos {

    //variables de la clase de operaciones de bases de datos, son datos estaticos
    // ya que se utilizaran en el resto de la aplicaicon
    public static SQLiteDatabase baseDatos; //base de datos
    private static BaseDatos _base; //se genera para el contexto de la base de datos en t'do el sistema
    public static Context mi_contexto; //contexto desde la que es llamad ala base de datos
    public static String login_vendedor; //es el vendedor que esta manipulando la aplicacion

    //se genera la instancia de la base de datos con el contexto de llamada
    public static void instanciarContexto(Context contexto) {
        //se guarda el contexto
        OperacionesBaseDatos.mi_contexto = contexto;
        //se genera la base de datos
        _base = new BaseDatos(OperacionesBaseDatos.mi_contexto);

    }
    //se obtiene la instancia para lectura de datos
    public static SQLiteDatabase obtenerInstancia() {
        if (baseDatos == null) {
            //la clase BaseDatos genera la base de datos para su lectura
            baseDatos = _base.getReadableDatabase();
        }
        return baseDatos;
    }

    //se obtiene la instancia para escribir en la base de datos
    public static SQLiteDatabase escribirInstancia()
    {
        if (baseDatos == null) {
            //la clase Basedatos genera la base de datos para su escritura
            baseDatos = _base.getWritableDatabase();
        }
        return baseDatos;
    }
}
