/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wholesalepricecrawler;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author kapil
 */
public
        class WholesalePriceCrawler
{

    /**
     * @param args the command line arguments
     */
    public static
            void main(String[] args)
    {
        // URL to be fetched
        String url = "http://agmarknet.nic.in/agnew/NationalBEnglish/MarketWiseDailyReport.aspx?ss=2";

        //Parameters of the form :: as per observation name and ids are kept same
        String form_name = "form1";
        String yearDropdownID = "drpDwnYear";
        String monthDropdownID = "drpDwnMonth";
        String calenderID = "Calendar1";
        String selectStateID = "ListBox1";
        String submitButton = "Submit_list";
        //String mandiCheckBox = "GridView1_ctl03_RowLevelCheckBox";

        // Fetch the page
        WebDriver driver = new FirefoxDriver();
        driver.get(url);

        // Get Dropdon lists
        Select yearSelect = new Select(driver.findElement(By.id(yearDropdownID)));

        // Related parameters
        List<WebElement> yearOptions = yearSelect.getOptions();

        //Select Year
        for (int i = 0; i < yearOptions.size(); i++)
        {

            /**
             * * For Testing **
             */
            System.out.println("Year Selected::");

            //System.out.println("Year Selected:" + yearOptions.get(i).getText());
            //yearSelect.getOptions().get(i).click();
            yearSelect.selectByValue("2015");
            yearSelect = new Select(driver.findElement(By.id(yearDropdownID)));
            yearOptions = yearSelect.getOptions();

            Select monthSelect = new Select(driver.findElement(By.id(monthDropdownID)));
            List<WebElement> monthOptions = monthSelect.getOptions();
            //Select Month
            for (int j = 0; j < monthOptions.size(); j++)
            {
                System.out.println("Month Selected:" + monthOptions.get(i).getText());
                monthSelect.getOptions().get(j).click();
                monthSelect = new Select(driver.findElement(By.id(monthDropdownID)));
                monthOptions = monthSelect.getOptions();
                // get the table for selecting date
                WebElement formElement = driver.findElement(By.id(form_name));

                // get table from this form
                WebElement tableElement = formElement.findElement(By.id(calenderID));

                // get all the elements of the table 
                List<WebElement> allCells = tableElement.findElements(By.tagName("td"));

                // check whether they have hyper link attached with them
                for (int k = 0; k < allCells.size(); k++)
                {
                    WebElement cell = allCells.get(k);
                    System.out.println("Cell :: " + cell.getText());
                    if (cell.findElements(By.tagName("a")).size() > 0)
                    {
                        WebElement hyperlink = cell.findElement(By.tagName("a"));
                        hyperlink.click();

                        System.out.println("----- HYPERLINK CLICKED ------");

                        // Select State
                        Select stateSelect = new Select(driver.findElement(By.id(selectStateID)));
                        List<WebElement> allStates = stateSelect.getOptions();

                        for (int l = 0; l < allStates.size(); l++)
                        {
                            WebElement state = allStates.get(l);
                            System.out.println("State selected:" + state.getText());
                            state.click();

                            // get submit Button and click on them
                            WebElement submitElement = driver.findElement(By.id(submitButton));
                            submitElement.click();

                            // get all mandis
                            // for all checked checkboxes
                            if (driver.findElements(By.cssSelector(
                                    "input:checked[type='checkbox']")).size() > 0)
                            {
                                WebElement selectedMandi = driver.findElement(By.cssSelector(
                                        "input:checked[type='checkbox']"));
                                // Uncheck it
                                selectedMandi.click();
                            }

                            // for all notchecked checkboxes
                            List<WebElement> mandis = driver.findElements(By.cssSelector(
                                    "input:not(:checked)[type='checkbox']"));

                            // Select All Mandis
                            for (int m = 0; m < mandis.size(); m++)
                            {
                                WebElement mandi = mandis.get(m);
                                System.out.println("Mandi Selected:" + mandi.getAttribute("value"));
                                mandi.click();
                                String submitMandi = "btnSubmit";
                                WebElement submitMandiButton = driver.findElement(By.id(submitMandi));
                                submitMandiButton.click();

                                System.out.println("DATA FOR MANDI... GOING BACK");

                                // Web page to be parsed
                                //
                                // ::::::::::::::::::::::::::::::::::::::::::::::::::
                                // ::::::::::::::::::: TODO :::::::::::::::::::::::::
                                parsePage(driver);
                                // ::::::::::::::::::::::::::::::::::::::::::::::::::
                                //
                                // Click on back 
                                driver.navigate().back();

                                // Reload the mandi list
                                // get all mandis
                                // for all checked checkboxes
                                if (driver.findElements(By.cssSelector(
                                        "input:checked[type='checkbox']")).size() > 0)
                                {
                                    WebElement selectedMandi = driver.findElement(By.cssSelector(
                                            "input:checked[type='checkbox']"));
                                    // Uncheck it
                                    selectedMandi.click();
                                }

                                // for all notchecked checkboxes
                                mandis = driver.findElements(By.cssSelector(
                                        "input:not(:checked)[type='checkbox']"));

                            }

                            // Go back 
                            driver.navigate().back();

                            // Reload the state list
                            stateSelect = new Select(driver.findElement(By.id(selectStateID)));
                            allStates = stateSelect.getOptions();
                        }

                    }

                    // Reload the all elements of page
                    yearSelect = new Select(driver.findElement(By.id(yearDropdownID)));
                    yearOptions = yearSelect.getOptions();

                    monthSelect = new Select(driver.findElement(By.id(monthDropdownID)));
                    monthOptions = monthSelect.getOptions();

                    // Reload the Table
                    formElement = driver.findElement(By.id(form_name));

                    // get table from this form
                    tableElement = formElement.findElement(By.id(calenderID));

                    // get all the elements of the table 
                    allCells = tableElement.findElements(By.tagName("td"));
                }
                break;
            }
            break;
        }
    }

