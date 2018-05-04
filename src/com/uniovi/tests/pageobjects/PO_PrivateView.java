package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.utils.SeleniumUtils;

public class PO_PrivateView extends PO_NavView {

	public static void searchUser(WebDriver driver, String userName) {
		WebElement search = driver.findElement(By.id("search"));
		search.click();
		search.clear();
		search.sendKeys(userName);

		// Pulsar el boton de buscar
		By boton = By.id("searchButton");
		driver.findElement(boton).click();
		SeleniumUtils.esperarSegundos(driver, 2);
	}

}
