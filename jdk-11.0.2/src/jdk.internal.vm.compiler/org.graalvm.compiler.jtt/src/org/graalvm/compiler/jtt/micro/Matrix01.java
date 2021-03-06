/*
 * Copyright (c) 2009, 2012, Oracle and/or its affiliates. All rights reserved.
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


package org.graalvm.compiler.jtt.micro;

import org.junit.Test;

import org.graalvm.compiler.jtt.JTTTest;

/*
 */
public class Matrix01 extends JTTTest {

    public static class Matrix {

        final int id;

        Matrix(int id) {
            this.id = id;
        }
    }

    public static int test(int arg) {
        if (arg == 0) {
            return matrix1(3) + matrix1(5);
        }
        if (arg == 1) {
            return matrix2(3) + matrix2(5);
        }
        if (arg == 2) {
            return matrix3(3) + matrix3(5);
        }
        if (arg == 3) {
            return matrix4(3) + matrix4(5);
        }
        if (arg == 4) {
            return matrix5(3) + matrix5(5);
        }
        return 42;
    }

    static int matrix1(int size) {
        Matrix[] matrix = new Matrix[size];
        fillMatrix(matrix, size);
        int count = 0;
        for (Matrix m : matrix) {
            if (m != null) {
                count++;
            }
        }
        return count;
    }

    static int matrix2(int size) {
        Matrix[][] matrix = new Matrix[size][size];
        fillMatrix(matrix, size * size);
        int count = 0;
        for (Matrix[] n : matrix) {
            for (Matrix m : n) {
                if (m != null) {
                    count++;
                }
            }
        }
        return count;
    }

    static int matrix3(int size) {
        Matrix[][][] matrix = new Matrix[size][5][size];
        fillMatrix(matrix, size * size * size);
        int count = 0;
        for (Matrix[][] o : matrix) {
            for (Matrix[] n : o) {
                for (Matrix m : n) {
                    if (m != null) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    static int matrix4(int size) {
        Matrix[][][][] matrix = new Matrix[size][2][size][3];
        fillMatrix(matrix, size * size * size * size);
        int count = 0;
        for (Matrix[][][] p : matrix) {
            for (Matrix[][] o : p) {
                for (Matrix[] n : o) {
                    for (Matrix m : n) {
                        if (m != null) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    static int matrix5(int size) {
        Matrix[][][][][] matrix = new Matrix[size][size][3][4][size];
        fillMatrix(matrix, size * size * size * size * size);
        int count = 0;
        for (Matrix[][][][] q : matrix) {
            for (Matrix[][][] p : q) {
                for (Matrix[][] o : p) {
                    for (Matrix[] n : o) {
                        for (Matrix m : n) {
                            if (m != null) {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    static void fillMatrix(Object[] matrix, int total) {
        for (int i = 0; i < 10000; i += 7) {
            int number = i % total;
            set(matrix, number);
        }
    }

    static void set(Object[] matrix, int number) {
        int val = number;
        Object[] array = matrix;
        while (!(array instanceof Matrix[])) {
            int index = val % array.length;
            val = val / array.length;
            array = (Object[]) array[index];
        }
        ((Matrix[]) array)[val % array.length] = new Matrix(number);
    }

    @Test
    public void run0() throws Throwable {
        runTest("test", 0);
    }

    @Test
    public void run1() throws Throwable {
        runTest("test", 1);
    }

    @Test
    public void run2() throws Throwable {
        runTest("test", 2);
    }

    @Test
    public void run3() throws Throwable {
        runTest("test", 3);
    }

    @Test
    public void run4() throws Throwable {
        runTest("test", 4);
    }

    @Test
    public void run5() throws Throwable {
        runTest("test", 5);
    }

}
