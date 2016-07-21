package cl.inacap.unidad1.clases;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import cl.inacap.unidad1.activity.R;

public class Configuracion implements Serializable {

    public boolean  fecha_personal;
    public String   fecha_formato;
    public boolean  moneda_personal;
    public String   moneda_formato;
    public Double   moneda_valor;

    //nombre de las columnas de la tabla
    private String COL_FECHA_PERSONAL = "fecha_personal";
    private String COL_FECHA_FORMATO = "fecha_formato";
    private String COL_MONEDA_PERSONAL = "moneda_personal";
    private String COL_MONEDA_FORMATO = "moneda_formato";
    private String COL_MONEDA_VALOR = "moneda_valor";
    //se juntan las columnas para generar los query con todas las columnas
    private String[] columnas = {
            COL_FECHA_PERSONAL,
            COL_FECHA_FORMATO,
            COL_MONEDA_PERSONAL,
            COL_MONEDA_FORMATO,
            COL_MONEDA_VALOR
    };
    private String nombreTabla = "configuracion";

    //funcion para transformar un cursor a la clase tipo
    private Configuracion cursorToConfiguracion(Cursor cursor) {

        Configuracion configuracion = new Configuracion();
        configuracion.fecha_personal = (cursor.getInt(cursor.getColumnIndex(COL_FECHA_PERSONAL))) == 1 ? true : false;
        configuracion.fecha_formato = cursor.getString(cursor.getColumnIndex(COL_FECHA_FORMATO));
        configuracion.moneda_personal = (cursor.getInt(cursor.getColumnIndex(COL_MONEDA_PERSONAL))) == 1 ? true : false;
        configuracion.moneda_formato = cursor.getString(cursor.getColumnIndex(COL_MONEDA_FORMATO));
        configuracion.moneda_valor = cursor.getDouble(cursor.getColumnIndex(COL_MONEDA_VALOR));
        return configuracion;

    }
    //Obtiene la Configuracion
    public Configuracion ObtenerConfiguracion()    {
        Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,columnas , null, null, null, null, null);
        Configuracion configuracion;
        //se consulta si existe un registro
        if(cursor.moveToFirst()) {
            configuracion = cursorToConfiguracion(cursor);
            this.fecha_personal     = configuracion.fecha_personal ;
            this.fecha_formato      = configuracion.fecha_formato ;
            this.moneda_personal    = configuracion.moneda_personal ;
            this.moneda_formato     = configuracion.moneda_formato ;
            this.moneda_valor       = configuracion.moneda_valor ;
            cursor.close();
            return this;
        }
        return null;
    }

    //actualiza la configuracion
    public String ModificarConfiguracion()    {
        try {
            //al igual que al agregar un nuevo cliente, se verifica que este nombre esta en la base de datos
            Cursor cursor = OperacionesBaseDatos.obtenerInstancia().query(this.nombreTabla,this.columnas , null, null, null, null, null);
            Configuracion configuracion;
            //en caso de no encontrar configuracion entrega error
            if(!cursor.moveToFirst()) {
                configuracion = cursorToConfiguracion(cursor);
                return obtenerRecursoString(R.string.configuracion_no_encontrado);
            }
            else {
                //si no se encuentra, se crea el contenedor de datos para enviar una actualizacion
                ContentValues values = new ContentValues();
                values.put(this.COL_FECHA_PERSONAL  , this.fecha_personal ? 1 : 0);
                values.put(this.COL_FECHA_FORMATO   , this.fecha_formato);
                values.put(this.COL_MONEDA_PERSONAL , this.moneda_personal ? 1 : 0);
                values.put(this.COL_MONEDA_FORMATO  , this.moneda_formato);
                values.put(this.COL_MONEDA_VALOR    , this.moneda_valor);

                //se mandan los datos para modificar el registro
                OperacionesBaseDatos.escribirInstancia().update(this.nombreTabla, values, null, null);
                return obtenerRecursoString(R.string.configuracion_modifica_exito);
            }
        } catch (Exception e) {
            Log.e("SegundaAplicacion",e.toString());
            e.printStackTrace();
            return obtenerRecursoString(R.string.configuracion_modifica_error_exp);
        }
    }

    //formato de la fecha configurado
    public String FormatoFecha()
    {
        switch (this.fecha_formato)
        {
            case "dd_MM_yyyy":
                return "dd/MM/yyyy";
            case "dd_MMM_yyyy":
                return "dd MMM, yyyy";
            case "MM_dd_yyyy":
                return "MM/dd/yyyy";
            case "MMM_dd_yyyy":
                return "MMM dd, yyyy";
        }
        return "dd/MM/yyyy";
    }

    private String obtenerRecursoString(int recurso)
    {
        return OperacionesBaseDatos.mi_contexto.getResources().getString(recurso);
    }

}
