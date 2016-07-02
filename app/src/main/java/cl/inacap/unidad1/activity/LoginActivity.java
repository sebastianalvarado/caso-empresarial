package cl.inacap.unidad1.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cl.inacap.unidad1.clases.OperacionesBaseDatos;
import cl.inacap.unidad1.clases.Vendedor;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

		//se instancia la base de datos al inicio de la aplicacion
		OperacionesBaseDatos.instanciarContexto(this.getApplicationContext());

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
			OperacionesBaseDatos.login_vendedor = txt_login.getText().toString(); //se setea el usuario conectado
			txt_login.setText("");
			txt_contrasena.setText("");
			Intent intent = new Intent(LoginActivity.this, PedidosActivity.class);
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

	}
}
