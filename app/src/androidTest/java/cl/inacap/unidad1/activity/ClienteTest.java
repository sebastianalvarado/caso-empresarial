package cl.inacap.unidad1.activity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.RenamingDelegatingContext;

import java.util.ArrayList;

import cl.inacap.unidad1.clases.Cliente;
import cl.inacap.unidad1.clases.OperacionesBaseDatos;

//se crea la clase de test que utilizara la instancia de una activity
//se crean los test deesta forma, ya que al utilizar sqlite se necesita obtener el contexto de la aplicacion
public class ClienteTest extends ActivityUnitTestCase<ClienteActivity> {

    private ClienteActivity testActivity;

    private Cliente clienteTest = new Cliente();

    public ClienteTest() {
        super(ClienteActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //se inicializa la activity en la clase de test
        testActivity = getActivity();
        //se obtiene el contexto para ser usado en test
        RenamingDelegatingContext context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        //se instancia la base de datos que se utilizara con el contexto de test
        OperacionesBaseDatos.instanciarContexto(context);
    }

    //este test se utiliza para agregar nuevos clientes. en este caso se agregan 3
    public void testIngresarCliente() throws Exception {
        clienteTest = new Cliente();
        clienteTest.nombre_cliente = "Juan";
        clienteTest.estado_cliente = true;
        clienteTest.AgregarCliente();
        assertEquals(true, clienteTest.id_cliente > 0);

        clienteTest = new Cliente();
        clienteTest.nombre_cliente = "Pedro";
        clienteTest.estado_cliente = true;
        clienteTest.AgregarCliente();
        assertEquals(true, clienteTest.id_cliente > 0);

        clienteTest = new Cliente();
        clienteTest.nombre_cliente = "Ramon";
        clienteTest.estado_cliente = true;
        clienteTest.AgregarCliente();
        assertEquals(true, clienteTest.id_cliente > 0);

    }

    //este test es para la modificacion de alguno de los clientes agregados, al cual se le cambia el nombre
    //y se pregunta, si segun la funcion original se logro modificar el cliente
    public void testModificarCliente() throws Exception {
        clienteTest = new Cliente();
        clienteTest.ObtenerCliente(1);
        clienteTest.nombre_cliente = "Juanito";
        clienteTest.estado_cliente = true;
        assertSame("Se modifico el cliente con exito", clienteTest.ModificarCliente());

    }

    //este test es para obtener un cliente, en este caso se buscara el cliente modificado en el test anterior
    //para comprobar si realmente ha sido cambiado se generan dos assert; explicamos acontinuación
    public void testObtenerCliente() throws Exception {
        clienteTest = new Cliente();
        //primer se debe entender que la funcion de obtener cliente tiene dos resultados.
        //el primero siendo si encuentra el id buscada, se devolvera a si mismo, ya que los valores son seteados dentro de la funcion
        //si no se encuentra el id, esta funcion devolvera vacio, y el objeto quedara isntanciado con valores predeterminados por java
        //este assert valida si la funcion devuelve un cliente o no
        assertNotNull(clienteTest.ObtenerCliente(1));
        //en caso de devolverlo, se preguntara si es el que se modifico en la prueba anterior (si se coordina el nombre)
        assertTrue(clienteTest.nombre_cliente.equals("Juanito"));

    }

    //como ultimo se genera este test para saber si estan los 3 clientes agregados en un principio
    public void testListarClientes() throws Exception {
        clienteTest = new Cliente();
        ArrayList<Cliente> lista = clienteTest.listaClientes();
        assertTrue(lista.size() == 3);
    }
}