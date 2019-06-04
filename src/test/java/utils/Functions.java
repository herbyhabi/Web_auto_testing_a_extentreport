package utils;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

public class Functions extends TestBase {

    String screenshotPath = "./screenshot";
    String imgFilePath = "./test-output/img/img.png";

    /**
     * Determine if the element exists
     * @param element enter the location of element
     * @return true or false
     * @author He, ying
     */

    public boolean doesElementDisplay(WebElement element){
        TestBase.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try{
            element.isDisplayed();
            return true;
        }catch (NoSuchElementException e){
            System.out.println("The element is not displayed!!");
            return false;
        }


    }

    public boolean isElementExist(WebDriver driver, String xpath) {
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            throw new NoSuchElementException();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            System.out.println("Element:" + xpath + " is not exist!");
            return false;
        }
    }



    /**
     * to capture the current screenshot
     * @param name
     * @param imgName customize the image name
     * @return return the image path
     */

    public void captureScreenshot(TakesScreenshot name, String imgName){
        File file = name.getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(file, new File(screenshotPath+"\\" +imgName +".png"));
        }catch (IOException e){
            System.out.println("cannot save the screenshot");
        }
    }


    public static void deleteOldScreenshot(){
        File file = new File(".\\test-output\\errorScreenshot");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date today = new Date();
        Date fileDate;
        String fileName;
        int distanceDays = 3;

        File[] fileList = file.listFiles();

        if(fileList.length>0){
            for(File f : fileList){
                fileName = (f.getName().split("/."))[0];

                try{
                    fileDate = dateFormat.parse(fileName);
                    if(calculateNumberDay(fileDate,today)>distanceDays){
                        f.delete();
                    }
                } catch (ParseException e) {
                    System.out.println("Delete Failed");
                }
            }
        }
    }

    /**
     * Compare two images, after compared, those files will be removed
     * @param image1Name
     * @param image2Name
     * @return
     */

    public boolean compareTwoImages(String image1Name, String image2Name){
        try{
            File f1 = new File(screenshotPath + "\\" + image1Name + ".png");
            File f2 = new File(screenshotPath + "\\" + image2Name + ".png");
            if(f1.exists() && f2.exists()){

                BufferedImage imgA = ImageIO.read(f1);
                BufferedImage imgB = ImageIO.read(f2);

                if(imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()){
                    for(int x=0; x< imgA.getWidth();x++){
                        for(int y =0; y<imgA.getHeight();y++){
                            if(imgA.getRGB(x, y)!= imgB.getRGB(x,y)){
                                return false;
                            }
                        }

                    }
                }else {
                    return false;
                }
            }else{
                System.out.println("The file not exists!!! Please check the file name and the path");
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("the two images is not accessed");
        }
        return true;
    }

    /**
     * Delete al of files in a directory
     * @param directoryPath
     */

    public void deleteAllfileInDirectory(String directoryPath){
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        for(File file: files){
            file.delete();

            if(!file.delete()){
                System.out.println("Failed to delete " + file);
            }
        }
    }


    public static int calculateNumberDay(Date d1, Date d2){
        return abs((int)(d2.getTime()-d1.getTime()/(1000*60*60*24)));
    }


    /**
     * clear the old extent report, always store 4 reports in the folder.
     */
    public static void clearOldExtentReport(){
        File file = new File(".\\test-output");
        File[] files = file.listFiles();

        if(files.length>3){

            try{

            for (int i = 1; i < files.length-3; i++) {
                files[i].delete();
                i++;
            }

            }catch (Exception e){
                System.out.println("Failed to delete extentreports");
            }
        }
    }


    /**
     * Get sub screenshot from a full page screenshot
     * @param element the target image's element
     * @throws IOException
     */


    public void getImage(WebElement element) throws IOException{
        //Get entire page screenshot
         File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
         BufferedImage fullImg = ImageIO.read(screenshot);

         //Get the location of element on the page
         Point point = element.getLocation();

         //Get width and height of the element
        int eleWidth = element.getSize().getWidth();
        int eleHeight = element.getSize().getHeight();

        //Crop the entire page screenshot to get only element screenshot
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(),point.getY(),eleWidth,eleHeight);
        ImageIO.write(eleScreenshot,"png",screenshot);


        //Copy the element screenshot
        File screenshotLocation = new File(imgFilePath);
        FileUtils.copyFile(screenshot,screenshotLocation);

        //将图片二值化
        BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(screenshotLocation));
        ImageIO.write(grayImage,"png", screenshotLocation);
    }


    /**
     * to obtain the content of image, just english and number
     * @return
     * @throws TesseractException
     */

    public String getImgContent() throws TesseractException {

        String code = "";

        ITesseract instance = new Tesseract();
        instance.setLanguage("eng");
        //File tessDataFolder = LoadLibs.extractNativeResources("tessdata");
        //instance.setDatapath(tessDataFolder.getAbsolutePath());

        File imgDir = new File(imgFilePath);
        code = instance.doOCR(imgDir);
//        System.out.println(code);

        //remove all whitespace and some specific characters from the string
        code = code.replaceAll(" ", "");

        String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        String aa = "";//这里是将特殊字符换为aa字符串,""代表直接去掉
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(code);//这里把想要替换的字符串传进来
        code= m.replaceAll(aa).trim();
        System.out.println(code);

        return code;
    }


    public List<String> getCurrentPageInfo(){
        List<String> list=new ArrayList<String>();
        list.add(driver.getCurrentUrl());
        list.add(driver.getTitle());
        return list;

    }



}
