/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retailpricecrawler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

        int noOfYears = 15;
        int latestMonth = 3;
        int latestDate = 15;
        int latestYear = 2015;

        WebDriver driver = new FirefoxDriver();

        driver.get("http://fcainfoweb.nic.in/PMSver2/Reports/Report_Menu_web.aspx");

        /*  List<String> years = populateYears(noOfYears);
         List<String> months = populateMonths();
         List<String> dates = populateDates();
         String date = "";
         for (int j = 0; j < years.size(); j++)
         {
         for (int k = 0; k < months.size(); k++)
         {
         if (years.get(j).equals(Integer.toString(latestYear)) && Integer.parseInt(months.get(k)) > latestMonth)
         {
         continue;
         }
         for (int l = 0; l < dates.size(); l++)
         {
         if (years.get(j).equals(Integer.toString(latestYear)) && months.get(k).equals(Integer.toString(latestMonth)) && Integer.parseInt(dates.get(l)) > latestDate)
         {
         continue;
         }
         if (validateDate(dates.get(l), months.get(k), years.get(j)))
         {
         date = generateDate(dates.get(l), months.get(k), years.get(j));
         }
         }
         }
         }*/
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        String dateTofetch = dateFormat.format(cal.getTime());

        for (int i = 1; i < 20; i++)
        {

            WebElement option = driver.findElement(By.id("MainContent_Rbl_Rpt_type_0"));

            option.click();

            WebDriverWait wait = new WebDriverWait(driver, 3000);

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("MainContent_Ddl_Rpt_Option0")));

            Select locator = new Select(driver.findElement(By.id("MainContent_Ddl_Rpt_Option0")));

            locator.getOptions().get(1).click();

            WebElement textElement = driver.findElement(By.id("MainContent_Txt_FrmDate"));

            // Here We are sending Date ... We need to generate the all dates, but how?
            textElement.sendKeys(dateTofetch);

            driver.findElement(By.id("MainContent_btn_getdata1")).click();

            /**
             * ****************************************************************
             * PARSE DATA HERE *
             * ****************************************************************
             */
            if (!driver.findElement(By.tagName("body")).getText().contains("Sorry, Data does not exist for this date"))

            {
                parseData(driver);
            }
            /**
             * *****************************************************************
             * PARSING DONE *
             * ****************************************************************
             */

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("btn_back")));

            driver.findElement(By.id("btn_back")).click();

            cal.add(Calendar.DATE, -1);
            dateTofetch = dateFormat.format(cal.getTime());

            System.out.println("Run Complete " + i);

        }

    }

    private static
            void parseData(WebDriver driver)
    {
        // Get All The tables from the page
        List<WebElement> allTables = driver.findElements(By.tagName("table"));

        // We need to parse tables which all are required
        // Consider all tables one by one
        String date = "";
        String unitInKG = "";
        String unitInLt = "";

        for (int i = 0; i < allTables.size(); i++)
        {
            /* Now Tables have format like this
             1. Table Stating Heading caption, date, price unit, etc
             2. Actual Data Of reatil prices
             3. Some information regarding notes and sources
             4. Useless Table
             So Looking at this order we will parse table
             */

            // For debug purpose
            System.out.println("Table :" + (i) + "\n\n" + allTables.get(i).getText());
            System.out.println("\n\n");

            if (i % 4 == 0)
            {
                // Table Type 1
                // Get Date and Unit of Price 
                // Consists Only One Row
                // consists of 3 elements
                // Date - Daily Retail Prices Of Essential Commodities - Unit

                // Get the row
                WebElement row = allTables.get(i).findElement(By.tagName("tr"));

                // Get 3 Cells (Columns)
                List<WebElement> cells = row.findElements(By.tagName("td"));

                // 1st Element has of format : Date<space><space><Actual Date>
                String temp = cells.get(0).getText();
                String elements[] = temp.split("  ");
                date = elements[1];

                // 3rd element consists of price
                // and has format Unit:&nbsp;(Rs./Kg.)
                temp = cells.get(2).getText();
                elements = temp.split(" ");
                unitInKG = elements[1];
            }
            else if (i % 4 == 2)
            {
                // Table type 3
                // Parsing this before to get unit of Milk

                // Consists Of 3 rows
                // Only 1st is important
                // get rows
                List<WebElement> rows = allTables.get(i).findElements(By.tagName("tr"));

                // first row has 2 cells, out of which 2nd is required
                // get the cells of the first row
                List<WebElement> cells = rows.get(0).findElements(By.tagName("td"));

                // 2nd cells consists text as
                // <font size=2 color=Black><b>NR</b> -> Not Reported &nbsp;&nbsp;&nbsp;&nbsp; <b>@</b> -> (Rs./Lt.)</font> &nbsp;&nbsp;&nbsp;&nbsp; <b>*</b> -> (Packed)
                // What to do now ???
            }
            else if (i % 4 == 1)
            {
                // Table type 2                
                // Actual data                
                // Now this tables has some rows
                // 1st row is always with the list of commodities, in that also
                // 1st cell is always "center"

                // with the rest of the rows, if it has only one element, then it is stating zone
                // else if it has more than one element then first element is center i.e. place
                // and rest is the retail price
                // So lets do it
                // get all rows
                List<WebElement> rows = allTables.get(i).findElements(By.tagName("tr"));

                // process each row
                List<WebElement> commodities = null;
                for (int j = 0; j < rows.size(); j++)
                {
                    if (j == 0)
                    {
                        // its 1st row
                        // all commodities
                        // get all cells/columns/elements
                        commodities = rows.get(j).findElements(By.tagName("td"));
                        // So these cells has all the commodity list 
                        // EXCEPT first cell, which has word "center"
                        continue;
                    }

                    // Other than first row
                    // get cells
                    List<WebElement> cells = rows.get(j).findElements(By.tagName("td"));

                    if (cells.size() >= 1)
                    {
                        String firstCellData = cells.get(0).getText();
                        if (firstCellData.equals("Maximum Price") || firstCellData.equals("Minimum Price") || firstCellData.equals("Modal Price"))
                        {
                            continue; // We can also write break over here
                        }
                    }
                    if (cells.size() == 1)
                    {
                        // Consists ZONE
                        // NOT Required
                    }
                    else
                    {
                        // Consists data
                        // 1st element is Center
                        // Rest Prices Of Commodities

                        // Process This
                        for (int k = 1; k < cells.size(); k++)
                        {
                            System.out.print("Date:" + date + " ");

                            if (!commodities.isEmpty())
                            {
                                String commoTemp = commodities.get(k).getText();
                                if (commoTemp.contains("@"))
                                {
                                    commoTemp = commoTemp.replace("@", "");
                                    System.out.print("Comodity:" + commoTemp + " ");
                                    System.out.print("Unit: " + unitInLt + " ");
                                }
                                else
                                {
                                    if (commoTemp.contains("*"))
                                    {
                                        commoTemp = commoTemp.replace("*", "");
                                    }
                                    System.out.print("Comodity:" + commoTemp + " ");
                                    System.out.print("Unit: " + unitInKG + " ");
                                }
                            }
                            else
                            {
                                System.out.println("Error! Commodity List not initialised");
                            }

                            System.out.print("Center:" + cells.get(0).getText() + " ");
                            System.out.print("Price:" + cells.get(k).getText());
                            System.out.println("");
                        }

                    }
                }
            }

            else if (i % 4 == 3)
            {
                // Table type 4
                // We do not need to parse this table
            }
        }

    }

    private static
            List<String> populateYears(int noOfYears)
    {
        List<String> years = new ArrayList();

        for (int i = 0; i < noOfYears; i++)
        {
            years.add(Integer.toString(2015 - i));
        }

        return years;
    }

    private static
            List<String> populateMonths()
    {
        List<String> months = new ArrayList();

        for (int i = 0; i < 12; i++)
        {
            months.add(Integer.toString(1 + i));
        }

        return months;
    }

    private static
            List<String> populateDates()
    {
        List<String> dates = new ArrayList();

        for (int i = 0; i < 31; i++)
        {
            dates.add(Integer.toString(1 + i));
        }

        return dates;
    }

    private static
            String generateDate(String date, String month, String year)
    {
        return date + "/" + month + "/" + year;
    }

    private static
            boolean validateDate(String dateS, String monthS, String yearS)
    {

        int date = Integer.parseInt(dateS);
        int month = Integer.parseInt(monthS);
        int year = Integer.parseInt(yearS);

        switch (month)

        {
            case 2:  // February
                if (year % 4 == 0)
                {
                    if (date > 29)
                    {
                        return false;
                    }
                }
                else
                {
                    if (date > 28)
                    {
                        return false;
                    }
                }
            case 4: // April
            case 6: // June
            case 9: // September
            case 11: // November
                if (date > 30)
                {
                    return false;
                }
        }

        return true;
    }

}
