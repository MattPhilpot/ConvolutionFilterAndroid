package com.example.matt.myapplication

/**
 * Created by MattPhilpot on 9/6/2017.
 */

internal object ConvolutionMatrix {
    val PrewittX = arrayOf( intArrayOf(-1, 0, 1),
                            intArrayOf(-1, 0, 1),
                            intArrayOf(-1, 0, 1))

    val PrewittY = arrayOf( intArrayOf( 1,  1,  1),
                            intArrayOf( 0,  0,  0),
                            intArrayOf(-1, -1, -1))

    val KirschX = arrayOf(  intArrayOf( 5,  5,  5),
                            intArrayOf(-3,  0, -3),
                            intArrayOf(-3, -3, -3))

    val KirschY = arrayOf(  intArrayOf(5, -3, -3),
                            intArrayOf(5,  0, -3),
                            intArrayOf(5, -3, -3))

    val Laplacian3x3 = arrayOf( intArrayOf(-1, -1, -1),
                                intArrayOf(-1,  8, -1),
                                intArrayOf(-1, -1, -1))

    val Laplacian5x5OfGaussian = arrayOf(   intArrayOf( 0,  0, -1,  0,  0),
                                            intArrayOf( 0, -1, -2, -1,  0),
                                            intArrayOf(-1, -2, 16, -2, -1),
                                            intArrayOf( 0, -1, -2, -1,  0),
                                            intArrayOf( 0,  0, -1,  0,  0))

    val Gaussian5x5_1 = arrayOf(intArrayOf(2, 4, 5, 4, 2),
                                intArrayOf(4, 9, 12, 9, 4),
                                intArrayOf(5, 12, 15, 12, 5),
                                intArrayOf(4, 9, 12, 9, 4),
                                intArrayOf(2, 4, 5, 4, 2))

    val Gaussian5x5_2 = arrayOf(intArrayOf(1, 4, 6, 4, 1),
                                intArrayOf(4, 16, 24, 16, 4),
                                intArrayOf(6, 24, 36, 24, 6),
                                intArrayOf(4, 16, 24, 16, 4),
                                intArrayOf(1, 4, 6, 4, 1))

    val Laplacian5x5 = arrayOf( intArrayOf(-1, -1, -1, -1, -1),
                                intArrayOf(-1, -1, -1, -1, -1),
                                intArrayOf(-1, -1, 24, -1, -1),
                                intArrayOf(-1, -1, -1, -1, -1),
                                intArrayOf(-1, -1, -1, -1, -1))

    val Laplacian7x7 = arrayOf( intArrayOf(-1, -1, -1, -1, -1, -1, -1),
                                intArrayOf(-1, -1, -1, -1, -1, -1, -1),
                                intArrayOf(-1, -1, -1, -1, -1, -1, -1),
                                intArrayOf(-1, -1, -1, 48, -1, -1, -1),
                                intArrayOf(-1, -1, -1, -1, -1, -1, -1),
                                intArrayOf(-1, -1, -1, -1, -1, -1, -1),
                                intArrayOf(-1, -1, -1, -1, -1, -1, -1))

    val Laplacian7x7_2 = arrayOf(   intArrayOf(-1, -1, -1, -1, -1, -1, -1),
                                    intArrayOf(-1, -1, -1, -1, -1, -1, -1),
                                    intArrayOf(-1, -1, -1, -3, -1, -1, -1),
                                    intArrayOf(-1, -1, -3, 56, -3, -1, -1),
                                    intArrayOf(-1, -1, -1, -3, -1, -1, -1),
                                    intArrayOf(-1, -1, -1, -1, -1, -1, -1),
                                    intArrayOf(-1, -1, -1, -1, -1, -1, -1))

    val Laplacian7x7_3 = arrayOf(   intArrayOf(0, 0, 1, 1, 1, 0, 0),
                                    intArrayOf(0, 1, 3, 3, 3, 1, 0),
                                    intArrayOf(1, 3, 0, -7, 0, 3, 1),
                                    intArrayOf(1, 3, -7, -24, -7, 3, 1),
                                    intArrayOf(1, 3, 0, -7, 0, 3, 1),
                                    intArrayOf(0, 1, 3, 3, 3, 1, 0),
                                    intArrayOf(0, 0, 1, 1, 1, 0, 0))

    val Laplacian7x7OfGaussian = arrayOf(   intArrayOf(0, 0, 0, -1, 0, 0, 0),
                                            intArrayOf(0, 0, -1, -2, -1, 0, 0),
                                            intArrayOf(0, -1, -2, -6, -2, -1, 0),
                                            intArrayOf(-1, -2, -6, 52, -6, -2, -1),
                                            intArrayOf(0, -1, -2, -6, -2, -1, 0),
                                            intArrayOf(0, 0, -1, -2, -1, 0, 0),
                                            intArrayOf(0, 0, 0, -1, 0, 0, 0))
}
