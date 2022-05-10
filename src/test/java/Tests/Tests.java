/*
 * Copyright 2014-2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package Tests;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.OutputType;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.UUID;
import java.util.Objects;
import java.time.Duration;
import java.io.IOException;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class Tests {
    /**
     * Make the driver static. This allows it to be created only once
     * and used across all of the test classes.
     */
    public static AndroidDriver<MobileElement> driver;

    private boolean takeScreenshots = false;

    /**
     * This method runs before any other method.
     *
     * Appium follows a client - server model:
     * We are setting up our appium client in order to connect to Device Farm's appium server.
     *
     * We do not need to and SHOULD NOT set our own DesiredCapabilities
     * Device Farm creates custom settings at the server level. Setting your own DesiredCapabilities
     * will result in unexpected results and failures.
     *
     * @throws MalformedURLException An exception that occurs when the URL is wrong
     */
    @BeforeSuite
    public void setUpAppium() throws MalformedURLException {

        final String URL_STRING = "http://127.0.0.1:4723/wd/hub";

        URL url = new URL(URL_STRING);

        //Use a empty DesiredCapabilities object
        DesiredCapabilities capabilities = new DesiredCapabilities();

        driver = new AndroidDriver<MobileElement>(url, capabilities);

        //Use a higher value if your mobile elements take time to show up
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    /**
     * Always remember to quit
     */
    @AfterSuite
    public void tearDownAppium() {
        driver.quit();
    }

    /**
     * Asserts the homepage headline
     */
    @Test
    public void testHomePageHeadline() {
        MobileElement el1 = (MobileElement) driver.findElementById("com.amazon.devicefarm:id/tasksText");
        String headLine = el1.getText();
        Assert.assertEquals(headLine, "Tasks");
    }

    /**
     * Asserts adding a new task
     */
    @Test (dependsOnMethods={"testHomePageHeadline"})
    public void testAddNewTask() {
        MobileElement el1 = (MobileElement) driver.findElementById("com.amazon.devicefarm:id/fab");
        el1.click();
        MobileElement el2 = (MobileElement) driver.findElementById("com.amazon.devicefarm:id/newTaskText");
        el2.sendKeys("Task 1");
        MobileElement el3 = (MobileElement) driver.findElementById("com.amazon.devicefarm:id/newTaskButton");
        el3.click();
        MobileElement el4 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout[1]/android.widget.RelativeLayout/android.widget.CheckBox");
        String headLine = el4.getText();
        Assert.assertEquals(headLine, "Task 1");
    }

    /**
     * Asserts adding a second new task
     */
    @Test (dependsOnMethods={"testAddNewTask"})
    public void testAddSecondNewTask() {
        MobileElement el1 = (MobileElement) driver.findElementById("com.amazon.devicefarm:id/fab");
        el1.click();
        MobileElement el2 = (MobileElement) driver.findElementById("com.amazon.devicefarm:id/newTaskText");
        el2.sendKeys("Task 2");
        MobileElement el3 = (MobileElement) driver.findElementById("com.amazon.devicefarm:id/newTaskButton");
        el3.click();
        MobileElement el4 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout[1]/android.widget.RelativeLayout/android.widget.CheckBox");
        String headLine = el4.getText();
        Assert.assertEquals(headLine, "Task 2");
    }

    /**
     * Asserts completing first task
     */
    @Test (dependsOnMethods={"testAddSecondNewTask"})
    public void testCompleteFirstTask() {
        MobileElement el4 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout[2]/android.widget.RelativeLayout/android.widget.CheckBox");
        el4.click();
        Assert.assertEquals(el4.getAttribute("checked"), "true");
    }

    /**
     * Asserts un-completing first task
     */
    @Test (dependsOnMethods={"testCompleteFirstTask"})
    public void testUnCompleteFirstTask() {
        MobileElement el4 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout[2]/android.widget.RelativeLayout/android.widget.CheckBox");
        el4.click();
        Assert.assertEquals(el4.getAttribute("checked"), "false");
    }

    /**
     * Asserts deleting first task
     */
    @Test (dependsOnMethods={"testUnCompleteFirstTask"})
    public void testDeleteFirstTask() {
        MobileElement el4 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout[2]/android.widget.RelativeLayout/android.widget.CheckBox");
        this.swipeElementAndroid(el4, "LEFT");
        MobileElement el1 = (MobileElement) driver.findElementById("android:id/button1");
        el1.click();
    }

    /**
     * Asserts remaining task name
     */
    @Test (dependsOnMethods={"testDeleteFirstTask"})
    public void testRemainingTaskName() {
        MobileElement el4 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.RelativeLayout/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout[1]/android.widget.RelativeLayout/android.widget.CheckBox");
        Assert.assertEquals(el4.getText(), "Task 2");
    }

    /**
    * Performs swipe inside an element
    *
    * @param el  the element to swipe
    * @param dir the direction of swipe
    * @version java-client: 7.3.0
    **/
    public void swipeElementAndroid(MobileElement el, String dir) {
        System.out.println("swipeElementAndroid(): dir: '" + dir + "'"); // always log your actions

        // Animation default time:
        //  - Android: 300 ms
        //  - iOS: 200 ms
        // final value depends on your app and could be greater
        final int ANIMATION_TIME = 200; // ms

        final int PRESS_TIME = 200; // ms

        int edgeBorder;
        PointOption pointOptionStart, pointOptionEnd;

        // init screen variables
        Rectangle rect = el.getRect();
        // sometimes it is needed to configure edgeBorders
        // you can also improve borders to have vertical/horizontal
        // or left/right/up/down border variables
        edgeBorder = 0;

        switch (dir) {
            case "DOWN": // from up to down
                pointOptionStart = PointOption.point(rect.x + rect.width / 2,
                        rect.y + edgeBorder);
                pointOptionEnd = PointOption.point(rect.x + rect.width / 2,
                        rect.y + rect.height - edgeBorder);
                break;
            case "UP": // from down to up
                pointOptionStart = PointOption.point(rect.x + rect.width / 2,
                        rect.y + rect.height - edgeBorder);
                pointOptionEnd = PointOption.point(rect.x + rect.width / 2,
                        rect.y + edgeBorder);
                break;
            case "LEFT": // from right to left
                pointOptionStart = PointOption.point(rect.x + rect.width - edgeBorder,
                        rect.y + rect.height / 2);
                pointOptionEnd = PointOption.point(rect.x + edgeBorder,
                        rect.y + rect.height / 2);
                break;
            case "RIGHT": // from left to right
                pointOptionStart = PointOption.point(rect.x + edgeBorder,
                        rect.y + rect.height / 2);
                pointOptionEnd = PointOption.point(rect.x + rect.width - edgeBorder,
                        rect.y + rect.height / 2);
                break;
            default:
                throw new IllegalArgumentException("swipeElementAndroid(): dir: '" + dir + "' NOT supported");
        }

        // execute swipe using TouchAction
        try {
            new TouchAction(driver)
                    .press(pointOptionStart)
                    // a bit more reliable when we add small wait
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                    .moveTo(pointOptionEnd)
                    .release().perform();
        } catch (Exception e) {
            System.err.println("swipeElementAndroid(): TouchAction FAILED\n" + e.getMessage());
            return;
        }

        // always allow swipe action to complete
        try {
            Thread.sleep(ANIMATION_TIME);
        } catch (InterruptedException e) {
            // ignore
        }
    }

}
