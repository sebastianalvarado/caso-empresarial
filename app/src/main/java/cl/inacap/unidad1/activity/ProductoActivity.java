package cl.inacap.unidad1.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import cl.inacap.unidad1.clases.Producto;

public class ProductoActivity extends AppCompatActivity {
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        LinearLayout ll_idproducto = (LinearLayout)findViewById(R.id.ll_idproducto);
        TextView txv_idproducto = (TextView)findViewById(R.id.txv_idproducto);
        final EditText txt_nombre_producto = (EditText)findViewById(R.id.txt_nombre_producto);
        final EditText txt_precio_producto = (EditText)findViewById(R.id.txt_precio_producto);
        final CheckBox cb_estado_producto = (CheckBox)findViewById(R.id.cb_estado_producto);
        Button btn_gestionar_producto  = (Button)findViewById(R.id.btn_gestionar_producto);
        Button btn_eliminar_producto  = (Button)findViewById(R.id.btn_eliminar_producto);


        btn_eliminar_producto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String resultado = producto.EliminarProducto();
                Toast.makeText(ProductoActivity.this,resultado, Toast.LENGTH_LONG).show();
                actualizarActivity();

            }
        });


        Bundle bundle = getIntent().getExtras().getBundle("extras");
        String accion = bundle.getString("accion"); //se recibe la accion que empleara esta activity

        if(accion.equals("editar")) {
            //le asigna nombre al boton de gestion
            btn_gestionar_producto.setText("EDITAR");
            producto = (Producto) bundle.getSerializable("producto");
            txv_idproducto.setText(String.valueOf(producto.id_producto));
            txt_nombre_producto.setText(String.valueOf(producto.nombre_producto));
            txt_precio_producto.setText(String.valueOf(producto.precio_producto));
            cb_estado_producto.setChecked(producto.estado_producto);
            cb_estado_producto.setClickable(!producto.estado_producto);
            //se indica que hará el boton de gestión
            btn_gestionar_producto.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    producto.nombre_producto = txt_nombre_producto.getText().toString();
                    producto.precio_producto = Integer.parseInt(txt_precio_producto.getText().toString());
                    producto.estado_producto = cb_estado_producto.isChecked();
                    String respuesta = producto.ModificarProducto(); //se modifica el Producto
                    Toast.makeText(ProductoActivity.this,respuesta, Toast.LENGTH_LONG).show();
                    actualizarActivity();
                }

            });
        }
        else if(accion.equals("nuevo"))
        {
            ll_idproducto.setVisibility(LinearLayout.GONE);
            btn_eliminar_producto.setVisibility(View.GONE);
            btn_gestionar_producto.setText("Nuevo");

            //se indica que hará el boton de gestión
            btn_gestionar_producto.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    producto = new Producto();
                    producto.nombre_producto = txt_nombre_producto.getText().toString();
                    producto.precio_producto = Integer.parseInt(txt_precio_producto.getText().toString());
                    producto.estado_producto = cb_estado_producto.isChecked();

                    String resultado = producto.AgregarProducto(); //agrega un nuevo Producto
                    Toast.makeText(ProductoActivity.this,resultado, Toast.LENGTH_LONG).show();
                    //se vuelve a inicializar la activity con el nuevo Producto para gestionar su modificacion
                    actualizarActivity();

                }

            });

        }
        else {
            Toast.makeText(ProductoActivity.this,"OPCION ERRONEA Producto", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //se vuelve a inicializar la activity
    void actualizarActivity()
    {
        Intent intent = new Intent(ProductoActivity.this, ProductoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("accion","editar");
        bundle.putSerializable("producto",(Serializable)producto);
        intent.putExtra("extras",bundle);
        startActivity(intent);
        finish();
    }

}
