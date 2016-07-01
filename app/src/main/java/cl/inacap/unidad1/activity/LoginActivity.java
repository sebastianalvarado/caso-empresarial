package cl.inacap.unidad1.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cl.inacap.unidad1.clases.JsonUtil;
import cl.inacap.unidad1.clases.Vendedor;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Button btn_ingresar = (Button)findViewById(R.id.btn_ingresar);
        btn_ingresar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				validarLoginUsuario();				
			}

        });

    }

    //Se realiza la validacion del usuario
    public void validarLoginUsuario()
    {
    	EditText txt_login = (EditText)findViewById(R.id.txt_login);
    	EditText txt_contrasena = (EditText)findViewById(R.id.txt_contrasena);

        Vendedor usuario = new Vendedor();
    	if(usuario.validarLogin(txt_login.getText().toString(), txt_contrasena.getText().toString()))
    	{
    		Toast.makeText(LoginActivity.this, "Usuario correcto", Toast.LENGTH_SHORT).show();
    		
    		txt_login.setText("");
    		txt_contrasena.setText("");
    		
    		Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
    		LoginActivity.this.startActivity(intent);
    	}
    	else
    	{
    		Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
    	}
    }

	@Override
	protected void onResume() {
		super.onResume();

		//se asignan variable para archivos locales
		InputStream is_vendedores = null;
		InputStream is_clientes = null;
		InputStream is_productos = null;
		InputStream is_pedidos = null;
		OutputStream os_vendedores = null;
		OutputStream os_clientes = null;
		OutputStream os_productos = null;
		OutputStream os_pedidos = null;

		String mensaje_error = "";
		try {
			//se buscan los archivos locales
			is_vendedores = getAssets().open("vendedores.json");
			is_clientes = getAssets().open("clientes.json");
			is_productos = getAssets().open("productos.json");
			is_pedidos = getAssets().open("pedidos.json");

			os_vendedores = openFileOutput("vendedores.json", MODE_PRIVATE);
			os_clientes = openFileOutput("clientes.json", MODE_PRIVATE);
			os_productos = openFileOutput("productos.json", MODE_PRIVATE);
			os_pedidos = openFileOutput("pedidos.json", MODE_PRIVATE);

			//control de errores en caso de no encontrar los archivos o problemas de lectura
		} catch (FileNotFoundException e) {
			mensaje_error = "\nArchivos no encontrados";
			e.printStackTrace();
		} catch (IOException e) {
			mensaje_error = "\nError al intentar obtener los archivos";
			e.printStackTrace();
		}

		JsonUtil jsonUtil = new JsonUtil();

		try {
			//se asignan los archivos al JsonUtil
			jsonUtil.poblarJsonArray(is_vendedores,is_clientes,is_productos,is_pedidos);
			jsonUtil.asignarArchivos(os_clientes,os_productos,os_pedidos);

		//control de errores en caso de no poder leer los archivos o problemas al pasarlos a json array
		} catch (IOException e) {
			mensaje_error = "\nError al intentar leer archivos";
			e.printStackTrace();
		} catch (JSONException e) {
			mensaje_error = "\nError al intentar leer los datos de archivos";
			e.printStackTrace();
		}
		if(mensaje_error.length() > 0)
			Toast.makeText(LoginActivity.this, mensaje_error, Toast.LENGTH_SHORT).show();

	}
}
