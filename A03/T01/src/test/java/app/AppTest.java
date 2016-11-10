/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import static app.App.connect;
import java.io.IOException;
import static junit.framework.TestCase.fail;
import org.junit.Test;

public class AppTest {

    public AppTest() {
    }

    /**
     * Test connect() call with valid and invalid URLs (Internet connection
     * needed).
     */
    @Test
    public void testConnect() {
        System.out.println("connect");

        try {
            connect("www.google.com", "", 80);

        } catch (IOException e) {
            fail("Shouldn't throw exception for invalid host.");
        }

        try {
            connect("www.google.com", "", 1);
            fail("Should throw exception for invalid port.");
        } catch (IOException e) {
        }

        try {
            connect("asdfasf", "", 80);
            fail("Should throw exception for invalid host.");
        } catch (IOException e) {
        }
    }

    /**
     * Test start() call failing without arguments
     */
    @Test
    public void testStart() {
        System.out.println("start");

        String[] args = null;
        try {
            App.start(args);
            fail("Shouldn't work with null arguments!");
        } catch (Exception e) {
        }

        args = new String[0];
        try {
            App.start(args);
            fail("Shouldn't work without arguments!");
        } catch (Exception e) {
        }

    }

}
