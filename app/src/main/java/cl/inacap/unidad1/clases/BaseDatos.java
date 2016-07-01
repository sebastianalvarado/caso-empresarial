package cl.inacap.unidad1.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {


    public static String DB_NAME = "segundaaplicacion.db"; //Nombre de la base de datos
    public static int version_db = 1; //version de base de datos


    //query para la creación de la tabla vendedor (usuarios)
    private String CREATE_TABLE_VENDEDOR = "CREATE TABLE vendedor(login_vendedor VARCHAR(5) PRIMARY KEY , " +
            "nombre_vendedor VARCHAR(20)," +
            "contrasena VARCHAR(5)); ";

    //query para la creación de la tabla de clientes
    private String CREATE_TABLE_CLIENTE = "CREATE TABLE cliente" +
                                            "(id_cliente INTEGER PRIMARY KEY AUTOINCREMENT , " +
                                            "nombre_cliente VARCHAR(20)," +
                                            "estado_cliente INTEGER" +
                                            "); ";

    //query para la creación de la tabla de productos
    private String CREATE_TABLE_PRODUCTO = "CREATE TABLE producto" +
                                            "(" +
                                            "id_producto INTEGER PRIMARY KEY AUTOINCREMENT," +
                                            "nombre_producto VARCHAR(20)," +
                                            "precio_producto INTEGER," +
                                            "estado_producto INTEGER" +
                                            ")";
    //query para la tabla de pedidos
    private String CREATE_TABLE_PEDIDO = "CREATE TABLE pedido" +
                                            "(" +
                                            "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT," +
                                            "id_cliente INTEGER ," +
                                            "id_producto INTEGER ," +
                                            "vendedor VARCHAR(5) ," +
                                            "cantidad_producto INTEGER ," +
                                            "fecha_pedido VARCHAR(10) ," +
                                            "precio_pedido INTEGER ," +
                                            "estado_pedido INTEGER" +
                                            ") ";


    //valores iniciales de la base de datos
    private String[] INSERT_DB = {
            "INSERT INTO vendedor(login_vendedor,nombre_vendedor,contrasena) VALUES('user1','Usuario Uno','user1');",
            "INSERT INTO vendedor(login_vendedor,nombre_vendedor,contrasena) VALUES('user2','Usuario Dos','user2');",
            "INSERT INTO vendedor(login_vendedor,nombre_vendedor,contrasena) VALUES('user3','Usuario Tres','user3');",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO COOL',500,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO  BIEN BARATO',200,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO WENO',400,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO PROMEDIO',1500,0);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO SELECCION',3000,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO COLECCION',3000,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('VAYA QUE BUEN PRODUCTO',2500,1);"

    };


    public BaseDatos(Context context)
    {
        super(context, DB_NAME, null, version_db);
        OperacionesBaseDatos.mi_contexto = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        if(db.isReadOnly())
        {
            db = getWritableDatabase();
        }

        //se crean las tablas en la base de datos
        db.execSQL(this.CREATE_TABLE_VENDEDOR);
        db.execSQL(this.CREATE_TABLE_CLIENTE);
        db.execSQL(this.CREATE_TABLE_PRODUCTO);
        db.execSQL(this.CREATE_TABLE_PEDIDO);
        //se itera en los valores iniciales para insertar en la base de datos
        for (String insert :
                this.INSERT_DB) {
            db.execSQL(insert);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

