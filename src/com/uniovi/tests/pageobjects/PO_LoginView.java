package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_LoginView {

	public static void fillForm(WebDriver driver, String userName, String password) {
		WebElement username = driver.findElement(By.name("email"));
		username.click();
		username.clear();
		username.sendKeys(userName);
		WebElement pass = driver.findElement(By.name("password"));
		pass.click();
		pass.clear();
		pass.sendKeys(password);

		By boton = By.id("btLogin");
		driver.findElement(boton).click();
	}

}
