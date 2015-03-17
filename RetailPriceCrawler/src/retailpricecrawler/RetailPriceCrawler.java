/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retailpricecrawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author kapil
 */
public
        class RetailPriceCrawler
{

    /**
     * @param args the command line arguments
     */
    public static
            void main(String[] args)
    {

        WebDriver driver = new FirefoxDriver();

        driver.get("http://fcainfoweb.nic.in/PMSver2/Reports/Report_Menu_web.aspx");

        for (int i = 1; i < 20; i++)
        {

            WebElement option = driver.findElement(By.id("MainContent_Rbl_Rpt_type_0"));

            option.click();

            WebDriverWait wait = new WebDriverWait(driver, 3000);

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("MainContent_Ddl_Rpt_Option0")));

            org.openqa.selenium.support.ui.Select locator = new org.openqa.selenium.support.ui.Select(driver.findElement(By.id("MainContent_Ddl_Rpt_Option0")));

            locator.getOptions().get(1).click();

            WebElement textElement = driver.findElement(By.id("MainContent_Txt_FrmDate"));
            textElement.sendKeys(i + "/02/2015");

            driver.findElement(By.id("MainContent_btn_getdata1")).click();

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("btn_back")));

            driver.findElement(By.id("btn_back")).click();

            System.out.println("Run Complete " + i);

        }

    }

}
