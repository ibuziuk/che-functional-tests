/******************************************************************************* 
 * Copyright (c) 2018 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
*/
package redhat.che.functional.tests.fragments;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.google.common.base.Function;

public class TabsPanel {
	
	private static final Logger LOG = Logger.getLogger(TabsPanel.class);

	@Root
	private WebElement rootElement;

	@FindByJQuery("div[active] > div:last-child")
	private WebElement activeTabClose;

	@FindByJQuery("div[active]")
	private WebElement activeTab;
	
	@FindByJQuery("div[focused]")
	private WebElement focusedTab;

	@FindByJQuery("div[unsaved]")
	private WebElement unsavedTab;

	public void waitUntilFocusedTabHasName(String tabName) {
		waitModel().until().element(rootElement).is().visible();
		waitModel().withTimeout(10, TimeUnit.SECONDS)
				.until((Function<WebDriver, Boolean>) webDriver -> focusedTab.getText().equals(tabName));
	}

	public void waitUntilActiveTabHasName(String tabName) {
		waitModel().until().element(rootElement).is().visible();
		waitModel().withTimeout(10, TimeUnit.SECONDS)
				.until((Function<WebDriver, Boolean>) webDriver -> activeTab.getText().equals(tabName));
	}

	public void closeActiveTab(WebDriver driver) {
		activeTabClose.click();
		try {
			driver.findElement(By.id("ask-dialog-ok")).click();
		} catch (Exception e) {
			// if exception arise, changes were saved automatically
		}
	}
	
	public void closeAllTabs(WebDriver driver) {
		LOG.info("Closing all tabs");
		try {
			Graphene.waitGui().until().element(activeTab).is().clickable();
		}catch (NoSuchElementException ex) {
			LOG.info("No tabs opened.");
			return;
		}
		new Actions(driver).contextClick(activeTab).perform();;
		WebElement menu = driver.findElement(By.id("menu-lock-layer-id"));
		menu.findElement(By.id("gwt-debug-contextMenu/closeAllEditors")).click();
		Graphene.waitGui().until().element(menu).is().not().present();
	}

	public void waintUntilFocusedTabSaves() {
		waitGui().until().element(unsavedTab).is().not().visible();
	}
}
