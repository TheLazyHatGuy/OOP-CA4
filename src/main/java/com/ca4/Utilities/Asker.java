package com.ca4.Utilities;

/*
Taken from Cameron's CA3
 */

import java.io.InputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Asker
{
    private final Scanner sc;
    private final PrintStream out;

    public Asker(InputStream in, PrintStream out)
    {
        sc = new Scanner(in);
        this.out = out;
    }

    public String askForString(String message)
    {
        out.print(message);
        return sc.nextLine();
    }

    public int askForInt(String message) throws InputMismatchException
    {
        int i;

        out.print(message);
        i = sc.nextInt();
        sc.nextLine();

        return i;
    }

    public double askForDouble(String message) throws InputMismatchException
    {
        double d;

        out.print(message);
        d = sc.nextDouble();
        sc.nextLine();

        return d;
    }

    public void clearScanner()
    {
        sc.next();
    }

    public void printBreak()
    {
        out.println();
    }

    public void printString(String message)
    {
        out.println(message);
    }
}
