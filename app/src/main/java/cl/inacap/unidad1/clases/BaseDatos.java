package cl.inacap.unidad1.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {


    public static String DB_NAME = "segundaaplicacion.db"; //Nombre de la base de datos
    public static int version_db = 3; //version de base de datos


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
                                            "precio_producto DOUBLE," +
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
                                            "precio_pedido DOUBLE ," +
                                            "estado_pedido INTEGER ," +
                                            "longitud_pedido DOUBLE ," +
                                            "latitud_pedido DOUBLE ," +
                                            "direccion_pedido VARCHAR(200)) ";

    //query para la tabla de pedidos
    private String CREATE_TABLE_CONFIGURACION = "CREATE TABLE configuracion" +
            "(" +
            "fecha_personal         VARCHAR(20) PRIMARY KEY," +
            "fecha_formato          INTEGER ," +
            "moneda_personal        INTEGER ," +
            "moneda_formato         INTEGER ," +
            "moneda_valor           DOUBLE ) ";

    //consultas de actualizacion de version
    private String ALTER_TABLE_PEDIDO_longitud = "ALTER TABLE pedido ADD longitud_pedido DOUBLE";
    private String ALTER_TABLE_PEDIDO_latitud = "ALTER TABLE pedido ADD latitud_pedido DOUBLE";
    private String ALTER_TABLE_PEDIDO_direccion = "ALTER TABLE pedido ADD direccion_pedido VARCHAR(200)";

    //valores iniciales de la base de datos
    private String[] INSERT_DB = {
            "INSERT INTO vendedor(login_vendedor,nombre_vendedor,contrasena) VALUES('user1','Usuario Uno','user1');",
            "INSERT INTO vendedor(login_vendedor,nombre_vendedor,contrasena) VALUES('user2','Usuario Dos','user2');",
            "INSERT INTO vendedor(login_vendedor,nombre_vendedor,contrasena) VALUES('user3','Usuario Tres','user3');",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO COOL',          1,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO  BIEN BARATO',  0.5,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO WENO',          0.75,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO PROMEDIO',      3,0);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO SELECCION',     6,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('PRODUCTO COLECCION',     6,1);",
            "INSERT INTO producto(nombre_producto,precio_producto,estado_producto) VALUES('VAYA QUE BUEN PRODUCTO', 5.5,1);",

            "INSERT INTO cliente(nombre_cliente,estado_cliente) VALUES('Juan','1')",
            "INSERT INTO cliente(nombre_cliente,estado_cliente) VALUES('Ramon','1')",
            "INSERT INTO cliente(nombre_cliente,estado_cliente) VALUES('Carlos','1')",
            "INSERT INTO cliente(nombre_cliente,estado_cliente) VALUES('Manuel','1')",

            "INSERT INTO pedido(id_cliente,id_producto,vendedor ,cantidad_producto,fecha_pedido,precio_pedido,estado_pedido,longitud_pedido,latitud_pedido,direccion_pedido)" +
                    " VALUES(1,1,'user1',1,'2/8/2016',1,0,-70.6204304,-33.4844593,'Vicuña Mackenna 52, Macul, San Joaquín, Región Metropolitana, Chile')",

            "INSERT INTO pedido(id_cliente,id_producto,vendedor ,cantidad_producto,fecha_pedido,precio_pedido,estado_pedido,longitud_pedido,latitud_pedido,direccion_pedido)" +
                    " VALUES(2,3,'user1',2,'5/9/2016',1.5,0,-70.5832571,-33.5173093,'La Florida 9, La Florida, Región Metropolitana, Chile')",

            "INSERT INTO pedido(id_cliente,id_producto,vendedor ,cantidad_producto,fecha_pedido,precio_pedido,estado_pedido,longitud_pedido,latitud_pedido,direccion_pedido)" +
                    " VALUES(3,5,'user2',1,'10/11/2016',6,0,-70.6493391,-33.4447741,'Serrano 40, Santiago, Región Metropolitana, Chile')",

            "INSERT INTO pedido(id_cliente,id_producto,vendedor ,cantidad_producto,fecha_pedido,precio_pedido,estado_pedido,longitud_pedido,latitud_pedido,direccion_pedido)" +
                    " VALUES(4,6,'user2',2,'2/10/2016',12,0,-70.634279,-33.435022,'Bellavista 75, Providencia, Región Metropolitana, Chile')",

            "INSERT INTO configuracion(fecha_personal,fecha_formato,moneda_personal,moneda_formato,moneda_valor) VALUES(0,'dd_MM_yyyy',0,'CLP',680);"

    };
    //update tabla preoducto y pedidos
    private String[] UPDATE_PRODUCTO = {
            "ALTER TABLE producto RENAME TO tmp_producto;",
            CREATE_TABLE_PRODUCTO,
            "INSERT INTO producto (id_producto,nombre_producto,precio_producto,estado_producto)" +
                    "SELECT id_producto,nombre_producto,precio_producto,estado_producto FROM tmp_producto",
            "DROP TABLE tmp_producto;",
            "UPDATE pedido SET precio_pedido = 1    WHERE id_pedido = 1",
            "UPDATE pedido SET precio_pedido = 1.5  WHERE id_pedido = 2",
            "UPDATE pedido SET precio_pedido = 6    WHERE id_pedido = 3",
            "UPDATE pedido SET precio_pedido = 12   WHERE id_pedido = 4"
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
        db.execSQL(this.CREATE_TABLE_CONFIGURACION);


        //se itera en los valores iniciales para insertar en la base de datos
        for (String insert :
                this.INSERT_DB) {
            db.execSQL(insert);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            if(oldVersion != 2) {
                db.execSQL(this.ALTER_TABLE_PEDIDO_longitud);
                db.execSQL(this.ALTER_TABLE_PEDIDO_latitud);
                db.execSQL(this.ALTER_TABLE_PEDIDO_direccion);
            }
            db.execSQL(this.CREATE_TABLE_CONFIGURACION);
            db.execSQL("INSERT INTO configuracion(fecha_personal,fecha_formato,moneda_personal,moneda_formato,moneda_valor) VALUES(0,'dd/MM/yyyy',0,'USD',1);");
            for (String insert :
                    this.UPDATE_PRODUCTO) {
                db.execSQL(insert);
            }

        }
    }
}

