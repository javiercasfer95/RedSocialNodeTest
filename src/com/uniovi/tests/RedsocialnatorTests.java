package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

//import com.uniovi.services.DatosEjemplo;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_PrivateView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.utils.SeleniumUtils;

//Ordenamos las pruebas por el nombre del método 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RedsocialnatorTests {

	// En Windows (Debe ser la versión 46.0 y desactivar las actualizacioens
	// automáticas)):
	// static String PathFirefox = "C:\\Path\\FirefoxPortable.exe";

	// Path Joni
	// static String PathFirefox =
	// "P:\\SDI\\P5\\Firefox46.0.win\\Firefox46.win\\FirefoxPortable.exe";

	// Path Javi MSI
	static String PathFirefox = "D:\\Programs\\Firefox46.win\\FirefoxPortable.exe";

	// Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox);

	static String URL = "http://localhost:8081";

	public static WebDriver getDriver(String PathFirefox) {
		// Firefox (Versión 46.0) sin geckodriver para Selenium 2.x.
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// @BeforeClass
	public static void resetDatabaseToDefault() {
		driver.navigate().to(URL);
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "id", "login");
		// Rellenamos el formulario con admin
		PO_LoginView.fillForm(driver, "admin@correo.es", "123456");
		PO_NavView.clickOptionConCriterio(driver, "userAdminMenu", "id", "userAdminMenu");
		PO_NavView.clickOption(driver, "id", "restoreDBDefault");
		SeleniumUtils.EsperaCargaPagina(driver, "id", "logout", 2);
		PO_NavView.clickLogout(driver);

	}

	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {

	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() { // Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	/*
	 * CASOS DE PRUEBA
	 */
	// 1.1 [RegVal] Registro de Usuario con datos válidos. FUNCIONA
	// @Test
	public void T01_1_RegVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		PO_HomeView.clickOption(driver, "id", "singup");
		PO_RegisterView.fillForm(driver, "JaviTest@email.com", "JaviTest", "Castro", "123456", "123456");
		PO_View.checkElement(driver, "id", "tituloLogin");

		// PO_View.checkElement(driver, "id", "bienvenidaUser");
		// SeleniumUtils.esperarSegundos(driver, 2);
		// PO_View.checkElement(driver, "id", "bienvenidaUser");
		// PO_HomeView.clickOption(driver, "id", "logout");
		// PO_View.checkElement(driver, "id", "tituloLogin");
	}

	// 1.2 [RegInval] Registro de Usuario con datos inv�lidos (repetici�n de
	// contrase�a invalida). FUNCIONA
	// @Test
	public void T01_2_RegInval() {

		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		PO_HomeView.clickOption(driver, "id", "singup");
		PO_RegisterView.fillForm(driver, "JaviTest@email.com", "JaviTest", "Castro", "123456", "123456234");
		PO_View.checkElement(driver, "id", "bandaAlerta");

		// PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		// PO_HomeView.clickOption(driver, "signup", "id", "signup");
		// PO_RegisterView.fillForm(driver, "preuba@emailmal", "Prueba", "Fernandez",
		// "123456", "123456");
		// SeleniumUtils.esperarSegundos(driver, 2);
		// // Se ha metido el email mal, deberia estar ese campo de error
		// PO_View.checkElement(driver, "id", "emailMal");

	}

	// 2.1 [InVal] Inicio de sesi�n con datos v�lidos.
	// home. FUNCIONA
	// @Test
	public void T02_1_InVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "id", "login");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "javier@correo.es", "123456");
		SeleniumUtils.esperarSegundos(driver, 2);
		PO_View.checkElement(driver, "id", "bienvenidaUser");
		// Comprueba que vea la lista de usuarios
		PO_HomeView.clickOption(driver, "id", "logout");
		PO_View.checkElement(driver, "id", "bienvenidaHome");

	}

	// 2.2 [InInVal] Inicio de sesión con datos inválidos (usuario no existente en
	// la aplicación). FUNCIONA
	// Español
	// @Test
	public void T02_2_InInVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "id", "login");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "josefaNoExiste@correo.es", "123456");
		SeleniumUtils.esperarSegundos(driver, 2);
		PO_View.checkElement(driver, "id", "bandaAlerta");

	}

	// 3.1 [LisUsrVal] Acceso al listado de usuarios desde un usuario en sesion.
	// FUNCIONA
	// @Test
	public void T03_1_LisUsrVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		// Vamos al formulario de logueo.
		iniciarSesion("joni@correo.es", "123456");
		// LOGIN VALIDO
		// Una vez dentro clickamos en Usuarios y que nos lleve a la lista de usuarios
		// del
		// sistema.
		PO_HomeView.clickOption(driver, "id", "userListPage");

		// Se comprueba que esta el texto de la pagina que dice que esos son los
		// usuarios que hay en el sistema.
		PO_View.checkElement(driver, "id", "parrafoUsuariosSistema");

	}

	// 3.2 [LisUsrInVal] Intento de acceso con URL desde un usuario no identificado
	// al listado de usuarios
	// desde un usuario en sesi�n. Debe producirse un acceso no permitido a vistas
	// privadas. FUNCIONA
	// @Test
	public void T03_2_LisUsrInVal() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
		driver.navigate().to("http://localhost:8081/listUsers");
		// Nos deberia redirigir a la pagina del login
		PO_View.checkElement(driver, "id", "tituloLogin");
	}

	// 4.1 [BusUsrVal] Realizar una busqueda valida en el listado de usuarios desde
	// un usuario en sesion. FUNCIONA
	// @Test
	public void T04_1_BusUsrVal() {
		// Login valido
		iniciarSesion("javier@correo.es", "123456");
		PO_HomeView.clickOption(driver, "id", "userListPage");
		SeleniumUtils.esperarSegundos(driver, 1);
		PO_PrivateView.searchUser(driver, "Zelda");
		PO_View.checkElement(driver, "id", "filaDeZeldaValles@uniovi.es");

	}

	// 4.2 [BusUsrInVal] Intento de acceso con URL a la busqueda de usuarios desde
	// un usuario no identificado. Debe producirse un acceso no permitido a vistas
	// privadas. FUNCIONA
	// @Test
	public void T04_2_BusUsrInVal() {
		driver.navigate().to("http://localhost:8081/listUsers?busqueda=Zelda");
		// Nos deberia redirigir a la pagina del login
		PO_View.checkElement(driver, "id", "tituloLogin");
	}

	// 5.1 [InvVal] Enviar una invitacion de amistad a un usuario de forma valida.
	// FUNCIONA
	// @Test
	public void T05_1_InvVal() {
		// Login valido
		iniciarSesion("javier@correo.es", "123456");
		PO_HomeView.clickOption(driver, "id", "userListPage");
		PO_PrivateView.clickOption(driver, "id", "tnJuanFrancisco@live.com");
		// assertTrue(PO_View.checkElementExists(driver, "id",
		// "btnJuanFrancisco@live.com") == false);
		PO_NavView.accederPeticionesEnviadas(driver);
		// btnAshRodriguez@default.es
		PO_View.checkElement(driver, "id", "filaDeJuanFrancisco@live.com");
	}

	/*
	 * 5.2 [InvInVal] Enviar una invitacion de amistad a un usuario al que ya le
	 * habiamos invitado la invitacion previamente. No debera dejarnos enviar la
	 * invitacion, se podra ocultar el boton de enviar invitacion o notificar que ya
	 * habia sido enviada previamente.
	 * 
	 * TAMBIEN COMPRUEBA EL ACCESO POR URL
	 * 
	 * FUNCIONA
	 */
	//@Test
	public void T05_2_InvInVal() {

		// Login valido
		iniciarSesion("javier@correo.es", "123456");
		PO_NavView.accederPeticionesEnviadas(driver);
		//Ya le hemos enviado la peticion a juan?
		assertTrue(PO_View.checkElementExists(driver, "id", "filaDeJuanFrancisco@live.com") == true);

		//Ahora vamos a intentar enviar otra. Tiene que saltar la alerta
		PO_HomeView.clickOption(driver, "id", "userListPage");
		PO_PrivateView.clickOption(driver, "id", "tnJuanFrancisco@live.com");
		
		//Tenemos que comprobar que seguimos en la pagina de lista de usuarios y ha salido la franja
		PO_View.checkElement(driver, "id", "parrafoUsuariosSistema");
		assertTrue(PO_View.checkElementExists(driver, "id", "bandaAlerta") == true);
		
		//Hacemos lo mismo pero por URL
		driver.navigate().to("http://localhost:8081/peticion/enviar/JuanFrancisco@live.com");
		PO_View.checkElement(driver, "id", "parrafoUsuariosSistema");
		assertTrue(PO_View.checkElementExists(driver, "id", "bandaAlerta") == true);

	}
	
	 // 6.1 [LisInvVal] Listar las invitaciones recibidas por un usuario, realizar la
	 // comprobacion con una lista que al menos tenga una invitacion recibida. FUNCIONAAA
	 //@Test
	 public void T06_1_LisInvVal() {
	 iniciarSesion("JuanFrancisco@live.com", "123456");
//	 // Vamos a la pagina de usuarios por si acaso
//	 PO_PrivateView.searchUser(driver, "joni");
//	 PO_PrivateView.clickOption(driver, "id", "btnjoni@correo.es");
//	 PO_NavView.clickLogout(driver);
//	 iniciarSesion("joni@correo.es", "123456");
	 
	 PO_NavView.accederPeticionesRecibidas(driver);
	 PO_View.checkElement(driver, "id", "filaDejavier@correo.es");
	 }
	
	// //
	// // 7.1 [AcepInvVal] Aceptar una invitacion recibida
	// @Test
	// public void T07_1_AcepInvVal() {
	// iniciarSesion("joni@correo.es", "123456");
	// PO_NavView.accederPeticionesRecibidas(driver);
	// PO_PrivateView.clickOption(driver, "id", "btnjavier@correo.es");
	// assertTrue(PO_View.checkElementExists(driver, "id", "filaDejavier@correo.es")
	// == false);
	// }
	//
	// // 8.1 [ListAmiVal] Listar los amigos de un usuario, realizar la comprobacion
	// // con una lista que al menos tenga un amigo.
	// @Test
	// public void T08_1_ListAmiVal() {
	// iniciarSesion("javier@correo.es", "123456");
	// PO_NavView.accederAmigos(driver);
	// PO_View.checkElement(driver, "id", "filaDejoni@correo.es");
	// }
	//
	// /*
	// * 14.1 [AdLisUsrVal] Desde un usuario identificado en sesión como
	// administrador
	// * listar a todos los usuarios de la aplicación.
	// */
	// @Test
	// public void T14_1_AdLisUsrVal() {
	// iniciarSesion("admin@correo.es", "123456");
	// PO_NavView.clickOptionConCriterio(driver, "userAdminMenu", "id",
	// "userAdminMenu");
	// PO_NavView.clickOption(driver, "id", "adminListUsers");
	// PO_View.checkElement(driver, "id", "tituloPagAdmin");
	// }
	//
	// /*
	// * 15.1 [AdBorUsrVal] Desde un usuario identificado en sesión como
	// administrador
	// * eliminar un usuario existente en la aplicación.
	// */
	// @Test
	// public void T15_1_AdBorUsrVal() {
	// iniciarSesion("admin@correo.es", "123456");
	// PO_NavView.clickOptionConCriterio(driver, "userAdminMenu", "id",
	// "userAdminMenu");
	// PO_NavView.clickOption(driver, "id", "adminListUsers");
	// PO_View.checkElement(driver, "id", "tituloPagAdmin");
	// PO_PrivateView.clickOption(driver, "id", "borrarjavier@correo.es");
	// PO_PrivateView.searchUser(driver, "javi");
	// assertTrue(PO_View.checkElementExists(driver, "id", "filaDejavier@correo.es")
	// == false);
	// }
	//
	// /*
	// * 15.2 [AdBorUsrInVal] Intento de acceso vía URL al borrado de un usuario
	// * existente en la aplicación. Debe utilizarse un usuario identificado en
	// sesión
	// * pero que no tenga perfil de administrador.
	// */
	//
	// @Test
	// public void T15_2_AdBorUsrInVal() {
	// iniciarSesion("joni@correo.es", "123456");
	// driver.navigate().to("http://localhost:8090/debug/deleteUser?userBorraEmail=AshRodriguez@default.es");
	// PO_View.checkElement(driver, "id", "tituloAccesoDenegado");
	// }

	private void iniciarSesion(String email, String pass) {
		// Vamos al formulario de logueo.
		PO_NavView.clickOption(driver, "id", "login");
		SeleniumUtils.esperarSegundos(driver, 2);
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, email, pass);
		// // Comprueba que vea la lista de usuarios
		SeleniumUtils.esperarSegundos(driver, 1);

	}
}
