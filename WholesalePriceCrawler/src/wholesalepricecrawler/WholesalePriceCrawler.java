/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wholesalepricecrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
            void main(String[] args) throws IOException
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
            //yearSelect.selectByValue("2015");
            yearSelect = new Select(driver.findElement(By.id(yearDropdownID)));
            yearOptions = yearSelect.getOptions();

            yearOptions.get(i).click();

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
                    cell.click();

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

                            // for all notchecked checkboxes
                            List<WebElement> mandis = driver.findElements(By.cssSelector(
                                    "input:not(:checked)[type='checkbox']"));

                            //WebElement mandi = mandis.get(m);
                            for (int chkCount = 0; chkCount < mandis.size(); chkCount++)
                            {
                                mandis.get(chkCount).click();
                                // System.out.println("Mandi Selected:" + mandis.get(chkCount).getAttribute("value"));
                            }
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

                            // Go back
                            driver.navigate().back();
                            //driver.manage().timeouts().implicitlyWait(1000, TimeUnit.DAYS);

                            // Reload the state list
                            stateSelect = new Select(driver.findElement(By.id(selectStateID)));
                            stateSelect.deselectAll();
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

            }

        }
    }

    private static
            void parsePage(WebDriver driver) throws IOException
    {

        int rowCount = 1;
        String state = "";
        String mandiName = "";
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
                arrivals = cells.get(1).getText();
                unitOfArrival = cells.get(2).getText();
                // origin not needed : skip 3
                variety = cells.get(4).getText();
                grade = cells.get(5).getText();
                minimumPrice = cells.get(6).getText();
                maximumPrice = cells.get(7).getText();
                modalPrice = cells.get(8).getText();
                unitOfPrice = cells.get(9).getText();

                System.out.print("State:" + state + " Mandi:" + mandiName + " Commodity:" + commodity + " Arrival:" + arrivals + " unitOfArrival" + unitOfArrival + " variety" + variety + " grade" + grade + " minimumPrice" + minimumPrice + " maximumPrice" + maximumPrice + " modalPrice" + modalPrice + " unitOfPrice" + unitOfPrice);

                try
                {
                    int statecode = getStateCode(state);
                    int mandicode = getMandiCode(statecode, mandiName);
                    InsertIntoWholeSaleTable(state, mandiName, commodity, arrivals, unitOfArrival, variety, grade, minimumPrice, maximumPrice, modalPrice, unitOfPrice);
                    System.out.println("Insertion Success");
                }
                catch (Exception e)
                {
                    System.out.println("Insertion UnSuccess " + e.getMessage() + e.getStackTrace());
                }
                rowCount++;

            }

        }

    }

    private static
            int getMandiCode(int state, String mandi) throws SQLException
    {
        Connection c = null;
        Statement stmt = null;
        int mandiCode = 0;
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Agriculture",
                            "postgres", "password");

            c.setAutoCommit(true);
            stmt = c.createStatement();
            String mandiInsert = "Insert into MANDIS(MandiName,StateCode) Values(?,?)";

            String stateSelect = "Select * from MANDIS WHERE StateCode=? AND MandiName ilike ?";
            PreparedStatement stateSelectPS = c.prepareStatement(stateSelect);
            stateSelectPS.setInt(1, state);
            stateSelectPS.setString(1, mandi);

            ResultSet rs = stateSelectPS.executeQuery();

            if (!rs.next())
            {
                PreparedStatement ps = c.prepareStatement(mandiInsert);
                ps.setString(1, mandi);
                ps.setInt(2, state);

                ps.executeUpdate();
                ps.close();

                PreparedStatement stateSelPS = c.prepareStatement(stateSelect);
                stateSelectPS.setString(1, mandi);
                stateSelectPS.setInt(2, state);

                ResultSet rs1 = stateSelectPS.executeQuery();
                if (rs.next())
                {
                    mandiCode = rs1.getInt("MandiCode");
                }
                stateSelPS.close();
                rs1.close();
                stateSelPS.close();
            }
            rs.close();
            stateSelectPS.close();

        }
        catch (Exception e)
        {

        }
        finally
        {
            c.close();
        }
        return mandiCode;
    }

    private static
            int getStateCode(String state) throws SQLException
    {
        Connection c = null;
        Statement stmt = null;
        int stateCode = 0;
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Agriculture",
                            "postgres", "password");

            c.setAutoCommit(true);
            stmt = c.createStatement();
            String stateInsert = "Insert into STATES(State) Values(?)";

            String stateSelect = "Select * from STATES WHERE State ilike ?";
            PreparedStatement stateSelectPS = c.prepareStatement(stateSelect);
            stateSelectPS.setString(1, state);

            ResultSet rs = stateSelectPS.executeQuery();

            if (!rs.next())
            {
                PreparedStatement ps = c.prepareStatement(stateInsert);
                ps.setString(1, state);

                ps.executeUpdate();
                ps.close();

                PreparedStatement stateSelPS = c.prepareStatement(stateSelect);
                stateSelectPS.setString(1, state);

                ResultSet rs1 = stateSelectPS.executeQuery();
                if (rs.next())
                {
                    stateCode = rs1.getInt("StateCode");
                }
                stateSelPS.close();
                rs1.close();
            }

        }
        catch (Exception e)
        {

        }
        finally
        {
            c.close();
        }
        return stateCode;
    }

    private static
            void InsertIntoWholeSaleTable(String state, String mandiName, String commodity, String arrivals, String unitOfArrival, String variety, String grade, String minimumPrice, String maximumPrice, String modalPrice, String unitOfPrice) throws IOException
    {
        Connection c = null;
        Statement stmt = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Agriculture",
                            "postgres", "password");

            c.setAutoCommit(true);
            stmt = c.createStatement();

            String prepQuery = "Insert into WholeSaleData Values(?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = c.prepareStatement(prepQuery);
            ps.setString(1, state);
            ps.setString(2, mandiName);
            ps.setString(3, commodity);

            ps.setString(5, unitOfArrival);
            ps.setString(6, variety);
            ps.setString(7, grade);

            ps.setString(11, unitOfPrice);

            double arrival = 0;
            try
            {
                arrival = Double.parseDouble(arrivals);
                ps.setDouble(4, arrival);
            }
            catch (Exception e)
            {
                ps.setNull(4, java.sql.Types.DOUBLE);
            }

            double minimumprice = 0;
            try
            {
                minimumprice = Double.parseDouble(minimumPrice);
                ps.setDouble(8, minimumprice);
            }
            catch (Exception e)
            {
                ps.setNull(8, java.sql.Types.DOUBLE);
            }

            double maximumprice = 0;
            try
            {
                maximumprice = Double.parseDouble(maximumPrice);
                ps.setDouble(9, maximumprice);
            }
            catch (Exception e)
            {
                ps.setNull(9, java.sql.Types.DOUBLE);
            }

            double modalprice = 0;
            try
            {
                modalprice = Double.parseDouble(modalPrice);
                ps.setDouble(10, modalprice);
            }
            catch (Exception e)
            {
                ps.setNull(10, java.sql.Types.DOUBLE);
            }

            ps.executeUpdate();
            ps.close();
            stmt.close();
            c.close();
            System.out.println("Data saved into database");
        }
        catch (Exception e)
        {
            File file = new File("/home/reshma/Desktop/WholeSaleAgriCultureErrorLogs.txt");

            //if file doesnt exists, then create it
            if (!file.exists())
            {
                file.createNewFile();
            }

            //true = append file
            FileWriter fileWritter = new FileWriter(file.getName(), true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(e.getMessage() + "\t" + e.getStackTrace() + "\n");
            bufferWritter.close();
        }
    }

}
