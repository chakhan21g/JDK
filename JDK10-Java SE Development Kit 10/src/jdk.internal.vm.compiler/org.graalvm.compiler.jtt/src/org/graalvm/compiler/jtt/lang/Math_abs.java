/*
 * Copyright (c) 2011, 2012, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.graalvm.compiler.jtt.lang;

import org.junit.Test;

import org.graalvm.compiler.jtt.JTTTest;

/*
 */
public class Math_abs extends JTTTest {

    @SuppressWarnings("serial")
    public static class NaN extends Throwable {
    }

    public static double test(double arg) throws NaN {
        double v = Math.abs(arg);
        if (Double.isNaN(v)) {
            // NaN can't be tested against itself
            throw new NaN();
        }
        return v;
    }

    @Test
    public void run0() throws Throwable {
        runTest("test", 5.0d);
    }

    @Test
    public void run1() throws Throwable {
        runTest("test", -5.0d);
    }

    @Test
    public void run2() throws Throwable {
        runTest("test", 0.0d);
    }

    @Test
    public void run3() throws Throwable {
        runTest("test", -0.0d);
    }

    @Test
    public void run4() throws Throwable {
        runTest("test", java.lang.Double.NEGATIVE_INFINITY);
    }

    @Test
    public void run5() throws Throwable {
        runTest("test", java.lang.Double.POSITIVE_INFINITY);
    }

    @Test
    public void run6() throws Throwable {
        runTest("test", java.lang.Double.NaN);
    }

}