    private static
            void parsePage(WebDriver driver)
    {

        int rowCount = 1;
        String state="";
        String mandiName="";
        String commodity;
        String arrivals;
        String unitOfArrival;
        // we dont want origin
        String variety;
        String grade;
        String minimumPrice;
        String maximumPrice;
        String modalPrice;
        String unitOfPrice;

        // get table 
        WebElement table = driver.findElement(By.id("GridView1")); // This table has data

        // get all rows
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        // parse each row
        // 1st row is table heading
        // 2nd row is State
        // from 3rd row mandi starts
        for (WebElement row : rows)
        {
            // Skip the first row consists all headings
            if (rowCount == 1)
            {
                rowCount++;
                continue;
            }
            /*  We can also write this
             if(row.findElements(By.tagName("th")).size()>0) 
             continue;
             */

            /* Row 2 contains state */
            if (rowCount == 2)
            {
                WebElement cellData = row.findElement(By.tagName("td"));
                state = cellData.getText();
                rowCount++;
                continue;
            }

            /* Now Mandis Will start */
            if (row.findElements(By.tagName("td")).size() == 1)
            {
                // Contains Mandi
                WebElement mandiNameWE = row.findElement(By.tagName("td"));
                mandiName = mandiNameWE.getText();
                rowCount++;
                continue;
            }

            /* from here mandi data will start 
             mandi name we should have already 
             so this will have data for above mandi
             */
            if (row.findElements(By.tagName("td")).size() > 1)
            {
                // Contains Mandi
                List<WebElement> cells = row.findElements(By.tagName("td"));

                // Cell has format 
                //Commodity(Market Center)-Arrivals-Unit of Arrivals-Origin-Variety-Grade-Minimum Price-Maximum Price-Modal Price-Unit of Price
                commodity = cells.get(0).getText();
                arrivals =cells.get(1).getText();
                unitOfArrival =cells.get(2).getText();
                // origin not needed : skip 3
                variety = cells.get(4).getText();
                grade =cells.get(5).getText();
                minimumPrice =cells.get(6).getText();
                maximumPrice =cells.get(7).getText();
                modalPrice =cells.get(8).getText();
                unitOfPrice =cells.get(9).getText();
                
                
                System.out.print("State:"+state + " Mandi:"+mandiName+" Commodity:"+commodity+" Arrival:"+arrivals+" unitOfArrival"+unitOfArrival+" variety"+variety+" grade"+grade+" minimumPrice"+minimumPrice+" maximumPrice"+maximumPrice+" modalPrice"+modalPrice+" unitOfPrice"+unitOfPrice);
                System.out.println("");
                rowCount++;
                
            }

        }

    }

}
